/* Luan Navarro Silva - 2020344 */
package eirvid;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;

public class EirVid {

    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException, IOException {
        System.out.println("\nWelcome to ÉirVid!\nPlease select an option:");
        int input;

        /* Switch menu */
        do {
            System.out.println("1 - Login \n2 - Register \n0 - Exit");
            Scanner scanner = new Scanner(System.in);
            while (!scanner.hasNextInt()) {
                System.out.println("Input is not a number! Please pick a number!");
                scanner.nextLine();
            }
            input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {

                // Login
                case 1:
                    Login lo = new Login();
                    lo.Login();
                    break;

                // Registration
                case 2:
                    Registration re = new Registration();
                    re.Registration();
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
