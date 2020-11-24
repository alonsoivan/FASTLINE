package com.ivn.game.managers;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.ivn.game.MainGame;
import com.ivn.game.models.MidBall;

import java.io.IOException;

import static com.ivn.game.screens.MultiPlayerScreen.disconected;
import static com.ivn.game.screens.MultiPlayerScreen.endGame;

public class NetworkManager extends Listener.ThreadedListener {

    static public final int tcpPort = 13913;
    //static public final int udpPort = 18586;
    static public final String address = "2.tcp.ngrok.io";
    static public final int timeOut = 6000;

    static public Client client;

    private MainGame game;

    public NetworkManager(MainGame game){
        super(new Listener());
        this.game = game;

        try {

            client = new Client();
            client.start();

            registrar();

            client.addListener(this);

            client.connect(timeOut, address, tcpPort);

            client.sendTCP(1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void connected (Connection connection) {
        System.out.println("Conectado al servidor.");
    }

    public void received (Connection connection, Object object) {
        if (object instanceof Integer) {
            if((Integer) object != 1)
                MidBall.enemyScore = (Integer)object;
        }

        if(object instanceof String){
            if(object.equals("rdy")){
                game.isMultiReady = true;
            }
        }

        if(object instanceof Boolean){
            if((Boolean) object)
                MidBall.myWinRounds++;
            else
                MidBall.enemyWinRounds++;

            HUD.setRounds(MidBall.myWinRounds,MidBall.enemyWinRounds);


            if(MidBall.myWinRounds >= 3 || MidBall.enemyWinRounds >= 3)
                endGame = true;
            else
                MidBall.nextRound();
        }
    }

    public void disconnected (Connection connection) {
        System.out.println("Desconectado del servidor.");

        if(!client.isConnected()) {
            disconected = true;

            System.out.println("disconected TRUE");
        }

        // TODO distinguir entre desconexión por rival o internet propio
        //System.exit(0);
    }

    // This registers objects that are going to be sent or received over the network.
    public void registrar() {
        //Kryo kryo = client.getKryo();
    }
}