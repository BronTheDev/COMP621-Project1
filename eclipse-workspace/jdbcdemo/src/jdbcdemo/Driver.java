package jdbcdemo;

import java.sql.*;
import java.util.Scanner;
public class Driver {
	
	
	private static String user_name = "";
	private static String password = "";
	private static String user_role = "";
	static Scanner user_input;
	private static String ConnectionUrl = "";
	private static Connection myConn;
	
	public Driver (String userN, String passW, String userR) {
		setUser_name(userN);
		setPassword(passW);
		setUser_role(userR);
		setConnectionUrl("jdbc:sqlserver://DESKTOP-GMPS9UK;databaseName=Hospital;user=" + userN + ";password=" + passW);
	}
/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// 1. Get a connection
			user_input = new Scanner(System.in);
			
			
			System.out.println("Please enter your Username, Password, and Role");
			
			System.out.print("Username: ");
			setUser_name(user_input.next()); 
			
			System.out.print("Password: ");
			setPassword(user_input.next());
			
			System.out.print("Role: ");
			setUser_role(user_input.next());
			
			String ConnectionUrl = "jdbc:sqlserver://DESKTOP-GMPS9UK;databaseName=Hospital;user=" + getUser_name() + ";password=" + getPassword();
			Connection myConn = DriverManager.getConnection(ConnectionUrl);
		
			//make_patient("Redd Foreman", "202 Rainy St", "10836", "862-760-3329", "88", myConn);
			boolean role_checked = check_role(getUser_name(), getPassword(), getUser_role(), myConn);
			preform_role(myConn, role_checked);
			myConn.close();
			System.exit(1);
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	*/
	
	public static void start_connection() {
		
		try {
			Connection myConn = DriverManager.getConnection(getConnectionUrl());
			
			//make_patient("Redd Foreman", "202 Rainy St", "10836", "862-760-3329", "88", myConn);
			boolean role_checked = check_role(getUser_name(), getPassword(), getUser_role(), myConn);
			preform_role(myConn, role_checked);
			myConn.close();
			System.exit(1);
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
	
	public static void preform_role(Connection myConn, boolean role_check) {
		boolean role_checked = check_role(getUser_name(), getPassword(), getUser_role(), myConn);
		
		if (role_checked) {
			user_input = new Scanner(System.in);
			if (getUser_role().equals("Doctor")) {
				System.out.println("Would you like to: \n 1: Show specific information for a single patient "
						+ "\n 2: Display all patient information?");
				int responce = user_input.nextInt();
				switch (responce) {
					case 1:
						System.out.print("Please enter the Patients ID: ");
						String ID = getInput();
						try {
							PreparedStatement myStmt = myConn.prepareStatement("select * from patients where ID=?");
							myStmt.setString(1, ID);
							ResultSet rs = myStmt.executeQuery();
							if (rs.next()) {           
								System.out.println(rs.getString("name") +  ", " + 
										rs.getString("address") + ", " + 
										rs.getString("ID") + ", " +
										rs.getString("phone_number") + ", " +
										rs.getInt("account_balance"));
							}
						} catch(Exception exc) {
							System.out.println("Could not execute command. Please check input.");
							exc.printStackTrace();
						}
					case 2:
						show_patients(myConn);
				}
				
			} else if (getUser_role().equals("Nurse")) {
				System.out.println("Would you like to: \n 1: Make a patient "
						+ "\n 2: Show specific information for a single patient "
						+ "\n 3: Display all patient information?");
				int responce = user_input.nextInt();
				switch (responce) {
				
				case 1:
					System.out.println("Please enter the Patient's Name, Address, ID, Phone, and Balance: ");
					
					System.out.print("Name: ");
					String new_name = getInput();
					System.out.print("Address: ");
					String new_address = getInput();
					System.out.print("ID: ");
					String new_ID = getInput();
					System.out.println("Phone-Number: ");
					String new_phone = getInput();
					System.out.println("Current Balance: ");
					String new_balance = getInput();
					make_patient(new_name, new_address, new_ID, new_phone, new_balance, myConn);
					show_patients(myConn);
				case 2:
					System.out.print("Please enter the Patients ID: ");
					String ID = user_input.next();
					try {
						PreparedStatement myStmt = myConn.prepareStatement("select * from patients where ID=?");
						myStmt.setString(1, ID);
						ResultSet rs = myStmt.executeQuery();
						if (rs.next()) {           
							System.out.println(rs.getString("name") +  ", " + 
									rs.getString("address") + ", " + 
									rs.getString("ID") + ", " +
									rs.getString("phone_number") + ", " +
									rs.getInt("account_balance"));
						}
					} catch(Exception exc) {
						System.out.println("Could not execute command. Please check input.");
						exc.printStackTrace();
					}
				case 3:
					show_patients(myConn);
					
				}
			}
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
	private static String getInput() {
	    Scanner scanner = new Scanner(System.in);
	    return scanner.nextLine();
	}

	private static String getPassword() {
		return password;
	}

	private static void setPassword(String password) {
		Driver.password = password;
	}

	private static String getUser_name() {
		return user_name;
	}

	private static void setUser_name(String user_name) {
		Driver.user_name = user_name;
	}

	private static String getUser_role() {
		return user_role;
	}

	private static void setUser_role(String user_role) {
		Driver.user_role = user_role;
	}

	private static Connection getMyConn() {
		return myConn;
	}

	private static void setMyConn(Connection myConn) {
		Driver.myConn = myConn;
	}

	private static String getConnectionUrl() {
		return ConnectionUrl;
	}

	public static void setConnectionUrl(String connectionUrl) {
		Driver.ConnectionUrl = connectionUrl;
	}
	

}
