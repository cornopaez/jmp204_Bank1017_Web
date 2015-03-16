package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.xml.sax.ErrorHandler;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.ErrorLogger;

public class Security {
	
	//This is the lone variable for the class
	private String userID;
	
	//These are the methods of this class
	/**
	 * This method validates a user from the database.
	 * 
	 * @param loginName - This specifies the login name of the user
	 * @param pin - This specifies the pin associated with the user
	 * @return - Returns the user as a Customer object. "Null" if user not found.
	 */
	public Customer validateLogin(String loginName, int pin){
		
		boolean match = false;
		Customer customer = null;
		
		String sql = "SELECT * FROM customer "; 
		sql += "WHERE loginName = '" + loginName + "' ";
		sql += "AND pin = " + pin + ";";
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			if(rs.next()){
				match = true;
				customer = new Customer(rs.getString("customerID"));
			}
		} catch (SQLException e) {
			ErrorLogger.log("The username or the pin are incorrect. No such customer.");
			ErrorLogger.log(e.getMessage());
		}
		if (match){
			return customer;
		} else {
			return null;
		}	
	}//End of method
	
	/**
	 * This method allows for the creation of an arrayList that contains every
	 * permission the user has in the system.
	 * 
	 * @return - Returns an arrayList of user permissions in String
	 * @param customer - This is the customer whose information we're getting
	 */
	public ArrayList<String> listUserGroups(Customer customer){
		
		ArrayList<String> results = new ArrayList<String>();
		
		String sql = "SELECT * FROM user_permissions "; 
		sql += "JOIN groups ON groupID = groupID2 ";
		sql += "WHERE groupOrUserID = '" + customer.getCustomerID() + "';";
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()){
				results.add(rs.getString("groupName"));
			}
		} catch (SQLException e) {
			ErrorLogger.log("The username or the pin are incorrect. No such customer.");
			ErrorLogger.log(e.getMessage());
		}
	
		return results;
		
	}//End of method
	
}//End of class
