/* Chia Hua Lin 2020044 */
package eirvid;

import static eirvid.EirVid.main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminPage {
    public void AdminPage(int id) throws SQLException, NoSuchAlgorithmException, IOException {
        int input;
        
        /* Switch menu */
        do {
            System.out.println("1 - Update database \n9 - Logout \n0 - Exit");
            Scanner scanner = new Scanner(System.in);
            while(!scanner.hasNextInt()){
                System.out.println("Input is not a number! Please pick a number!");
                scanner.nextLine();
            }
            input = scanner.nextInt();
            scanner.nextLine();
            switch (input){
                
                // Update database
                case 1:
                    PreparedStatement st;
                    ResultSet rs;
                    
                    //Delete previous database if exists.
                    try {
                        st = My_CNX.getConnection().prepareStatement("DROP TABLE IF EXISTS  movies");
                        st.executeUpdate();
                        System.out.println("Previous database deleted successfully!");
                    } catch(SQLException err) {
                        System.out.println("Something went wrong while deleting previous database. Please try again!");
                    }
                    
                    //Create a new empty database.
                    try {
                        st = My_CNX.getConnection().prepareStatement("CREATE TABLE  movies (\n" +
                            "  `id` smallint(255) NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  `movie` varchar(255) NOT NULL,\n" +
                            "  `price` smallint(255) NOT NULL\n" +
                        ")" );
                        st.executeUpdate();
                        System.out.println("New database created successfully!");
                    } catch (SQLException err) {
                        System.out.println("Something went wrong while creating a new database. Please try again!");
                    }

                    String line = "";
                    String splitBy = ",";
                    int i = 0;             
                    int[] movieId = new int[50000];
                    String[] movieTitle = new String[50000];
                    int[] moviePrice = new int[50000];
                    
                    //Insert into new database movies and its price.
                    try {
                        BufferedReader br = new BufferedReader(new FileReader("src/eirvid/movies.csv"));
                        while ((line = br.readLine()) != null) {
                            String[] movie = line.split(splitBy);
                            movieId[i] = i;
                            movieTitle[i] = movie[0];
                            String numberOnly= movie[1].replaceAll("[^0-9]", "");
                            int price = Integer.parseInt(numberOnly);
                            moviePrice[i] = price;
        
                            String queryInsert = "INSERT INTO movies(movie, price) VALUES (?, ?)";
        
                            st = My_CNX.getConnection().prepareStatement(queryInsert);
                            st.setString(1, movieTitle[i]);
                            String stringPrice = Integer.toString(moviePrice[i]);
                            st.setString(2, stringPrice);
                            i++;
                            if(st.executeUpdate() != 0) {
                                System.out.println("Movie " + movieTitle[i-1] + " was inserted into database successfuly!");
                            }else{
                                System.out.println("Something went wrong, please try again!");
                            } 
                        }
                    }
                    catch(IOException e) {
                        System.out.println("Something went wrong while inserting data into database!");
                    }
                    break;
                    
                //Logout
                case 9:
                    main(null);  
                    break;

                // Exit
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
