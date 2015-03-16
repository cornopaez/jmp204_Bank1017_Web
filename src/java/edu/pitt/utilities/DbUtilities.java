package edu.pitt.utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import edu.pitt.bank.Transaction;

/**
 * 
 * @author mauriciopaez
 *
 */
public interface DbUtilities {
	
	/**
     * Get SQL result set (data set) based on an SQL query
     * @param sql - SQL SELECT query
     * @return - ResultSet - java.sql.ResultSet object, contains results from SQL query argument
     * @throws SQLException
     */
	public ResultSet getResultSet(String sql) throws SQLException;
	
	/**
     * Executes INSERT, UPDATE, DELETE queries
     * @param sql - SQL statement - a well-formed INSERT, UPDATE, or DELETE query
     * @return true if execution succeeded, false if failed 
     */
	public boolean executeQuery(String sql);
	
	/**
     * Creates a model for JTable using default database table column names as table headers
     * @param sql - SQL SELECT query
     * @return a model for JTable
     * @throws SQLException
     */
	public DefaultTableModel getDataTable(String sql) throws SQLException;
	
	/**
     * Creates a model for JTable using custom column names
     * @param sqlQuery - SQL SELECT query
     * @param customColumnNames - an array containing custom column names for table headers
     * @return a model for JTable
     * @throws SQLException
     */
	public DefaultTableModel getDataTable(ArrayList<Transaction> arrayList, String[] customColumnNames) throws SQLException;
	
}
