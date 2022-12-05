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
        List<Integer> movieRented=new ArrayList<>();
        List<Long> rentedTime=new ArrayList<>();
        
        try {
            
            PreparedStatement st;
            ResultSet rs;
              
            String query = "SELECT * FROM users";
                            
            st = My_CNX.getConnection().prepareStatement(query);
            rs = st.executeQuery();
        
            while (rs.next()) {      
                movieRented.add(rs.getInt("movieRented"));
                String stringRentedTime = rs.getString("rentedTime").trim();
                long intRentedTime = Long.parseLong(stringRentedTime);
                rentedTime.add(intRentedTime);
            }
            
        } catch (SQLException err) {
            System.out.println("Something went wrong while fetching from database");
        }
        
        /* Organize arrays indexes using bubble sort */
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
        for (int i = 0; i < rentedTime.size() ;i++) {
            if (rentedTime.get(i) < minutes5Before) {
                movieRented.remove(i);
                rentedTime.remove(i);
                i--;
            }
        }
        
        /* Transform long ArrayList into set */
        Set<Integer> set = new HashSet<Integer>();
        for (int num : movieRented) {
            set.add(num);
        }
        
        /* Transform set into Integer array */
        Integer[] recommendedMovies = new Integer[set.size()];
        int b = 0;
        for (Integer i: set) {
            recommendedMovies[b++] = i;
        }
        
        /* Declare a String array with 5 slots for the 5 most rented films of the past 5 minutes */
        String[] movieTitle = new String[5];
        
        /* Get the movie names based on their id number */
        try {
            PreparedStatement st;
            ResultSet rs;
            
            for (int i = 0; i < recommendedMovies.length; i++) {
                String query2 = "SELECT * FROM movies WHERE id = ?";
                
                st = My_CNX.getConnection().prepareStatement(query2);
                st.setString(1, Integer.toString(recommendedMovies[i]));
                rs = st.executeQuery();
                
                if(rs.next()){
                            String movie = rs.getString("movie");
                            movieTitle[i] = movie;
                }
            }
                        
        } catch (SQLException err) {
            System.out.println("Something went wrong while fetching from database");
        }
        
        /* Clear out null and empty values from movieTitle array */ 
        /* Code from: https://makeinjava.com/remove-null-empty-string-array-lambda-stream-java8-example/ */
        String[] removedNull = Arrays.stream(movieTitle).filter(value -> value != null && value.length() > 0).toArray(size -> new String[size]);
        
        System.out.println("Our most rented movies of the past 5 minutes are: " + Arrays.toString(removedNull));
       
    }
}
