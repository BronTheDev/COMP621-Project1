package jdbcdemo;
/**
*
* @author gksidburycrawford
*/
import java.sql.*;
import java.util.Scanner;
public class Driver {
	
	
	private static String user_name = "";
	private static String password = "";
	private static String user_role = "";
	static Scanner user_input;
	private static String ConnectionUrl = "";
	static Connection myConn;
	
	public Driver (String userN, String passW) {
		setUser_name(userN);
		setPassword(passW);
		setConnectionUrl("jdbc:sqlserver://DESKTOP-GMPS9UK;databaseName=Hospital;user=" + userN + ";password=" + passW);
		start_connection();
	}

	
	public static void start_connection() {
		
		try {
			@SuppressWarnings("unused")
			Connection myConn = DriverManager.getConnection(getConnectionUrl());
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
			
	public static void check_role() {
		try {
			myConn = DriverManager.getConnection(getConnectionUrl());
			PreparedStatement myStmt = myConn.prepareStatement("select * from users where user_name=? and password=?");
			myStmt.setString(1, getUser_name());
			myStmt.setString(2, getPassword());
			ResultSet rs = myStmt.executeQuery();
			if (rs.next()) {           
			    String roles =  rs.getString("role");
			    roles = roles.replaceAll("\\s", "");
			    setUser_role(roles);
		    } else {
			    	System.out.println("This user does not seem to have a role");
		    }
			   
		
		} catch (Exception exc) {
			System.out.println("Could not execute command. Please check input.");
			exc.printStackTrace();
		}
		
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
	

	public String[] search_patient() {
		String[] info = new String[5];
		try {
			PreparedStatement myStmt = getMyConn().prepareStatement("select * from patients where ID=?");
			myStmt.setString(1, getPassword());
			ResultSet rs = myStmt.executeQuery();
			if (rs.next()) {           
				info[0] = rs.getString("name");
				info[1] = rs.getString("address"); 
				info[2] = rs.getString("ID");
				info[3] = rs.getString("phone_number");
				int bill = rs.getInt("account_balance");
				info[4] = "" + bill;
			}
		} catch(Exception exc) {
			System.out.println("Could not execute command. Please check input.");
			exc.printStackTrace();
		}
		return info;
	}
	public String search_patient(String id) {
		String info = "";
		try {
			PreparedStatement myStmt = getMyConn().prepareStatement("select * from patients where ID=?");
			myStmt.setString(1, id);
			ResultSet rs = myStmt.executeQuery();
			if (rs.next()) {           
				info += rs.getString("name") +  ", " + 
						rs.getString("address") + ", " + 
						rs.getString("ID") + ", " +
						rs.getString("phone_number") + ", " +
						rs.getInt("account_balance");
			}
		} catch(Exception exc) {
			System.out.println("Could not execute command. Please check input.");
			exc.printStackTrace();
		}
		return info;
	}
	
	public void show_patients(Connection myConn) {
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
	
	public static String[] get_patients(Connection myConn) {

		try {
			
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			
			// 3. Execute SQL query
			ResultSet Show_Patients = myStmt.executeQuery("select * from patients");
			String[] patients = new String[Show_Patients.getFetchSize()];
			// 4. Process the result set
			int i = 0;
			while (Show_Patients.next()) {
				patients[i] = (Show_Patients.getString("name") +  ", " + 
				Show_Patients.getString("address") + ", " + 
				Show_Patients.getString("ID") + ", " +
				Show_Patients.getString("phone_number") + ", " +
				Show_Patients.getInt("account_balance"));
				i++;
			
			}
			return patients;
		} catch (Exception exc) {
			System.out.println("Could not execute command. Please check input.");
			exc.printStackTrace();
		}
		return null;
		
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

	static String getUser_role() {
		return user_role;
	}

	private static void setUser_role(String user_role) {
		Driver.user_role = user_role;
	}

	static Connection getMyConn() {
		return myConn;
	}

	private static String getConnectionUrl() {
		return ConnectionUrl;
	}

	public static void setConnectionUrl(String connectionUrl) {
		Driver.ConnectionUrl = connectionUrl;
	}
	

}
