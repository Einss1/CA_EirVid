/*Jing Li 2019301 */
package eirvid;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

class RentMovie {

    public static void RentMovie(String name, int id, int movieId, String movie, int price) {

        // Gets the time now and creates the variable endTime which adds one minute(rent time of a movie) to the initial time value
        long initialTime = Instant.now().toEpochMilli();
        long endTime = initialTime + TimeUnit.MINUTES.toMillis(1);

        // Converts the previous EPOCH datetime into human readable date
        Date dateInitial = new Date(initialTime);
        Date dateEnd = new Date(endTime);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String initialTimeFormatted = format.format(dateInitial);
        String endTimeFormatted = format.format(dateEnd);

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

            if (st.executeUpdate() != 0) {
            } else {
                System.out.println("Something went wrong, please try again!");
            }
        } catch (SQLException err) {
            System.out.println("Something went wrong while adding rent to database!");
        }

        System.out.println(name + ", you rented " + movie + " for " + price + " €");
        System.out.println("You have rented the movie at: " + initialTimeFormatted);
        System.out.println("It will be available until: " + endTimeFormatted);

    }
}
