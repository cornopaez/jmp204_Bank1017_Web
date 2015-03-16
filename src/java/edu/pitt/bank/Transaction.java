package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.ErrorLogger;

/**
 * The Transaction class allows creation of new transaction in the database and the loading of existing ones.
 * Dmitriy Babichenko contributed the framework for this class.
 * 
 * @author mauriciopaez
 *
 */
public class Transaction {
	
	//These are the variables used in this class
	private String transactionID;
	private String accountID;
	private String type;
	private double amount;
	private double balance;
	private Date transactionDate; 
	
	/**
	 * This method allows of the loading of existing transactions from the database. You must
	 * provide the transactionID.
	 * 
	 * @param transactionID - This specifies the transaction you'd like to load
	 */
	public Transaction(String transactionID){
		String sql = "SELECT * FROM transaction "; 
		sql += "WHERE transactionID = '" + transactionID + "'";
		//System.out.println(sql);
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()){
				this.transactionID = rs.getString("transactionID");
				this.accountID = rs.getString("accountID");
				this.type = rs.getString("type");
				this.amount = rs.getDouble("amount");
				this.balance = rs.getDouble("balance");
				this.transactionDate = rs.getDate("transactionDate");
			}
		} catch (SQLException e) {
			ErrorLogger.log("The Transaction method failed. No transactions were loaded.");
			ErrorLogger.log(e.getMessage());
		}
	}//End of constructor
	
	/**
	 * This constructor allows for the creation of new transactions and their insertions into the proper
	 * database. The transactionID is created at random using Java's UUID utility. You must specify the
	 * various fields in the signature.
	 * 
	 * @param accountID - This specifies the account to which this transaction will post
	 * @param type - This specifies the type of transaction it is
	 * @param amount - This specifies the amount of the transaction
	 * @param balance - This specifies the new balance of the account
	 */
	public Transaction(String accountID, String type, double amount, double balance){
		this.transactionID = UUID.randomUUID().toString();
		this.type = type;
		this.amount = amount;
		this.accountID = accountID;
		this.balance = balance;
		
		String sql = "INSERT INTO transaction ";
		sql += "(transactionID, accountID, amount, transactionDate, type, balance) ";
		sql += " VALUES ";
		sql += "('" + this.transactionID + "', ";
		sql += "'" + this.accountID + "', ";
		sql += amount + ", ";
		sql += "CURDATE(), ";
		sql += "'" + this.type + "', ";
		sql += balance + ");";
		
		//System.out.println(sql);
		
		DbUtilities db = new MySqlUtilities();
		db.executeQuery(sql);
	}//End of constructor
	
	//These are the getters and setters

	/**
	 * This is the accountID getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the accountID as a String
	 */
	public String getAccountID() {
		return accountID;
	}

	/**
	 * This is the type of transaction getter
	 * 
	 * @return - Returns the type of the transaction as a String
	 */
	public String getType() {
		return type;
	}

	/**
	 * This is the amount getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the amount of the transaction as a Double
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * This is the balance getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the new balance of the account as a Double
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * This is the transactionID getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the transactionID as a String
	 */
	public String getTransactionID() {
		return transactionID;
	}

	/**
	 * This is the transactionDate getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the date of the transaction as Date
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}
	
	
	
}//End of class

