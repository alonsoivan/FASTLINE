import java.io.IOException;


import java.util.HashMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class PruebaServer {
    Server server;
    
    public static void main(String[] args) throws IOException {
		
    	new PruebaServer();
	    
    }

    public PruebaServer() throws IOException {

        server = new Server();
        server.start();
        server.bind(54777);

        registrar(server.getKryo());

        server.addListener(new MiListener(server));
        
        System.out.println("Server escuchando");
    }

    public static void registrar(Kryo kryo) {
    	kryo.register(Util.class);
    	kryo.register(Util.PowerUp.class);
    	kryo.register(Ranking.class);
    	kryo.register(String[].class);
    }
}
class Util {
    public enum PowerUp {
        INK, VIBRATION, FREEZED
    }

    public PowerUp pu;

    public Util(){}

    public Util(PowerUp pu){
        this.pu = pu;
    }
}
class Ranking {
	public String[] names = new String[5];
	public String[] scores = new String[5];
	public String[] dates = new String[5];

    public Ranking(){}

    public Ranking(String[] names, String[] scores, String[] dates){
        this.names = names;
        this.scores = scores;
        this.dates = dates;
    }
}
class MiListener extends Listener {
    Server server;
    boolean  clientWaiting;
    Connection connection;
    HashMap<Connection,Connection> conexiones = new HashMap<>();

    public MiListener(Server server) {
        this.server = server;
        clientWaiting = false;
    }

    public void connected (Connection connection) {
        System.out.println("Cliente con id: " + connection.getID() + " e IP:"+connection.getRemoteAddressTCP().getAddress() + " conectado.");
                      
    }

    public void disconnected (Connection connection) {
        System.out.println("Cliente con id: " + connection.getID() + " desconectado.");
        if(conexiones.containsKey(connection)) {
        	// desconecto a la pareja cuando uno se desconecta
        	connection.close();
	        conexiones.get(connection).close();
        	
	        // les borro del registro
        	conexiones.remove(conexiones.get(connection));
	        conexiones.remove(connection);
        }
        
        if(this.connection != null)
	        if(this.connection.equals(connection))
	        	clientWaiting = false;
        
        // TODO mandar algo para que paren la partida y den señal de desconectado, 
        // igual si se desconectan por pérdida de conexion
    }

    public void received (Connection connection, Object object) {
    	if(object instanceof Ranking) {
    		PruebasDB.addScore((Ranking)object);
    		System.out.println("nuevo score actualizado");
		}
    	
    	// emparejar
		if(object instanceof String) {
			if(!((String)object).equals("db") && !((String)object).equals("client")) {
				System.out.print("recibo NOMBRE "+object);
				if(conexiones.get(connection) != null) {
					conexiones.get(connection).sendTCP((String)object);
					System.out.print("ENVIO NOMBRE "+object);
				}
			}
			
			if(((String)object).equals("db")) {
				connection.sendTCP(PruebasDB.getRanking());
			}
			
			if(((String)object).equals("client"))
				if(!clientWaiting) {
					this.connection = connection;
					clientWaiting = true;
					System.out.println("Cliente "+connection.getID()+" esperando oponente.");
	        	}else{
	        		conexiones.put(connection, this.connection);
	        		conexiones.put(this.connection, connection);
	                	
	        		System.out.println("Cliente "+connection.getID()+" esperando oponente.");
	                	
	                	
	        		clientWaiting = false;
	                	
	        		// TODO hacer esto bien
	        		this.connection.sendTCP("rdy");
	        		connection.sendTCP("rdy");
	                	
	        		System.out.println("Clientes "+ connection.getID() + " y " + this.connection.getID()+ " emparejados.");
	            }	
		}
    	
    	if(conexiones.containsKey(connection)) {
	        if(object instanceof Integer){
	        	
	        	conexiones.get(connection).sendTCP(object);
	        	System.out.println(object);
	        	
	        	if((Integer)object == 100) {
	        		connection.sendTCP(true);
	        		conexiones.get(connection).sendTCP(false);
        		}
	        }
	        
	        if(object instanceof Util) {
	        	conexiones.get(connection).sendTCP(object);
	        }
        }
    }
}
