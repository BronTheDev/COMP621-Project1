package jdbcdemo;

import java.sql.*;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// 1. Get a connection
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost;integratedSecurity=true");

			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			
			
			// 3. Execute SQL query
			ResultSet myRs = myStmt.executeQuery("select ")
			// 4. Process the result set
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

}
