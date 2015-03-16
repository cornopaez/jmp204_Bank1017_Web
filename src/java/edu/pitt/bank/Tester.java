package edu.pitt.bank;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;

/**
 * This a tester class. It contains a "main" function. It is build solely for the purpose of testing.
 * Most things in the class are of a temporary nature. No real documentation will be provided for this 
 * class.
 * 
 * @author mauriciopaez
 * @version 1.0
 *
 */

public class Tester {
	public static void main(String[] args) {
ArrayList<String> results = new ArrayList<String>();
		
		String sql = "SELECT * FROM user_permissions "; 
		sql += "JOIN groups ON groupID = groupID2 ";
		sql += "WHERE groupOrUserID = '69a1e316-5d40-11e3-94ef-97beef767f1d';";
		System.out.println(sql);
		
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
		
	}
}

