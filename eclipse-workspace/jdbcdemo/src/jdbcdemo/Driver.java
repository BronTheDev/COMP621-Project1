package jdbcdemo;

import java.sql.*;
import java.util.Scanner;
public class Driver {
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// 1. Get a connection
			Scanner user_input = new Scanner(System.in);
			
			
			System.out.println("Please enter your Username, Password, and Role");
			
			System.out.print("Username: ");
			String user_name = user_input.next(); 
			
			System.out.print("Password: ");
			String password = user_input.next();
			
			System.out.print("Role: ");
			String user_role = user_input.next();
			
			String ConnectionUrl = "jdbc:sqlserver://DESKTOP-GMPS9UK;databaseName=Hospital;user=" + user_name + ";password=" + password;
			Connection myConn = DriverManager.getConnection(ConnectionUrl);
		
			//make_patient("Redd Foreman", "202 Rainy St", "10836", "862-760-3329", "88", myConn);
			
			boolean role_check = check_role(user_name, password, user_role, myConn);
			
			if (role_check) {
				show_patients(myConn);
				if (user_role.equals("Nurse")) {
					System.out.println("Would you like to make a patient? Please type \"Yes\"or \"No\" ");
					String responce = user_input.next();
					if (responce.equals("Yes")) {
						System.out.print("Please enter the Patient's Name: ");
						String name = user_input.next();
						System.out.print("Please enter the Patient's Address: ");
						String address = user_input.next();
						System.out.print("Please enter the Patients ID: ");
						String ID = user_input.next();
						System.out.print("Please enter the Patients Phone-Number: ");
						String phone = user_input.next();
						System.out.print("Please enter the Patients Current Balance: ");
						String balance = user_input.next();
						make_patient(name, address, ID, phone, balance, myConn);
						
					}
				}
				
			} else {
				System.out.println("Acess has been denied for this user. Please check credentials in system input");
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public static boolean check_role(String user_name, String password,String user_role, Connection myConn) {
		try {
		
			PreparedStatement myStmt = myConn.prepareStatement("select * from users where user_name=? and password=?");
			myStmt.setString(1, user_name);
			myStmt.setString(2, password);
			ResultSet rs = myStmt.executeQuery();
			if (rs.next()) {           
			    String roles =  rs.getString("role");
			    roles = roles.replaceAll("\\s", "");
			    
			    if (roles.equals(user_role)) {
			    	System.out.println("This user is confirmed to be a " + roles);
			    	return true;
			    } else {
			    	System.out.println("This user is confirmed to NOT be a " + roles);
			    	return false;
			    }
			    
			}
			return false;
		} catch (Exception exc) {
			System.out.println("Could not execute command. Please check input.");
			exc.printStackTrace();
		}
		return false;
		
	}
	public static void make_patient(String new_patient_name, String new_patient_address, 
			String new_patient_ID, String new_patient_phone, String new_patient_balance,
			Connection myConn) {
		
		try {
			
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			
			new_patient_name = "'" + new_patient_name + "', ";
			new_patient_address = "'" + new_patient_address + "', ";
			new_patient_ID = "'" + new_patient_ID + "', ";
			new_patient_phone = "'" + new_patient_phone + "', ";
			new_patient_balance = "'" + new_patient_balance + "'";
			
			String new_patient_stmt = "insert into patients " +
									" (name, address, ID, phone_number, account_balance)" +
									" values (" + new_patient_name + new_patient_address + new_patient_ID + new_patient_phone + new_patient_balance + ")";
			
			
			// 3. Execute SQL query
			myStmt.executeUpdate(new_patient_stmt);
			
			// 4. Process the result set
			System.out.println("Patient created.");
			
			} catch (Exception exc) {
				System.out.println("Could not execute command. Please check input.");
				exc.printStackTrace();
			}
		
	}
	
	public static void show_patients(Connection myConn) {
		try {
			
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			
			// 3. Execute SQL query
			ResultSet Show_Patients = myStmt.executeQuery("select * from patients");
			
			
			// 4. Process the result set
			while (Show_Patients.next()) {
				System.out.println(Show_Patients.getString("name") +  ", " + 
				Show_Patients.getString("address") + ", " + 
				Show_Patients.getString("ID") + ", " +
				Show_Patients.getString("phone_number") + ", " +
				Show_Patients.getInt("account_balance"));
			
			}
		} catch (Exception exc) {
			System.out.println("Could not execute command. Please check input.");
			exc.printStackTrace();
		}
			
	}
	

}
