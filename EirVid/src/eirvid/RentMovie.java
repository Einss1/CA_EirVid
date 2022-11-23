/*Jing Li 2019301 */
package eirvid;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

class RentMovie {
    public static void RentMovie(int id, int movieId){
        long initialTime = Instant.now().toEpochMilli();
        long endTime = initialTime + TimeUnit.MINUTES.toMillis(1);
        
        LocalDateTime initialTimeFormatted = Instant.ofEpochMilli(initialTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endTimeFormatted = Instant.ofEpochMilli(endTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
        
        String stringUserId = Integer.toString(id);
        String stringMovieId = Integer.toString(movieId);
        String stringInitialTime = Long.toString(initialTime);
        String stringEndTime = Long.toString(endTime);
        
        //Insert movie rented and time it was rented onto database
        try {
            PreparedStatement st;
            
            String query = "UPDATE users SET movieRented = ?, rentedTime = ?, returnTime = ? WHERE id = ?";
            
            st = My_CNX.getConnection().prepareStatement(query);
            
            st.setString(1, stringMovieId);
            st.setString(2, stringInitialTime);
            st.setString(3, stringEndTime);
            st.setString(4, stringUserId);
        
            if(st.executeUpdate() != 0) {
                System.out.println("Successful");
            }else{
                System.out.println("Something went wrong, please try again!");
            } 
        } catch (SQLException err) {
            System.out.println("Something went wrong while adding rent to database!");
        }
       
    }
}
