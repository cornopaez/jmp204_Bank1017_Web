package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.ErrorLogger;

/**
 * The Customer class allows creation of new customers in the database and the loading of existing ones.
 * Dmitriy Babichenko contributed the framework for this class.
 * 
 * @author mauriciopaez
 * @version 1.0
 *
 */
public class Customer {
	
	//These are the variables used in this class
	private String customerID;
	private String firstName;
	private String lastName;
	private String ssn;
	private String streetAddress;
	private String city;
	private String state;
	private int zip;
	private String loginName;
	private int pin;
	
	/**
	 * This constructor allows for the loading of information from an existing costumer from the database. You must
	 * provide the customerID. 
	 * 
	 * @param customerID - This specifies the customer whose information is going to be loaded.
	 */
	public Customer(String customerID){
		String sql = "SELECT * FROM customer "; 
		sql += "WHERE customerID = '" + customerID + "'";
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			if(rs.next()){
				this.firstName = rs.getString("firstName");
				this.lastName = rs.getString("lastName");
				this.ssn = rs.getString("ssn");
				this.streetAddress = rs.getString("streetAddress");
				this.city = rs.getString("city");
				this.state = rs.getString("state");
				this.zip = rs.getInt("zip");
				this.loginName = rs.getString("loginName");
				this.pin = rs.getInt("pin");
				this.customerID = rs.getString("customerID");
				
			}
		} catch (SQLException e) {
			ErrorLogger.log("The customer information could not be loaded.");
			ErrorLogger.log(e.getMessage());
		}
	}//End of constructor
	
	
	/**
	 * This constructor allows for the creation of new account and its insertion to the proper database. The "customerID"
	 * is created at random using Java's UUID utility. You must specify the various fields in the signature. 
	 * 
	 * @param lastName - This specifies the last name of the customer
	 * @param firstName - This specifies the first name of the customer
	 * @param ssn - This specifies the ssn of the customer
	 * @param loginName - This specifies the login name of the customer
	 * @param pin - This specifies the pin of the customer
	 * @param streetAddress - This species the street address of the customer
	 * @param city - This specifies the city of the customer
	 * @param state - This specifies the state of the customer
	 * @param zip - This specifies the zip code of the customer
	 */
	public Customer(String lastName, String firstName, String ssn, String loginName, int pin, String streetAddress,String city, String state, int zip){
		this.customerID = UUID.randomUUID().toString();
		this.lastName = lastName;
		this.firstName = firstName;
		this.ssn = ssn;
		this.loginName = loginName;
		this.pin = pin;
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.zip = zip;
		
		String sql = "INSERT INTO customer ";
		sql += "(customerID,lastName,firstName,ssn,loginName,pin,streetAddress,city,state,zip) ";
		sql += " VALUES ";
		sql += "('" + this.customerID + "', ";
		sql += "'" + this.lastName + "', ";
		sql += this.firstName + ", ";
		sql += this.ssn + ", ";
		sql += this.loginName + ", ";
		sql += "'" + this.pin + "', ";
		sql += "'" + this.streetAddress + "', ";
		sql += "'" + this.city + "', ";
		sql += "'" + this.state + "', ";
		sql += "'" + this.zip + "', ";
		sql += "CURDATE());";
		
		DbUtilities db = new MySqlUtilities();
		db.executeQuery(sql);
	}//End of constructor
	
	// These are the setters and getters

	/**
	 * This is the first name getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the first name of the customer as a String
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * This is the customerID getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the customerID as a String
	 */
	public String getCustomerID() {
		return customerID;
	}


	/**
	 * This is the last name getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the last name of the customer as a String
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * This is the street address getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the street address of the customer as a String
	 */
	public String getStreetAddress() {
		return streetAddress;
	}

	/**
	 * This is the city getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the city of the customer as a String
	 */
	public String getCity() {
		return city;
	}

	/**
	 * This is the state getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the state of the customer as a String
	 */
	public String getState() {
		return state;
	}

	/**
	 * This is the zip getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the zip of the customer as an Int
	 */
	public int getZip() {
		return zip;
	}
	
	
	
}//End of class
