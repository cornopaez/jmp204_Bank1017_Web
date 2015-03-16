/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pitt.bank;

import edu.pitt.utilities.ErrorLogger;
import java.util.ArrayList;

/**
 * This class contains some advanced bank functions too complex to fit in the regular .JSP. Each of the functions is described in detail
 * under their respective documentation.
 *
 * @author mauriciopaez
 * @version 1.0
 */
public class WebBankUtilities {

    /**
     * This method allows for both parsing of a String that contains a value and depositing said value in the specified account. It returns 
     * a String that contains the result of the operation. If the parsing goes well, the method will return a positive message. It also checks
     * account status to process the transaction.
     * 
     * @param stringAmount - Value to be parsed
     * @param currentAccount - Account to which the value is going to be deposited
     * @return - Returns a plain text description of the result of the method.
     */
    public static String parseAndDeposit(String stringAmount, Account currentAccount) {

        String confirmText = "";

        if (stringAmount != null) {
            if (!stringAmount.equals("")) {
                if (currentAccount.getStatus().equals("active")) {

                    int parsedAmount = 0;
                    int irsLimit = 10000;

                    //We'll try and parse the amount provided
                    try {
                        parsedAmount = Integer.parseInt(stringAmount);
                        currentAccount.setTransactionType("Deposit");
                        currentAccount.deposit(parsedAmount);
                        confirmText = "<br/><font color=\"green\">The transaction has been posted!</font><br/>";

                        if (parsedAmount >= irsLimit) {
                            confirmText += "<br/>Your transaction has been reported to the IRS.";
                        }

                    } catch (Exception e) {
                        ErrorLogger.log("Could not parse the transaction amount. Invalid characters.");
                        ErrorLogger.log(e.getMessage());
                        confirmText = "<p><br/><font color=\"red\">The amount provided contains invalid characters.</font>";
                    }

                } else {
                    confirmText = "<p><br/><font color=\"red\">Your account is " + currentAccount.getStatus() + ". No transactions for you!</font>";
                }
            }
        }

        return confirmText;

    }

    /**
     * This method allows for both parsing of a String that contains a value and withdrawing said value in the specified account. It returns 
     * a String that contains the result of the operation. If the parsing goes well, the method will return a positive message. It also checks
     * account status to process the transaction.
     * 
     * @param stringAmount - Value to be parsed
     * @param currentAccount - Account from which the value is going to be withdrawn
     * @return - Returns a plain text description of the result of the method.
     */
    public static String parseAndWithdraw(String stringAmount, Account currentAccount) {

        String confirmText = "";

        if (stringAmount != null) {
            if (!stringAmount.equals("")) {
                if (currentAccount.getStatus().equals("active")) {

                    int parsedAmount = 0;
                    int irsLimit = 10000;

                    //We'll try and parse the amount provided
                    try {
                        parsedAmount = Integer.parseInt(stringAmount);

                        if (parsedAmount < currentAccount.getBalance()) {
                            currentAccount.setTransactionType("Withdraw");
                            currentAccount.withdraw(parsedAmount);
                            confirmText = "<br/><font color=\"green\">The transaction has been posted!</font>";
                        } else {
                            confirmText = "<br/><font color=\"red\">You have insufficient funds!</font>";
                        }

                    } catch (Exception e) {
                        ErrorLogger.log("Could not parse the transaction amount. Invalid characters.");
                        ErrorLogger.log(e.getMessage());
                        confirmText = "<p><br/><font color=\"red\">The amount provided contains invalid characters.</font>";
                    }

                } else {
                    confirmText = "<p><br/><font color=\"red\">Your account is " + currentAccount.getStatus() + ". No transactions for you!</font>";
                }
            }
        }

        return confirmText;
    }

    /**
     * This method creates an HTML text string in which the account details are formatted for display. The formatting is the following:
     * Type:
     * Balance:
     * Interest Rate:
     * Penalty:
     * Status:
     * 
     * @param currentAccount - Account whose details you need formatted
     * @return - The details formatted in HTML
     */
    public static String accountDetailsToText(Account currentAccount) {

        String detailsString = "";

        detailsString = "Type: " + currentAccount.getType() + ". <br/>";
        detailsString += "Balance: $" + currentAccount.getBalance() + "<br/>";
        detailsString += "Interest Rate: " + (currentAccount.getInterestRate() * 100) + "% <br/>";
        detailsString += "Penalty: $" + currentAccount.getPenalty() + "<br/>";
        detailsString += "Status: " + currentAccount.getStatus() + "<br/>";

        return detailsString;

    }

    /**
     * This method looks through the provided bank for a value selected and returns the entire matching account.
     * 
     * @param currentCboSelect - Account ID of the account you wish to load
     * @param bank - Bank in which the account is located
     * @return - Account selected!
     */
    public static Account setCurrentAccount(String currentCboSelect, Bank bank) {

        Account currentAccount = null;

        for (Account acct : bank.getAccountList()) {
            if (acct.getAccountID().equals(currentCboSelect)) {
                currentAccount = acct;
            }
        }

        return currentAccount;
    }

    /**
     * This method creates an HTML formatted text listing the contents an ArrayList which 
     * contains all of the user's permissions in the system.
     * 
     * @param lug - This is the array list that contains the user permissions
     * @param customer - This is the user logged into the system
     * @return - An HTML formatter text with the results.
     */
    public static String generateUserPermissions(ArrayList<String> lug, Customer customer) {

        //Generating the user permissions text line
        //This will populate the user permissions in the system
        int counter = 0;
        String currentGroups = "Customer, ";

        for (String group : lug) {
            //Adding all the groups the user belongs to
            if (!group.equalsIgnoreCase("Customer")) {
                currentGroups += group;

                //Adding comas or periods appropiately
                if (counter < lug.size()) {
                    currentGroups += ", ";
                } else {
                    currentGroups += ".";
                }
                counter++;
            }
        }

        String welcomeText = "<p id=\"groupText\">Welcome " + customer.getFirstName() + " " + customer.getLastName() + "! ";
        welcomeText += "Your have the following permissions in the system: <br/> ";
        welcomeText += currentGroups + ".</p>";

        return welcomeText;

    }

    /**
     * This method creates an HTML formatted text that generates a table out of all the transactions of an Account.
     * 
     * @param account - The account in question
     * @return - The table!
     */
    public static String generateTransactionTable(Account account) {
        
        String table = "";
        
        for (Transaction trans : account.getTransactionList()) {
            table += "<tr>";
            table += "<td align=\"center\">" + trans.getTransactionID() + "</td>";
            table += "<td align=\"right\">$" + trans.getAmount() + "</td>";
            table += "<td align=\"center\">" + trans.getTransactionDate() + "</td>";
            table += "<td align=\"center\">" + trans.getType() + "</td>";
            table += "<td align=\"right\">$" + trans.getBalance() + "</td>";
            table += "</tr>";
        }
        
        return table;
    }

}
