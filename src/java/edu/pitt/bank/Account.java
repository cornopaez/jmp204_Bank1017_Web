package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.ErrorLogger;

/**
 * The Account class allows creation of new accounts in the database and maintenance of existing ones by  
 * doing deposits and withdrawals. For each one of these transactions, the class is tasked with creating a 
 * unique transaction record and saving it to the corresponding database. Dmitriy Babichenko contributed the
 * framework for this class.
 * 
 * @author mauriciopaez
 * @version 1.0
 */
public class Account {
	
	//These are the variables used in this class
	private String accountID;
	private String type;
	private String transactionType;
	private double balance;
	private double interestRate;
	private double penalty;
	private String status;
	private Date dateOpen;
	private ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
	private ArrayList<Customer> accountOwners = new ArrayList<Customer>();
	
	
	/**
	 * This is account constructor loads information from the database on to memory regarding the account 
	 * specified by the parameter "accountID". This includes all the transactions related to the account in question.
	 * 
	 * @param accountID - This allows to specify the account detains you'd like to load from the databases (String)
	 */
	public Account(String accountID){
		
		//This loads the account information
		String sql = "SELECT * FROM account "; 
		sql += "WHERE accountID = '" + accountID + "'";
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()){
				this.accountID = rs.getString("accountID");
				this.type = rs.getString("type");
				this.balance = rs.getDouble("balance");
				this.interestRate = rs.getDouble("interestRate");
				this.penalty = rs.getDouble("penalty");
				this.status = rs.getString("status");
				this.dateOpen = rs.getDate("dateOpen");
			}
		} catch (SQLException e) {
			ErrorLogger.log("The account information could not be loaded.");
			ErrorLogger.log(e.getMessage());
		}
		
		//This loads the transactions that are associated with the account
		String sql2 = "SELECT * FROM transaction "; 
		sql2 += "WHERE accountID = '" + accountID + "'";
		DbUtilities db2 = new MySqlUtilities();
		try {
			ResultSet rs2 = db2.getResultSet(sql2);
			while(rs2.next()){
				createTransaction(rs2.getString("transactionID"));
			}
		} catch (SQLException e) {
			ErrorLogger.log("The transacions could not be loaded for the specified account.");
			ErrorLogger.log(e.getMessage());
		}
	}//End of constructor
	
	/**
	 * This constructor allows for the creation of new accounts. The "accountID" is generated at random using Java's UUID utility.
	 * You must specify both the account type and the initial balance. All other values in the account are set to zero.
	 * 
	 * @param accountType - This specifies the type of account being opened
	 * @param initialBalance - This specifies the initial balance of the account being opened
	 */
	public Account(String accountType, double initialBalance){
		this.accountID = UUID.randomUUID().toString();
		this.type = accountType;
		this.balance = initialBalance;
		this.interestRate = 0;
		this.penalty = 0;
		this.status = "active";
		this.dateOpen = new Date();
		
		String sql = "INSERT INTO account ";
		sql += "(accountID,type,balance,interestRate,penalty,status,dateOpen) ";
		sql += " VALUES ";
		sql += "('" + this.accountID + "', ";
		sql += "'" + this.type + "', ";
		sql += this.balance + ", ";
		sql += this.interestRate + ", ";
		sql += this.penalty + ", ";
		sql += "'" + this.status + "', ";
		sql += "CURDATE());";
		
		DbUtilities db = new MySqlUtilities();
		db.executeQuery(sql);
	}//End of constructor
	
	
	/* These are the methods*/
	
	/**
	 * This is the withdraw method. Self explanatory. Move along...
	 * 
	 * @param amount - This specifies the amount of the withdrawal
	 */
	public void withdraw(double amount){
		this.balance -= amount;
		createTransaction(this.accountID, this.transactionType, amount, this.balance);
		updateDatabaseAccountBalance();
	}
	
	/**
	 * This is the deposit method. Self explanatory. Move along...
	 * 
	 * @param amount - This specifies the amount of the deposit 
	 */
	public void deposit(double amount){
		this.balance += amount;
		createTransaction(this.accountID, this.transactionType, amount, this.balance);
		updateDatabaseAccountBalance();
	}
	
	/**
	 * This method is in charge of updating the balance of the account in the database once a transaction has occurred.
	 * 
	 */
	private void updateDatabaseAccountBalance(){
		String sql = "UPDATE account SET balance = " + this.balance + " ";
		sql += "WHERE accountID = '" + this.accountID + "';";
		
		DbUtilities db = new MySqlUtilities();
		db.executeQuery(sql);
	}
	
	/**
	 * This method allows the loading of the information pertaining to a specific transaction.
	 * You must provide the transaction ID.
	 * 
	 * @param transactionID - This specifies the transaction being fetched
	 * @return - This method returns the information that has been fetched
	 */
	private Transaction createTransaction(String transactionID){
		Transaction t = new Transaction(transactionID);
		transactionList.add(t);
		return t;
	}
	
	/**
	 * This method allows for the creation of a new transaction record and its insertion to the proper
	 * database. After the transaction record has been created, it is added to the local list and returned
	 * to the user.
	 * 
	 * @param accountID - This specifies the account to which the transaction will be posted to
	 * @param type - This specifies the type of account
	 * @param amount - This specifies the amount of the transaction to be created
	 * @param balance - This specifies the new balance of the account
	 * @return - This method returns the transaction record it just created with the information provided
	 */
	private Transaction createTransaction(String accountID, String type, double amount, double balance){
		Transaction t = new Transaction(accountID, transactionType, amount, balance);
		transactionList.add(t);
		return t;
	}
	
	/**
	 * This method adds a new owner to an account
	 * 
	 * @param accountOwner - A customer you wish to add as an owner of an account
	 */
	public void addAccountOwner(Customer accountOwner){
		accountOwners.add(accountOwner);
	}
	
	
	
	// These are the setters and getters
	
	/**
	 * This is the AccountID getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the AccountID as a String
	 */
	public String getAccountID(){
		return this.accountID;
	}
	
	/**
	 * This is the balance getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the balance of the account as a Double
	 */
	public double getBalance(){
		return this.balance;
	}

	/**
	 * This is the status getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the status of the account as a String
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * This is the status setter. Self explanatory. Move along...
	 * 
	 * @param status - This specifies the state of the account
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * This is the AccountOwners getter. It returns the owners of the account in the form of an ArrayList
	 * 
	 * @return - Returns the owners of the account as an ArrayList of Costumers
	 */
	public ArrayList<Customer> getAccountOwners() {
		return accountOwners;
	}

	/**
	 * This is the type of the account getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the type of the account as a String
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * This is the interest rate getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the interest rate of the account as a Double
	 */
	public double getInterestRate() {
		return interestRate;
	}
	
	/**
	 * This is the penalty getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the penalty of the account as a Double
	 */
	public double getPenalty() {
		return penalty;
	}
	
	/**
	 * This is the date opened getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the date in which the account was opened as a Date
	 */
	public Date getDateOpen() {
		return dateOpen;
	}

	/**
	 * This is the transaction list getter. Self explanatory. Move along...
	 * 
	 * @return - Returns the list of transaction of the account as an ArrayList of Transactions
	 */
	public ArrayList<Transaction> getTransactionList() {
		return transactionList;
	}
	
	
	/**
	 * This is the transaction type list setter. Self explanatory. Move along...
	 * 
	 * @param transactionType - Sets the type of transaction occurring
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public String toString(){
		return getAccountID();
	}
	
}//End of class
