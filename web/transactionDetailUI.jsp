<%-- 
    Document   : transactionDetailUI
    Created on : Mar 8, 2015, 3:31:38 PM
    Author     : mauriciopaez
--%>

<%@page import="edu.pitt.bank.Transaction"%>
<%@page import="edu.pitt.bank.Account"%>
<%@page import="edu.pitt.bank.WebBankUtilities"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bank 1017 - Transaction Detail</title>
        <style>
            p.topText{
                text-align: center;
            }
            table.transactionDetail{
                //border: 1px solid black;
                width: 80%;
                margin-top: 2%;
                margin-left: 10%; 
                margin-right: 10%;
            }

            th {
                text-align: center; 
            }

        </style>
    </head>
    <body>
        <%
            if (session.getAttribute("authenticatedUser") != null && session.getAttribute("authenticatedBank") != null) {
                Account currentAccount = (Account) session.getAttribute("currentAccount");
        %>
        <p class="topText">These are all the transactions for the following account: <% out.print(currentAccount.getAccountID()); %>
        <table class="transactionDetail">
            <tr>
                <th>Transaction ID</th>
                <th>Amount</th>
                <th>Date</th>
                <th>Type</th>
                <th>Balance</th>
            </tr>
            <%
                    String table = WebBankUtilities.generateTransactionTable(currentAccount);
                    if (table == null) {
                        out.print("Seems you were logged off and there's nothing to show.");
                    } else {
                        out.print(table);                        
                    }
                } else {
                    response.sendRedirect("index.jsp");
                }
            %>
        </table>
    </body>
</html>
