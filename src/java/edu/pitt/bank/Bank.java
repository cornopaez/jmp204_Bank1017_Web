package edu.pitt.bank;

//package edu.pitt.bank;

import edu.pitt.bank.Account;
import edu.pitt.bank.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.ErrorLogger;

public class Bank {
	
	//These are the variables used in this class
	private ArrayList<Account> accountList = new ArrayList<Account>(); 
	private ArrayList<Customer> customerList = new ArrayList<Customer>();
	
	/**
	 * 
	 */
	public Bank(){
		loadAccounts();
		setAccountOwners();	
	}
	
	//These are the methods
	
	/**
	 * This method loads all the accounts found in the database and adds them to the accountList property
	 */
	private void loadAccounts(){
		String sql = "SELECT * FROM account";
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()){
				Account a = new Account(rs.getString("accountID"));
				accountList.add(a);
			}
		} catch (SQLException e) {
			ErrorLogger.log("No accounts wehre found. No accounts were loaded to acountList.");
			ErrorLogger.log(e.getMessage());
		}
	}//End method
	
	/**
	 * This method facilitates the finding of an account, provided you provide the accountID
	 * 
	 * @param accountID - Specifies which account is to be found
	 * @return - Returns the account as an Account object
	 */
	private Account findAccount(String accountID){
		int index = 0;
		for (int i=0 ; i < accountList.size() ; i++) {
			if (accountList.get(i).getAccountID().equals(accountID)){
				index = i;
			}
		}
		return accountList.get(index);
	}//End of method
	
	
	/**
	 * This method facilitates the finding of a customer, provided you provide the customerID
	 * 
	 * @param customerID - Specifies which customer is to be found
	 * @return - Returns the customer as a Customer object
	 */
	private Customer findCustomer(String customerID){
		int index = 0;
		for (int i=0 ; i < customerList.size() ; i++) {
			if (customerList.get(i).getCustomerID().equals(customerID)){
				index = i;
			}
		}
		return customerList.get(index);	
	}
	
	
	/**
	 * This method loads all accounts and the respective owners and populates both the customerList property in
	 * the Bank class and the accountOwner property in the Account class. That is to say, it adds every owner to
	 * the respective account inside of the program.
	 */
	private void setAccountOwners(){
		
		String sql = "SELECT * FROM account "; 
		sql += "JOIN customer_account ON accountId = fk_accountId ";
		sql += "JOIN customer ON fk_customerId = customerId;";
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			Customer customer = null;
			while(rs.next()){
				customer = new Customer(rs.getString("customerID"));
				customerList.add(customer);
				findAccount(rs.getString("accountID")).addAccountOwner(customer);
				customer = null;
			}
		} catch (SQLException e) {
			ErrorLogger.log("No accounts or owners were loaded. Both of these properties should have not changed.");
			ErrorLogger.log(e.getMessage());
		}
		
		
	}//End method
	
	//These are the getters and setters

	/**
	 * This is the account list getter. Self-explanatory, move along...
	 * 
	 * @return - Returns the account list as an arrayList of Accounts
	 */
	public ArrayList<Account> getAccountList() {
		return accountList;
	}

	/**
	 * This is the customer list getter. Self-explanatory, move along...
	 * 
	 * @return - Returns the customer list as an arrayList of Customers
	 */
	public ArrayList<Customer> getCustomerList() {
		return customerList;
	}
	
	

}
