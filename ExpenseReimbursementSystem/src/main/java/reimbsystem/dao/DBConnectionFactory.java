package reimbsystem.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionFactory {
	public static String url = "jdbc:postgresql://" + System.getenv("TRAINING_DB_ENDPOINT") + "/" + 
			System.getenv("TRAINING_DB_REIMB");
	public static String username = System.getenv("TRAINING_DB_USERNAME");
	public static String password = System.getenv("TRAINING_DB_PASSWORD");
	
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url, username, password);
	}
}
