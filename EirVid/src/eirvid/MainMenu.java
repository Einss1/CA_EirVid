/* Chia Hua Lin 2020044 */
package eirvid;

import static eirvid.EirVid.main;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Scanner;

public class MainMenu {

    public void MainMenu(int id, String name) throws FileNotFoundException, IOException, SQLException, NoSuchAlgorithmException {

        System.out.println("Please select an option:");
        int input;

        /* Switch menu */
        do {
            System.out.println("\n1 - Rent movie \n2 - Recommended \n9 - Logout \n0 - Exit");
            Scanner scanner = new Scanner(System.in);
            while (!scanner.hasNextInt()) {
                System.out.println("Input is not a number! Please pick a number!");
                scanner.nextLine();
            }
            input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {

                /* Rent movie option*/
                case 1:
                    long timeNow = Instant.now().toEpochMilli();
                    boolean canRent = true;

                    /* Get information from database to check if user has a movie to be returned, if yes, the user can't rent another */
                    try {
                        PreparedStatement st;
                        ResultSet rs;

                        String query = "SELECT * FROM users WHERE id = ?";
                        st = My_CNX.getConnection().prepareStatement(query);

                        String stringId = Integer.toString(id);
                        st.setString(1, stringId);

                        rs = st.executeQuery();

                        while (rs.next()) {
                            long returnTime = rs.getLong("returnTime");
                            if (returnTime > timeNow) {
                                canRent = false;
                                break;
                            }
                        }

                    } catch (SQLException err) {
                        System.out.println("Something went wrong while fetching information from users database");
                    }

                    /* Get all movies from the database and display to the user */
                    if (canRent != false) {
                        try {
                            PreparedStatement st;
                            ResultSet rs;

                            String query = "SELECT * FROM movies";

                            st = My_CNX.getConnection().prepareStatement(query);

                            rs = st.executeQuery();
                            System.out.println("ID" + "\t" + "MOVIE" + "\t\t" + "PRICE\n");
                            while (rs.next()) {
                                int movieId = rs.getInt("id");
                                String movie = rs.getString("movie");
                                int price = rs.getInt("price");
                                System.out.println(movieId + "\t" + movie + "\t" + price + "€");
                            }

                        } catch (SQLException err) {
                            System.out.println("Something went wrong while fetching movies from database. Please try again!");
                        }

                        /* Rent the movie picked by user */
                        System.out.println("\nPick a movie from the list to rent!");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Input is not a number! Please pick a number!");
                            scanner.nextLine();
                        }
                        input = scanner.nextInt();
                        scanner.nextLine();
                        try {
                            PreparedStatement st;
                            ResultSet rs;

                            String query = "SELECT * FROM movies WHERE id = ?";

                            st = My_CNX.getConnection().prepareStatement(query);

                            String stringInput = Integer.toString(input);
                            st.setString(1, stringInput);
                            rs = st.executeQuery();

                            if (rs.next()) {
                                int movieId = rs.getInt("id");
                                String movie = rs.getString("movie");
                                int price = rs.getInt("price");
                                RentMovie.RentMovie(name, id, movieId, movie, price);
                            } else {
                                System.out.println("Something went wrong while fetching movie from database. Please try again!");
                            }
                        } catch (SQLException err) {
                            System.out.println("Something went wrong while renting a movie. Please try again!");
                        }
                    } else {
                        System.out.println("Sorry, you can't rent at the moment");
                    }
                    break;

                /* Recommended movies option */
                case 2:
                    Recommended.Recommended();
                    break;

                /* Logout option */
                case 9:
                    main(null);
                    break;

                /* Exit option */
                case 0:
                    System.out.println("Thank you for using ÉirVid!");
                    System.exit(0);

                default:
                    System.out.println("That is not a valid option! Please pick again!");
                    break;
            }
        } while (input != 0);
    }
}
