package jdbcdemo;

import java.sql.*;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// 1. Get a connection
			String ConnectionUrl = "jdbc:sqlserver://localhost;databaseName=Hospital;integratedSecurity=true";
			Connection myConn = DriverManager.getConnection(ConnectionUrl);
		

			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			
			
			// 3. Execute SQL query
			ResultSet myRs = myStmt.executeQuery("select * from patients");
			
			// 4. Process the result set
			while (myRs.next()) {
				System.out.print(myRs.getString("name") +  ", " + 
				myRs.getString("address") + ", " + 
				myRs.getString("ID") + ", " +
				myRs.getString("phone_number") + ", " +
				myRs.getString("account_balance"));
				
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

}
