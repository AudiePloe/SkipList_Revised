//
// Audie Ploe
// 10/02/2021 -- Revised 09/02/2025
//

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchList
{
    public static DataManager dataManager = new DataManager();


    public static void main(String[] a) throws IOException
    {

        long timeS = System.currentTimeMillis(); // takes starting time in milliseconds
        long timeE = 0;

        System.out.println("LOADING-DATABASE----------------------------------");

        dataManager.createAllLists();

        timeE = System.currentTimeMillis(); // takes ending time in milliseconds

        System.out.print("\n\nData loaded in " + ((timeE - timeS) / 1000) + " seconds.\n\n"); // print out how many seconds it took

        menu(); // the user input part

        System.out.println("\nClosed");

    }

    static void menu() throws FileNotFoundException // used for getting user input.
    {
        Scanner reader = new Scanner(System.in);

        String tmpString = ""; // used as storage for user input
        int num = 0; // used as storage for user input

        do // loops asking the user what they want to do
        {
            System.out.println("\nWelcome to the user database. To look up a user type 1. To list all users by job type 2. To exit type 3.");

            num = reader.nextInt();
            reader.nextLine();

            if (num == 1) // If looking up user
            {
                do
                {
                    System.out.print("\n\nPlease enter the SSN of the user you are looking for: ");

                    tmpString = reader.next();

                } while (!isNumeric(tmpString));

                User tmpUser = new User();
                tmpUser.SSN = tmpString;
                UserBySSN searchWrapper = new UserBySSN(tmpUser);

                Node<UserBySSN> foundNode = dataManager.ssnList.logSearch(searchWrapper);

                if (foundNode != null) {
                    User foundUser = foundNode.data.user;
                    String userData = foundUser.toString();

                    System.out.println(userData);
                } else {
                    // Handle the case where the user was not found
                    System.out.println("User with SSN " + searchWrapper.user.SSN + " not found.");
                }
            }


            else if (num == 2) // or if list all users by job type:
            {
                System.out.println("\nSelect your first job:");
                String job1 = getJobSelection(reader); // getJobSelection and availableJobs must be accessible

                if (job1 != null) {
                    System.out.print("\nDo you want to add a second job to the search? (y/n): ");
                    String secondJobChoice = reader.nextLine().trim().toLowerCase();
                    String job2 = null;

                    if (secondJobChoice.equals("y")) {
                        System.out.println("\nSelect your second job:");
                        job2 = getJobSelection(reader);
                    }

                    // Call the search method with the collected jobs
                    dataManager.findAndPrintUsersByJobs(job1, job2);
                } else {
                    System.out.println("Search cancelled.");
                }
            }


        } while (num != 3); // loop until user enters 3 to close


        reader.close();

    }

    private static String getJobSelection(Scanner scanner)
    {
        ArrayList<String> availableJobs = DataManager.jobs;
        // Print the available jobs
        for (int i = 0; i < availableJobs.size(); i++) {
            String job = availableJobs.get(i);
            // Removed the check that prevents a user from seeing an already selected job.
            System.out.println((i + 1) + ". " + job);
        }

        while (true) {
            System.out.print("Enter the number for your job selection: ");
            try {
                int selection = Integer.parseInt(scanner.nextLine());
                if (selection > 0 && selection <= availableJobs.size()) {
                    String selectedJob = availableJobs.get(selection - 1);
                    // Removed the check that prevents a user from selecting an already selected job.
                    return selectedJob;
                } else {
                    System.out.println("Invalid selection. Please enter a number between 1 and " + availableJobs.size() + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public static boolean isNumeric(String str) // checks to see if ssn is a number or not
    {
        try
        {
            Double.parseDouble(str); // if it can parse to a double then true
            return true;
        }

        catch (NumberFormatException e)
        {
            return false;
        }

    }
}