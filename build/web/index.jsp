
<%@page import="edu.pitt.bank.Customer"%>
<%@page import="edu.pitt.bank.Security"%>
<%@page import="edu.pitt.bank.Bank"%>
<%@page import="edu.pitt.utilities.ErrorLogger"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bank 1017 Login</title>
        <style>
            p.text{
                text-align: center;
            }
            
            table.main{
                margin-left:auto; 
                margin-right:auto;
            }
        </style>
    </head>
    <body>
        <form id="frmLogin" name="frmLogin" action="index.jsp" method="POST">
            <br/>
            <p class="text">Welcome to the Bank 1017 login page! Please provide your credentials to continue.
            <br/>
            <br/>
            <table class="main">
                <tr>
                    <td>Login Name:</td>
                    <td colspan="2"><input type="text" id="txtLogin" name="txtLogin" value="" /></td>
                </tr>
                <tr>
                    <td>PIN:</td>
                    <td colspan="2"><input type="password" id="txtPassword" name="txtPassword" value=""/></td>
                </tr>
                <tr>
                    <td></td>
                    <td align="right" colspan="3"><input type="submit" id="btnSubmit" name="btnSubmit" value="Login"/> </td>
                </tr>
            </table>
        </form>
        <%
            
            //These are some variables used in this section
            String txtLogin = request.getParameter("txtLogin");
            String txtPassword = request.getParameter("txtPassword");
            
            //Checking for empty and null values
            if (txtLogin != null && txtPassword != null) {
                if(!txtLogin.equals("") && !txtPassword.equals("")){
                    
                    //We'll try and parse the pin provided
                    int parsedPin = 0;
                    try {
                        parsedPin = Integer.parseInt(txtPassword);
                    } catch (Exception e) {
                        ErrorLogger.log("Could not parse the password. Incorrect input");
                        ErrorLogger.log(e.getMessage());
                        out.print("<p class=\"text\"><font color=\"red\">The password entered contains invalid characters. Please try again. </font>");
                    }
                    
                    // If the pin was parsed, we'll try to validate the login. If login is invalid, the customer will know.
                    Security s = new Security();
                    if (s.validateLogin(txtLogin, parsedPin) != null) {
                        Customer c = new Customer(s.validateLogin(txtLogin, parsedPin).getCustomerID());
                        out.print("<p>Please wait while we load the bank. This may take a few seconds...</p>");
                        Bank b = new Bank();
                        
                        //Passing some attributes to the next page and sending you there
                        session.setAttribute("authenticatedUser", c);
                        session.setAttribute("authenticatedBank", b);
                        session.setAttribute("listUserGroups", s.listUserGroups(c));
                        session.setMaxInactiveInterval(360);
                        response.sendRedirect("accountDetailsUI.jsp");
                        

                    } else {
                        out.print("<p class=\"text\"><font color=\"red\">The credentials you've provided are invalid. Please try again.</font>");
                        session.setAttribute("authenticatedUser", null);
                        session.setAttribute("authenticatedBank", null);
                        session.setAttribute("listUserGroups", s.listUserGroups(null));
                    }
                } else {
                   out.print("<p class=\"text\"><font color=\"red\">Seems you left something out...</font>");
                }
            }
        %>
    </body>
</html>
