/* Rueili Jhang 2020443*/
package eirvid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class Recommended {
    public static String Recommended() throws SQLException {
    
        PreparedStatement st;
        ResultSet rs;
              
        String query = "SELECT * FROM users";
                            
        st = My_CNX.getConnection().prepareStatement(query);
        rs = st.executeQuery();
        
        List<Integer> movieRented=new ArrayList<>();
        List<Long> rentedTime=new ArrayList<>();
        while (rs.next()) {      
            movieRented.add(rs.getInt("movieRented"));
            String stringRentedTime = rs.getString("rentedTime").trim();
            long intRentedTime = Long.parseLong(stringRentedTime);
            rentedTime.add(intRentedTime);
        }
        
        int n = rentedTime.size();
        long temp = 0;
        int temp1 = 0;
        
        for (int i = 0; i < n; i++) {
            for (int j=1; j < (n-i); j++) {
                if (rentedTime.get(j-1) > rentedTime.get(j)) {
                    temp = rentedTime.get(j-1);
                    temp1 = movieRented.get(j-1);
                    rentedTime.set(j-1, rentedTime.get(j));
                    movieRented.set(j-1, movieRented.get(j));
                    rentedTime.set(j, temp);
                    movieRented.set(j, temp1);
                }
            }
        } 
        
        long timeNow = Instant.now().toEpochMilli();
        long minutes5Before = timeNow - TimeUnit.MINUTES.toMillis(5);
       
        /*
        for (int p = 0; p < rentedTime.size() ;p++) {
            if (rentedTime.get(p) < minutes5Before) {
                System.out.println("true");
                System.out.println(movieRented.get(p));
                System.out.println(rentedTime.get(p));
                movieRented.remove(p);
                rentedTime.remove(p);
            } else {
                System.out.println("false");
            }
        } 
        
        System.out.println(minutes5Before);
        System.out.println(movieRented);
        System.out.println(rentedTime); */
        
        String stringRecommended = "The recommended is...";
        return stringRecommended;
    }
}
