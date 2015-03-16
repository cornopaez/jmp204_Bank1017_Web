
<%@page import="edu.pitt.bank.WebBankUtilities"%>
<%@page import="edu.pitt.utilities.ErrorLogger"%>
<%@page import="edu.pitt.bank.Account"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.pitt.bank.Bank"%>
<%@page import="edu.pitt.bank.Customer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bank 1017 - Account Details</title>
        <style>
            #groupText {
                text-align: center;
            }

            table.detailsTable{
                border: 0;
                margin-left:auto; 
                margin-right:auto;
            }

            #firstRow{
                text-align: left;
            }

            #secondRow {
                text-align: right;
            }

            #thirdRow {
                text-align: left;
            }
            
            #fourthRow {
                text-align: right;
            }

        </style>
    </head>
    <body>
        <%
            //Some local variables
            String welcomeText = null;
            Customer ac = null;
            Bank ab = null;
            ArrayList<String> lug = null;
            Account currentAccount = null;
            String transAmount = null;
            String confirmText = "";

            //Checkign for a successful value transnfer from the login page
            if (session.getAttribute("authenticatedUser") != null && session.getAttribute("authenticatedBank") != null) {

                //Turning all the values given into something useful
                ac = (Customer) session.getAttribute("authenticatedUser");
                ab = (Bank) session.getAttribute("authenticatedBank");
                lug = (ArrayList) session.getAttribute("listUserGroups");

                welcomeText = WebBankUtilities.generateUserPermissions(lug, ac);

        %>

        <br/>
        <%            out.print(welcomeText);
        %>
        <br/>

        <table class="detailsTable">
            <tr>
                <td>Your accounts: </td>
                <td id="firstRow" colspan="2">
                    <form id="frmAccountDetails" name="frmAccountDetails">
                        <select id="cboAccountList" name="cboAccountList">
                            <%
                                for (Account account : ab.getAccountList()) {
                                    for (Customer customer : account.getAccountOwners()) {
                                        if (customer.getCustomerID().equals(ac.getCustomerID())) {
                                            out.print("<option value=" + account.getAccountID() + ">" + account.getAccountID() + "</option>");
                                        }
                                    }
                                }
                            %>
                        </select>
                        <input type="Submit" id="btnShowDetails" name="btnShowDetails" value="Get Details" />

                        <%
                            if (request.getParameter("cboAccountList") != null) {
                                currentAccount = WebBankUtilities.setCurrentAccount(request.getParameter("cboAccountList"), ab);
                                session.setAttribute("currentAccount", currentAccount);
                                session.setAttribute("detailsString", WebBankUtilities.accountDetailsToText(currentAccount));
                            }
                        %>
                    </form>
                </td>
            </tr>
            <tr>
                <td> 
                    <b>Account Details:</b> <br/>
                    <%
                        if (session.getAttribute("detailsString") == null) {
                            out.print("Type: <br/>Balance: $<br/>Interest Rate: %<br/>Penalty: $<br/>Status: <br/>");
                        } else {
                            out.print(session.getAttribute("detailsString"));
                        }
                    %>
                </td>
                <td id="secondRow"> Amount:</td>
                <td>
                    <form id="frmTransactions" name="frmTransactions">
                        <input type="text" id="txtAmount" name="txtAmount" value="" /><br/>
                        <input type="submit" id="btnDeposit" name="btnDeposit" value="Deposit"/>
                        <input type="submit" id="btnWithdraw" name="btnWithdraw" value="Withdraw"/>

                        <%
                                transAmount = request.getParameter("txtAmount");

                                if (request.getParameter("btnDeposit") != null) {
                                    confirmText = WebBankUtilities.parseAndDeposit(transAmount, (Account) session.getAttribute("currentAccount"));
                                    out.print(confirmText);
                                }

                                if (request.getParameter("btnWithdraw") != null) {
                                    confirmText = WebBankUtilities.parseAndWithdraw(transAmount, (Account) session.getAttribute("currentAccount"));
                                    out.print(confirmText);
                                }

                            } else {
                                response.sendRedirect("index.jsp");
                            }
                        %>
                    </form>
                </td>
            </tr>
            <tr>
                <td id="thirdRow" colspan="3">
                    <form id="frmDetails" name="frmDetails">
                        <input type="Submit" id="btnShowTrasactions" name="btnShowTrasactions" value="Show Transactions" onclick="openNewWin()"/>
                    </form>
                </td>
            </tr>
            <tr>
                <td id="fourthRow" colspan="3">
                    <form id="frmExit" name="frmExit">
                        <input type="Submit" id="btnExit" name="btnExit" value="Exit / Log Off"/>
                        <%
                            if (request.getParameter("btnExit") != null) {
                                session.setAttribute("authenticatedUser", null);
                                session.setAttribute("authenticatedBank", null);
                                session.setAttribute("listUserGroups", null);
                                response.sendRedirect("index.jsp");
                            }

                        %>
                    </form>
                </td>
            </tr>
        </table>
        <script>
            function openNewWin() {
                window.open("transactionDetailUI.jsp");
            }
        </script>
    </body>
</html>
