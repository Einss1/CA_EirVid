/* Luan Navarro Silva - 2020344 */
package eirvid;

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
        /* Collect user account information */
        System.out.println("Please insert your email address!");
        Scanner scanner = new Scanner(System.in);
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
    }
}
