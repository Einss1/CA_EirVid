/* Luan Navarro Silva - 2020344 */
package eirvid;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Registration {
    
    /* Function to verify if email is already in database */
    public boolean verifyDuplicate(String email) throws SQLException{
        
        PreparedStatement st;
        ResultSet rs;
        boolean duplicateEmail;
        
        String query = "SELECT * FROM users WHERE email = ?";
        
        st = My_CNX.getConnection().prepareStatement(query);
        st.setString(1, email);
        rs = st.executeQuery();
        
        duplicateEmail = rs.next();
        
        return duplicateEmail;
        
    }
    
    public void Registration() throws SQLException, NoSuchAlgorithmException {
        System.out.println("Welcome to the registration page!");
        Scanner scanner = new Scanner(System.in);
        
        /* Name input is validated throug regex */
        System.out.println("Please insert your name"); 
        String name = scanner.nextLine().trim();         
        while (!name.matches("^[a-zA-Z ]*$")) {            
            System.out.println("That is not a valid name! Please try again.");             
            name = scanner.nextLine().trim();            
        }
        
        /* Email input is validated through the verifyDuplicate function and regex */
        System.out.println("Please insert your email");
        String email = scanner.nextLine().trim();
        while(verifyDuplicate(email) == true) {
            System.out.println("The email is already registered. Please insert a different email");
            email = scanner.nextLine().trim();
        }
        while (!email.matches("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")) {
                System.out.println("That is not a valid email address! Please try again.");
                email = scanner.nextLine().trim();
        }
        
        /* Password input is validated throug regex */
        System.out.println("Please insert a password!");
        String password = scanner.nextLine().trim();
        while (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            System.out.println("That is not a valid password! A valid password contains: \n-At least 8 characters\n-At least one digit\n-At least one uppercase character and one lowercase character\n-At least one special character(@#$%¨, etc)\n-No space allowed\nPlease try again:");
            password = scanner.nextLine().trim();
        }
        
        /* MD5 hashing method */
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        password = bigInt.toString();
    
        /* Insert new user with hashed password into database */
        PreparedStatement st;
        
        String query = "INSERT INTO users(name, email, password) VALUES (?, ?, ?)";
        
        st = My_CNX.getConnection().prepareStatement(query);
        st.setString(1, name);
        st.setString(2, email);
        st.setString(3, password);
  
        if(st.executeUpdate() != 0) {
            System.out.println("Your account was created successfully!");
        }else{
            System.out.println("Something went wrong, please try again!");
        } 
    }
}