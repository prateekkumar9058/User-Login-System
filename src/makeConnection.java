import java.sql.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;

public class makeConnection {

    // connecting to database with java database connectivity
    public static Connection getConnection() {

        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/college";
            String user = "root";
            String pass = "123456";

            connection = DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
        }
        return connection;
    }

    // check username is already in database or not
    public static int userNameValidation(String username) {
        int result = 1;
        try {
            Connection connection = getConnection();

            String query = "SHOW TABLES";

            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet set = statement.executeQuery(query);
            ArrayList<String> list = new ArrayList<>();

            String user;
            while (set.next()) {
                user = set.getString("Tables_in_college");
                list.add(user);

                for (int i = 0; i < list.size(); i++) {
                    if (username.equals(list.get(i))) {
                        result = 0;
                    }
                }
            }
        } catch (Exception e) {
        }
        return result;
    }

    // check that is any uppercase letter in username is present or not
    public static int haveUpperCase(String username) {
        int result = 1;

        for (int i = 0; i < username.length(); i++) {
            if (Character.isUpperCase(username.charAt(i))) {
                result = 2;
            }
        }
        return result;
    }

    // check that is any digit in username is present or not
    public static int isHaveAnyDigit(String username) {
        int result = 1;

        for (int i = 0; i < username.length(); i++) {
            if (Character.isDigit(username.charAt(i))) {
                result = 3;
            }
        }
        return result;
    }

    // check whether the email field is empty or not
    public static int isEmpty(String name, String email, String username, String DOB, String pass, String Confpass) {
        int result = 0;
        if (name.length() == 0 || email.length() == 0 || username.length() == 0 || DOB.length() == 0
                || pass.length() == 0 || Confpass.length() == 0) {
            result = 1;
        }
        return result;
    }

    // check the username and password is correct or not
    public static int userLoginAuthentication(String username, String password) {
        int result = 1;

        try {

            Connection connection = getConnection();

            String query = "SELECT * FROM " + username + " ";

            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet set = statement.executeQuery(query);

            String userName;
            String Pass;
            while (set.next()) {
                userName = set.getString("username");
                Pass = set.getString("New_Password");
                if (userName.equals(username) && Pass.equals(password)) {
                    result = 0;
                }

            }

        } catch (Exception e) {
        }

        return result;
    }

    // check whether the email is present in database or not
    public static int emailValidation(String email) {
        int result = 1;

        try {

            Connection connection = getConnection();

            String query = "SELECT TABLE_NAME, COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME LIKE '%date_of_birth%'";

            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet set = statement.executeQuery(query);

            ArrayList<String> list1 = new ArrayList<>();

            String table;
            while (set.next()) {
                table = set.getString("TABLE_NAME");
                list1.add(table);

                for (int i = 0; i < list1.size(); i++) {
                    String query2 = "SELECT * FROM " + list1.get(i) + " ";

                    PreparedStatement statement2 = connection.prepareStatement(query2);

                    ResultSet set2 = statement2.executeQuery();

                    String Email = "";
                    while (set2.next()) {

                        Email = set2.getString("email");

                        if (Email.equals(email)) {
                            result = 0;
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
        }

        return result;
    }

    // creating your account and store all your details in database
    public static void signUp(String name, String email, String username, String DOB, String newPass, String confPass) {
        try {
            Connection connection = getConnection();

            String query = "CREATE TABLE " + username
                    + "(id INT PRIMARY KEY auto_increment, name VARCHAR(200), email VARCHAR(200), username VARCHAR(200), date_of_birth VARCHAR(100), new_password VARCHAR(200), confirm_pass VARCHAR(200))";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();

            String query2 = "INSERT INTO " + username
                    + "(name, email, username, date_of_birth, new_password, confirm_pass)VALUES(?, ?, ?, ?, ?, ?)";

            PreparedStatement statement2 = connection.prepareStatement(query2);

            statement2.setString(1, name);
            statement2.setString(2, email);
            statement2.setString(3, username);
            statement2.setString(4, DOB);
            statement2.setString(5, newPass);
            statement2.setString(6, confPass);

            statement2.executeUpdate();
        } catch (Exception e) {
        }
    }

    
    // check the password is weak or strong
    public static int checkPassword(String password) {
        int result = 1;

        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i)) && password.length() > 6) {
                result = 0;
            }
        }
        return result;
    }

    // check your new password and confirm password is matched or not
    public static int checkConfirmPassword(String confPass, String password) {
        int result = 0;

        if (password.equals(confPass)) {
            result = 1;
        }
        return result;
    }

    // check verification code via Email
    public static void SendingEmail(String email, int code) {
        // Email id of recipient
        String recipient = email;

        // Email id of sender
        String myEmail = "prateekt782@gmail.com";

        // using host as a local host
        String host = "smtp.gmail.com";

        // Getting system properties
        Properties properties = System.getProperties();

        // Setting up mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // creating session object to get properties
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("prateekt782@gmail.com", "umzbdabxsrjhitou");
            }
        });

        try {
            // MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set from field: adding sender email to from field
            message.setFrom(new InternetAddress(myEmail));

            // Set to field: adding recipient's email to from field.
            message.addRecipient(RecipientType.TO, new InternetAddress(recipient));

            // Set subject: subject of the email.
            message.setSubject("MakeMyTriip Verification Code");

            // Set body of the email
            message.setText("MakeMyTriip Dear User\n\nThanks for using MakeMyTriip\n\nHere is the verification code:\n\n" + code + "\n\nFor your security, please use the code in one hour.\n\n\nBest Regards\nMakeMyTriip Team");

            // Send email.
            Transport.send(message);
            System.out.println("Email sent successfully");
        } catch (MessagingException mex) {
        }

    }

    // check the email is valid or not
    public static int checkValidEmail(String email) {
        int result = 1;
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                result = 0;
            }
        }
        return result;
    }

    // check forgot field email is valid or not
    public static int checkForgotEmail(String email) {
        int result = 1;
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                result = 0;
            }
        }
        return result;
    }

    // generating random numbers
    public static int getRandomNumbers(){
        Random random = new Random();
        int randomNums = random.nextInt(100000, 900000);
        return randomNums;
    }

    // getting email from userName
    public static String getEmail(String username){
        String email = "";
        try {
            Connection connection = getConnection();

            String query = "SELECT * FROM "+username+" ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet set = preparedStatement.executeQuery();
    
            while(set.next()){
                email = set.getString("email");
            }   
        } catch (Exception e) {
        }
        return email;
    }

    // set your new password
    public static void changePassword(String email, String pass, String confPass){

        for (int i = 0; i<email.length(); i++){
            if (email.charAt(i)=='@'){
                try {
            
                    Connection connection = getConnection();
        
                    String query = "SELECT TABLE_NAME, COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME LIKE '%date_of_birth%'";
        
                    PreparedStatement statement = connection.prepareStatement(query);
        
                    ResultSet set = statement.executeQuery(query);
        
                    ArrayList<String> list1 = new ArrayList<>();
        
                    String username = "";
                    String table;
                    while (set.next()) {
                        table = set.getString("TABLE_NAME");
                        list1.add(table);
                        
                        for (int k = 0; k < list1.size(); k++) {
                            String query2 = "SELECT * FROM " + list1.get(k) + " ";
                            
                            PreparedStatement statement2 = connection.prepareStatement(query2);
                            
                            ResultSet set2 = statement2.executeQuery();
                            
                            String Email = "";
                            while (set2.next()) {
                                Email = set2.getString("email");
                                
                                if (Email.equals(email)) {
                                    username = set2.getString("username");
                                    System.out.println(username);
                                    break;
                                }
                            }
                        }
                    }
                    System.out.println(username);
                    String query2 = "UPDATE "+username+" SET new_password = '"+pass+"', confirm_pass = '"+confPass+"' WHERE username = '"+username+"' ";
                    PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                    preparedStatement2.executeUpdate();
                    
        
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            else{
                try {
                    Connection connection = getConnection();
                    String query2 = "UPDATE "+email+" SET new_password = '"+pass+"', confirm_pass = '"+confPass+"' WHERE username = '"+email+"' ";
                    PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                    preparedStatement2.executeUpdate();    
                } catch (Exception e) {
                }
                
            }
        }
    }

    public static void main(String[] args) {

    }

}
