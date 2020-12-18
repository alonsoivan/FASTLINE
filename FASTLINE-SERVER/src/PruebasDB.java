import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PruebasDB {

	public static void main(String[] args) {
		//addScore(r);
		//getRanking();
	}
	public static Ranking getRanking() {
		Ranking ranking = new Ranking();
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection conexion = DriverManager.getConnection ("jdbc:mysql://localhost:3306/fast_line", "root", "");
								
			Statement sentencia = conexion.createStatement();
			ResultSet cursor = sentencia.executeQuery("SELECT * FROM ranking order by SCORE DESC limit 5");	
		
			int i = 0;
			while (cursor.next()) {

				//System.out.println(cursor.getString(1) +cursor.getInt(2) + cursor.getDate(3));
				ranking.names[i] = cursor.getString(1);
				ranking.scores[i] = String.valueOf( cursor.getInt(2));
				ranking.dates[i] = cursor.getDate(3).toString();
				
				i++;
			}
			
			cursor.close();     // Cerrar ResultSet
			sentencia.close(); // Cerrar Statement
			conexion.close();  // Cerrar conexión
			
		}
		catch (ClassNotFoundException cn) {cn.printStackTrace();} 
		catch (SQLException e) {e.printStackTrace();}
		
		
		return ranking;
	}
	public static void addScore(Ranking ranking) {
		Connection conexion = null;
	    PreparedStatement ps;
		try {
			conexion = DriverManager.getConnection ("jdbc:mysql://localhost:3306/fast_line", "root", "");
			ps = conexion.prepareStatement("INSERT INTO ranking VALUES(?,?,?)");
			
			ps.setString(1, ranking.names[0]);
			ps.setInt(2, Integer.parseInt(ranking.scores[0]));
			ps.setDate(3, new Date(Long.parseLong(ranking.dates[0])));
			 
			conexion.setAutoCommit(false);
			int regis = ps.executeUpdate();
			 
			System.out.println(regis+" registros insertados.");
			 
			conexion.commit();
			conexion.setAutoCommit(true);
			
			conexion.close();
			ps.close();
		} catch (SQLException e) {
			try {
				System.out.println("Error al hacer el insert.");
				conexion.rollback();
			}catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}
