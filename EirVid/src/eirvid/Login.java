/* Luan Navarro Silva - 2020344 */
package eirvid;

import static eirvid.EirVid.main;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {
    public void Login() throws SQLException, NoSuchAlgorithmException, IOException {
        System.out.println("\nWelcome to the login page!");
        Scanner scanner = new Scanner(System.in);
        
        int input;
        
        /* Switch menu */
        do {
            System.out.println("\n1 - Login \n0 - Main Menu");
            
            while(!scanner.hasNextInt()){
                System.out.println("Input is not a number! Please pick a number!");
                scanner.nextLine();
            }
            
            input = scanner.nextInt();
            scanner.nextLine();
            
            switch (input){ 
                case 1:
                    /* Collect user account information */
                    System.out.println("Please insert your email address!");
                    String email = scanner.nextLine().trim();
                    System.out.println("Please insert your password!");
                    String password = scanner.nextLine().trim();
        
                    /* Create a MD5 hash to compare with the one stored in database and grant access */
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] messageDigest = md.digest(password.getBytes());
                    BigInteger bigInt = new BigInteger(1, messageDigest);
                    password = bigInt.toString();
        
                    PreparedStatement st;
                    ResultSet rs;
                    
                    try {
                        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
                            
                        st = My_CNX.getConnection().prepareStatement(query);
                
                        st.setString(1, email);
                        st.setString(2, password);
                        rs = st.executeQuery();
                
                        if(rs.next()){
                            int id = rs.getInt("id");
                            String name = rs.getString("name");
                            if ( id == 1) {
                                AdminPage ap = new AdminPage();
                                ap.AdminPage(id);
                            }
                            MainMenu mm = new MainMenu();
                            mm.MainMenu(id, name);
                        }
                        else {
                            System.out.println("Password incorrect or account not found!");
                        }
                    } catch (SQLException err) {
                        System.out.println("Something went wrong while fetching data from database!");
                    }
                    break;
                
                case 0:
                    main(null); 
                    break;
                
                default:
                    System.out.println("That is not a valid option! Please pick again!");
                    break;
            }
        } while (input != 0);
        
    }
}
