/* Rueili Jhang 2020443*/
package eirvid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

class Recommended {
    public static void Recommended() throws SQLException {
        
        /* Get and store information about all films rented by users */
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
        
        /* Organize arrays using bubble sort */
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
        
        /* Declare the time now and 5 minutes before */
        long timeNow = Instant.now().toEpochMilli();
        long minutes5Before = timeNow - TimeUnit.MINUTES.toMillis(5);
       
        /* Filter out films rented past 5 minutes before current time */
        for (int p = 0; p < rentedTime.size() ;p++) {
            if (rentedTime.get(p) < minutes5Before) {
                movieRented.remove(p);
                rentedTime.remove(p);
                p--;
            }
        }
        
        Set<Integer> set = new HashSet<Integer>();
        for (int num : movieRented) {
            set.add(num);
        }
        
        Object[] recommendedMovies = set.toArray();
        
        System.out.println("Our most rented movies of the past 5 minutes are: " + Arrays.toString(recommendedMovies));
       
    }
}
