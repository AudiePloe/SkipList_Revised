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

        int num = 0; // used as storage for user input

        do // loops asking the user what they want to do
        {
            System.out.println("\nWelcome to the user database.");
            System.out.println("1) Look up a user");
            System.out.println("2) List all users by job");
            System.out.println("3) List all users by workDays");
            System.out.println("4) List all users by floor");
            System.out.println("5) List all users by department");
            System.out.println("6) To exit");

            num = reader.nextInt();
            reader.nextLine();

            if(num == 1)
            {
                mainSearchRoutine(reader, num);
            }
            else if (num != 6 && !DataManager.getCustomLists().isEmpty())
            {
                int listNum;
                System.out.println("Would you like to search inside:");
                System.out.println("1) Main List");

                for (int i = 0; i < DataManager.getCustomLists().size(); i++)
                {
                    System.out.printf("%d) Custom %d\n", (i + 2), DataManager.getCustomLists().size());
                }
                listNum = reader.nextInt();
                reader.nextLine();

                if (listNum == 1)
                {
                    mainSearchRoutine(reader, num);
                }
                else if (listNum >= 2 || listNum <= DataManager.getCustomLists().size() + 1)
                {
                    customSearchRoutine(reader, num, listNum - 2);
                }
            }
            else
            {
                mainSearchRoutine(reader, num);
            }
        } while (num != 6); // loop until user enters 3 to close

        reader.close();
    }

    private static void customSearchRoutine(Scanner reader, int num, int listNum)
    {
        ArrayList<User> tmpList = DataManager.getCustomLists().get(listNum);
        String tmpString;

        if (num == 1) // If looking up user
        {
            do
            {
                System.out.print("\n\nPlease enter the SSN of the user you are looking for: ");

                tmpString = reader.next();

            } while (!isNumeric(tmpString));

            User foundUser = null;

            for (int i = 0; i < tmpList.size(); i++)
            {
                if (tmpString.compareTo(tmpList.get(i).getSSN()) == 0)
                {
                    foundUser = tmpList.get(i);
                    break;
                }
            }

            if (foundUser != null)
            {
                String userData = foundUser.toString();

                System.out.println(userData);
            }
            else
            {
                // Handle the case where the user was not found
                System.out.println("User with SSN " + tmpString + " not found.");
            }
        }
        else if (num == 2) // or if list all users by job type:
        {
            System.out.println("\nSelect your first job:");
            String job1 = getSelection(reader, DataManager.getJobs()); // getJobSelection and availableJobs must be accessible

            if (job1 != null)
            {
                System.out.print("\nDo you want to add a second job to the search? (y/n): ");
                String secondJobChoice = reader.nextLine().trim().toLowerCase();
                String job2 = null;

                if (secondJobChoice.equals("y"))
                {
                    System.out.println("\nSelect your second job:");
                    job2 = getSelection(reader, DataManager.getJobs());
                }

                ArrayList<User> foundUsers = new ArrayList<>();

                for (User user : tmpList)
                {
                    if ((user.getJob1().equals(job1) || user.getJob2().equals(job1)) && job2 == null || user.getJob1().equals(job1) && user.getJob2().equals(job2)
                            || user.getJob1().equals(job2) && user.getJob2().equals(job1))
                    {
                        foundUsers.add(user);
                        System.out.println(user.toString());
                    }
                }

                if (!foundUsers.isEmpty())
                {
                    promptAddCustomList(reader, foundUsers);
                }
                else
                {
                    System.out.println("No users found...");
                }
            }
            else
            {
                System.out.println("Search cancelled.");
            }
        }
        else if (num == 3)
        {
            System.out.println("\nSelect your first workdays:");
            String workDay1 = getSelection(reader, DataManager.getWorkDays()); // getJobSelection and availableJobs must be accessible

            if (workDay1 != null)
            {
                System.out.print("\nDo you want to add a second workdays to the search? (y/n): ");
                String secondDayChoice = reader.nextLine().trim().toLowerCase();
                String workDay2 = null;

                if (secondDayChoice.equals("y"))
                {
                    System.out.println("\nSelect your second workdays:");
                    workDay2 = getSelection(reader, DataManager.getWorkDays());
                }
                ArrayList<User> foundUsers = new ArrayList<>();

                for (User user : tmpList)
                {
                    if ((user.getWorkDay1().equals(workDay1) || user.getWorkDay2().equals(workDay1)) && workDay2 == null || user.getWorkDay1().equals(workDay1) && user.getWorkDay2().equals(workDay2)
                            || user.getWorkDay1().equals(workDay2) && user.getWorkDay2().equals(workDay1))
                    {
                        foundUsers.add(user);
                        System.out.println(user.toString());
                    }
                }

                if (!foundUsers.isEmpty())
                {
                    promptAddCustomList(reader, foundUsers);
                }
                else
                {
                    System.out.println("No users found...");
                }
            }
            else
            {
                System.out.println("Search cancelled.");
            }

        }
        else if (num == 4)
        {
            System.out.println("\nSelect your first floor:");
            String floor1 = getSelection(reader, DataManager.getFloors()); // getJobSelection and availableJobs must be accessible

            if (floor1 != null)
            {
                System.out.print("\nDo you want to add a second floor to the search? (y/n): ");
                String secondFloorChoice = reader.nextLine().trim().toLowerCase();
                String floor2 = null;

                if (secondFloorChoice.equals("y"))
                {
                    System.out.println("\nSelect your second floor:");
                    floor2 = getSelection(reader, DataManager.getFloors());
                }

                ArrayList<User> foundUsers = new ArrayList<>();

                for (User user : tmpList)
                {
                    if ((user.getFloor1().equals(floor1) || user.getFloor2().equals(floor1)) && floor2 == null || (user.getFloor1().equals(floor1) && user.getFloor2().equals(floor2)
                            || user.getFloor1().equals(floor2) && user.getFloor2().equals(floor1)))
                    {
                        foundUsers.add(user);
                        System.out.println(user);
                    }
                }

                if (!foundUsers.isEmpty())
                {
                    promptAddCustomList(reader, foundUsers);
                }
                else
                {
                    System.out.println("No users found...");
                }
            }
            else
            {
                System.out.println("Search cancelled.");
            }
        }
        else if (num == 5)
        {
            System.out.println("\nSelect your department:");
            String department = getSelection(reader, DataManager.getDepartments()); // getJobSelection and availableJobs must be accessible

            if (department != null)
            {

                ArrayList<User> foundUsers = new ArrayList<>();

                for (User user : tmpList)
                {
                    if (user.getDepartment().equals(department))
                    {
                        foundUsers.add(user);
                        System.out.println(user);
                    }
                }

                if (!foundUsers.isEmpty())
                {
                    promptAddCustomList(reader, foundUsers);
                }
                else
                {
                    System.out.println("No users found...");
                }
            }
            else
            {
                System.out.println("Search cancelled.");
            }
        }
    }

    private static void mainSearchRoutine(Scanner reader, int num)
    {
        String tmpString;
        if (num == 1) // If looking up user
        {
            do
            {
                System.out.print("\n\nPlease enter the SSN of the user you are looking for: ");

                tmpString = reader.next();

            } while (!isNumeric(tmpString));

            User tmpUser = new User();
            tmpUser.setSSN(tmpString);
            UserBySSN searchWrapper = new UserBySSN(tmpUser);

            Node<UserBySSN> foundNode = dataManager.ssnList.logSearch(searchWrapper);

            if (foundNode != null)
            {
                User foundUser = foundNode.data.user;
                String userData = foundUser.toString();

                System.out.println(userData);
            }
            else
            {
                // Handle the case where the user was not found
                System.out.println("User with SSN " + searchWrapper.user.getSSN() + " not found.");
            }
        }
        else if (num == 2) // or if list all users by job type:
        {
            System.out.println("\nSelect your first job:");
            String job1 = getSelection(reader, DataManager.getJobs()); // getJobSelection and availableJobs must be accessible

            if (job1 != null)
            {
                System.out.print("\nDo you want to add a second job to the search? (y/n): ");
                String secondJobChoice = reader.nextLine().trim().toLowerCase();
                String job2 = null;

                if (secondJobChoice.equals("y"))
                {
                    System.out.println("\nSelect your second job:");
                    job2 = getSelection(reader, DataManager.getJobs());
                }

                // Call the search method with the collected jobs
                promptAddCustomList(reader, dataManager.findAndPrintUsersByJobs(job1, job2));
            }
            else
            {
                System.out.println("Search cancelled.");
            }
        }
        else if (num == 3)
        {
            System.out.println("\nSelect your first workdays:");
            String workDay1 = getSelection(reader, DataManager.getWorkDays()); // getJobSelection and availableJobs must be accessible

            if (workDay1 != null)
            {
                System.out.print("\nDo you want to add a second workdays to the search? (y/n): ");
                String secondDayChoice = reader.nextLine().trim().toLowerCase();
                String workDay2 = null;

                if (secondDayChoice.equals("y"))
                {
                    System.out.println("\nSelect your second workdays:");
                    workDay2 = getSelection(reader, DataManager.getWorkDays());
                }
                // Call the search method with the collected jobs
                promptAddCustomList(reader, dataManager.findAndPrintUsersByWorkday(workDay1, workDay2));
            }
            else
            {
                System.out.println("Search cancelled.");
            }

        }
        else if (num == 4)
        {
            System.out.println("\nSelect your first floor:");
            String floor1 = getSelection(reader, DataManager.getFloors()); // getJobSelection and availableJobs must be accessible

            if (floor1 != null)
            {
                System.out.print("\nDo you want to add a second floor to the search? (y/n): ");
                String secondFloorChoice = reader.nextLine().trim().toLowerCase();
                String floor2 = null;

                if (secondFloorChoice.equals("y"))
                {
                    System.out.println("\nSelect your second floor:");
                    floor2 = getSelection(reader, DataManager.getFloors());
                }
                // Call the search method with the collected jobs
                promptAddCustomList(reader, dataManager.findAndPrintUsersByFloor(floor1, floor2));
            }
            else
            {
                System.out.println("Search cancelled.");
            }
        }
        else if (num == 5)
        {
            System.out.println("\nSelect your department:");
            String department = getSelection(reader, DataManager.getDepartments()); // getJobSelection and availableJobs must be accessible

            if (department != null)
            {
                // Call the search method with the collected jobs
                promptAddCustomList(reader, dataManager.findAndPrintUsersByDepartment(department));
            }
            else
            {
                System.out.println("Search cancelled.");
            }
        }
    }

        private static void promptAddCustomList (Scanner reader, ArrayList < User > list)
        {
            String tmpString;
            System.out.println("Number of matching users found: " + list.size());
            System.out.println("Would you like to save this list? (y/n)");
            tmpString = reader.nextLine();
            if (tmpString.equals("y"))
            {
                DataManager.addToCustomLists(list);
            }
        }

        private static String getSelection (Scanner scanner, ArrayList < String > list)
        {
            // Print the available jobs
            for (int i = 0; i < list.size(); i++)
            {
                String job = list.get(i);
                // Removed the check that prevents a user from seeing an already selected job.
                System.out.println((i + 1) + ". " + job);
            }

            while (true)
            {
                System.out.print("Enter the number for your selection: ");
                try
                {
                    int selection = Integer.parseInt(scanner.nextLine());
                    if (selection > 0 && selection <= list.size())
                    {
                        // Removed the check that prevents a user from selecting an already selected job.
                        return list.get(selection - 1);
                    }
                    else
                    {
                        System.out.println("Invalid selection. Please enter a number between 1 and " + list.size() + ".");
                    }
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
        }

        public static boolean isNumeric (String str) // checks to see if ssn is a number or not
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