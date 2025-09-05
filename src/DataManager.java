import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * Manages the SkipLists of the program.
 * As well as the arraylists of jobs, workdays, and floors.
 */
public class DataManager
{

    private static Random randomNum = new Random(System.currentTimeMillis());

    private static User rat;
    private static String ratJob;
    private static String ratWorkDay;
    private static String ratFloor;
    private static String ratDepartment;


    static SkipList ssnList = new SkipList(); // the skip list that holds all the nodes
    static SkipList jobList = new SkipList();
    static SkipList workdayList = new SkipList();
    static SkipList floorList = new SkipList();
    static SkipList departmentList = new SkipList();


    private static ArrayList<String> jobs = new ArrayList<>();
    private static ArrayList<String> workDays = new ArrayList<>();
    private static ArrayList<String> floors = new ArrayList<>();
    private static ArrayList<String> departments = new ArrayList<>();

    // A counter to keep track of the current item for balanced distribution
    private static int jobCounter = 0;
    private static int workdayCounter = 0;
    private static int floorCounter = 0;
    private static int departmentCounter = 0;

    private static ArrayList<ArrayList<User>> customLists = new ArrayList<ArrayList<User>>();


    public static User getRat()
    {
        return rat;
    }

    public static String printRat()
    {
        String ratString = "THE RAT...";
        ratString += ("\nJob1: " + rat.getJob1() + " " + rat.getWorkDay1() + " " + rat.getFloor1());
        ratString += ("\nJob2: " + rat.getJob2() + " " + rat.getWorkDay2() + " " + rat.getFloor2());
        ratString += ("\nJob3: " + ratJob + " " + ratWorkDay + " " + ratFloor);
        ratString += ("\nDepartment1: " + rat.getDepartment() + "\nDepartment2: " + ratDepartment);
        return (ratString);
    }

    public static ArrayList<String> getJobs()
    {
        return jobs;
    }

    public static ArrayList<String> getWorkDays()
    {
        return workDays;
    }

    public static ArrayList<String> getFloors()
    {
        return floors;
    }

    public static ArrayList<String> getDepartments()
    {
        return departments;
    }

    public void createAllLists() throws IOException
    {
        loadAllCategories();

        readFromFile(new File("Users.txt")); // creates the list
    }

    // Method to read a file into an ArrayList
    public static void readCategoriesFromFile(String fileName, ArrayList<String> list)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                list.add(line.trim());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Method to get the next item in a balanced, cycling order
    public static String getNextJob()
    {
        return jobs.get(jobCounter++ % jobs.size());
    }

    public static String getRandJob()
    {
        return jobs.get((int) (randomNum.nextFloat() * jobs.size()));
    }

    public static String getNextWorkday()
    {
        return workDays.get(workdayCounter++ % workDays.size());
    }

    public static String getRandWorkDay()
    {
        return workDays.get((int) (Math.random() * workDays.size()));
    }

    public static String getNextFloor()
    {
        return floors.get(floorCounter++ % floors.size());
    }

    public static String getRandFloor()
    {
        return floors.get((int) (Math.random() * floors.size()));
    }

    public static String getNextDepartment()
    {
        return departments.get(departmentCounter++ % departments.size());
    }
    public static String getRandDepartment()
    {
        return departments.get((int)(Math.random() * departments.size()));
    }

    // Call this method before starting your main user-reading loop
    public static void loadAllCategories()
    {
        readCategoriesFromFile("jobs.txt", jobs);
        readCategoriesFromFile("workdays.txt", workDays);
        readCategoriesFromFile("floors.txt", floors);
        readCategoriesFromFile("departments.txt", departments);

        jobCounter = (int) (Math.random() * 10);
        workdayCounter = (int) (Math.random() * 10);
        floorCounter = (int) (Math.random() * 10);
        departmentCounter = (int) (Math.random() * 10);
    }


    long countLines(String fileName) // counts the number of lines in the file (used for reading from file) derived
    // from Mkyong.com source
    {
        long lines = 0;

        try (InputStream is = new BufferedInputStream(new FileInputStream(fileName)))
        {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean endsWithoutNewLine = false;

            while ((readChars = is.read(c)) != -1)
            {
                for (int i = 0; i < readChars; ++i)
                {
                    if (c[i] == '\n')
                        ++count;
                }
                endsWithoutNewLine = (c[readChars - 1] != '\n');
            }
            if (endsWithoutNewLine)
            {
                ++count;
            }
            lines = count;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return lines;
    }

    @SuppressWarnings("unchecked")
    public void readFromFile(File file) throws IOException
    {

        RandomAccessFile database = new RandomAccessFile(file, "rw");
        String line = "";
        long lineCount = countLines(file.getName());

        System.out.print("\n");

        int ratNum = (int)(Math.random() * lineCount);

        for (int i = 0; i < lineCount; i++)
        {
            try
            {
                line = database.readLine();
                User newUser = new User();
                newUser = newUser.getUserFromTxt(line);

                // Assign two jobs, two workdays, and two floors
                // Use i * 2 to ensure a different item for the second assignment
                newUser.setJob1(getRandJob());
                newUser.setJob2(getRandJob());

                newUser.setWorkDay1(getRandWorkDay());
                newUser.setWorkDay2(getRandWorkDay());

                newUser.setFloor1(getRandFloor());
                newUser.setFloor2(getRandFloor());

                newUser.setDepartment(getRandDepartment());


                ssnList.add(new UserBySSN(newUser));

                jobList.add(new UserJobWrapper(newUser.getJob1(), newUser));
                jobList.add(new UserJobWrapper(newUser.getJob2(), newUser));

                // Add both workdays to the workday list
                workdayList.add(new UserWorkdayWrapper(newUser.getWorkDay1(), newUser));
                workdayList.add(new UserWorkdayWrapper(newUser.getWorkDay2(), newUser));

                // Add both floors to the floor list
                floorList.add(new UserFloorWrapper(newUser.getFloor1(), newUser));
                floorList.add(new UserFloorWrapper(newUser.getFloor2(), newUser));

                departmentList.add(new UserDepartmentWrapper(newUser.getDepartment(), newUser));

                if(i == ratNum)
                {
                    rat = newUser;
                    ratJob = getRandJob();
                    ratWorkDay = getRandWorkDay();
                    ratFloor = getRandFloor();
                    ratDepartment = getRandDepartment();
                }

                if (i % 10000 == 0) // for every 10000 users print a .
                    System.out.print(".");

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        database.close();
    }

    public ArrayList<User> findAndPrintUsersByJobs(String job1, String job2)
    {
        ArrayList<User> foundUsers = new ArrayList<>();
        // 1. Create a temporary wrapper object for the initial search.
        UserJobWrapper searchWrapper = new UserJobWrapper(job1, new User());

        // 2. Perform a log search to find the first occurrence of job1.
        Node<UserJobWrapper> foundNode = jobList.logSearch(searchWrapper);

        // 3. Handle the case where no users are found with the first job.
        if (foundNode == null)
        {
            System.out.println("No users found with the job: " + job1);
            return null;
        }

        System.out.println("Users with jobs '" + job1 + "'");
        if (job2 != null)
        {
            System.out.println(" and '" + job2 + "':");
        }
        else
        {
            System.out.println(":");
        }

        // 4. Start a linear scan from the first found node.
        Node<UserJobWrapper> current = foundNode;

        // 5. Iterate through the base level of the skip list.
        while (current != null && current.data.jobName.equalsIgnoreCase(job1))
        {
            // Access the original User object.
            User user = current.data.user;

            // Check if the user has a job that matches the second input (if it's not null).
            // The check must be against both job1 and job2 on the user object because a user has both.
            boolean matchesSecondJob = (job2 == null || (user.getJob1().equalsIgnoreCase(job1) && user.getJob2().equalsIgnoreCase(job2)) ||
                    (user.getJob1().equalsIgnoreCase(job2) && user.getJob2().equalsIgnoreCase(job1)));

            // Print the user's data if they have both jobs (or just job1 if job2 is null).
            if (matchesSecondJob)
            {
                /*
                System.out.println("SSN: " + user.getSSN());
                System.out.println("Name: " + user.getFirstName() + " " + user.getLastName());
                System.out.println("Job 1: " + user.job1 + " " + user.workDay1 + " " + user.floor1);
                System.out.println("Job 2: " + user.job2 + " " + user.workDay2 + " " + user.floor2);
                System.out.println("---------------------------------------------------------------");
                 */

                foundUsers.add(user);
                System.out.println(user.toString());
            }
            // Move to the next node in the list.
            current = current.forward[0];
        }
        //System.out.println("\nNumber of matching users found: " + foundUsers.size());
        return foundUsers;
    }


    public ArrayList<User> findAndPrintUsersByWorkday(String workDay1, String workDay2)
    {
        ArrayList<User> foundUsers = new ArrayList<>();
        // 1. Create a temporary wrapper object for the initial search.
        UserWorkdayWrapper searchWrapper = new UserWorkdayWrapper(workDay1, new User());

        // 2. Perform a log search to find the first occurrence of job1.
        Node<UserWorkdayWrapper> foundNode = workdayList.logSearch(searchWrapper);

        // 3. Handle the case where no users are found with the first job.
        if (foundNode == null)
        {
            System.out.println("No users found with the Work days: " + workDay1);
            return null;
        }

        System.out.println("Users with Work days '" + workDay1 + "'");
        if (workDay2 != null)
        {
            System.out.println(" and '" + workDay2 + "':");
        }
        else
        {
            System.out.println(":");
        }

        // 4. Start a linear scan from the first found node.
        Node<UserWorkdayWrapper> current = foundNode;

        // 5. Iterate through the base level of the skip list.
        while (current != null && current.data.workDayName.equalsIgnoreCase(workDay1))
        {
            // Access the original User object.
            User user = current.data.user;

            // Check if the user has a job that matches the second input (if it's not null).
            // The check must be against both job1 and job2 on the user object because a user has both.
            boolean matchesSecondJob = (workDay2 == null || (user.getWorkDay1().equalsIgnoreCase(workDay1) && user.getWorkDay2().equalsIgnoreCase(workDay2)) ||
                    (user.getWorkDay1().equalsIgnoreCase(workDay2) && user.getWorkDay2().equalsIgnoreCase(workDay1)));

            // Print the user's data if they have both jobs (or just job1 if job2 is null).
            if (matchesSecondJob)
            {
                /*
                System.out.println("SSN: " + user.SSN);
                System.out.println("Name: " + user.FirstName + " " + user.LastName);
                System.out.println("Job 1: " + user.job1 + " " + user.workDay1 + " " + user.floor1);
                System.out.println("Job 2: " + user.job2 + " " + user.workDay2 + " " + user.floor2);
                System.out.println("---------------------------------------------------------------");
                 */

                foundUsers.add(user);
                System.out.println(user.toString());
            }
            // Move to the next node in the list.
            current = current.forward[0];
        }
        //System.out.println("\nNumber of matching users found: " + foundUsers.size());
        return foundUsers;
    }

    public ArrayList<User> findAndPrintUsersByFloor(String floor1, String floor2)
    {
        ArrayList<User> foundUsers = new ArrayList<>();

        // 1. Create a temporary wrapper object for the initial search.
        UserFloorWrapper searchWrapper = new UserFloorWrapper(floor1, new User());

        // 2. Perform a log search to find the first occurrence of job1.
        Node<UserFloorWrapper> foundNode = floorList.logSearch(searchWrapper);

        // 3. Handle the case where no users are found with the first job.
        if (foundNode == null)
        {
            System.out.println("No users found working on floor: " + floor1);
            return null;
        }

        System.out.println("Users working on floor '" + floor1 + "'");
        if (floor2 != null)
        {
            System.out.println(" and '" + floor2 + "':");
        }
        else
        {
            System.out.println(":");
        }

        // 4. Start a linear scan from the first found node.
        Node<UserFloorWrapper> current = foundNode;

        // 5. Iterate through the base level of the skip list.
        while (current != null && current.data.floorNumber.equalsIgnoreCase(floor1))
        {
            // Access the original User object.
            User user = current.data.user;

            // Check if the user has a job that matches the second input (if it's not null).
            // The check must be against both job1 and job2 on the user object because a user has both.
            boolean matchesSecondJob = (floor2 == null || (user.getFloor1().equalsIgnoreCase(floor1) && user.getFloor2().equalsIgnoreCase(floor2)) ||
                    (user.getFloor1().equalsIgnoreCase(floor2) && user.getFloor2().equalsIgnoreCase(floor1)));

            // Print the user's data if they have both jobs (or just job1 if job2 is null).
            if (matchesSecondJob)
            {
                /*
                System.out.println("SSN: " + user.SSN);
                System.out.println("Name: " + user.FirstName + " " + user.LastName);
                System.out.println("Job 1: " + user.job1 + " " + user.workDay1 + " " + user.floor1);
                System.out.println("Job 2: " + user.job2 + " " + user.workDay2 + " " + user.floor2);
                System.out.println("---------------------------------------------------------------");
                 */

                foundUsers.add(user);
                System.out.println(user.toString());
            }
            // Move to the next node in the list.
            current = current.forward[0];
        }
        //System.out.println("\nNumber of matching users found: " + foundUsers.size());
        return foundUsers;
    }
    public ArrayList<User> findAndPrintUsersByDepartment(String department)
    {
        ArrayList<User> foundUsers = new ArrayList<>();

        // 1. Create a temporary wrapper object for the initial search.
        UserDepartmentWrapper searchWrapper = new UserDepartmentWrapper(department, new User());

        // 2. Perform a log search to find the first occurrence of job1.
        Node<UserDepartmentWrapper> foundNode = departmentList.logSearch(searchWrapper);

        // 3. Handle the case where no users are found with the first job.
        if (foundNode == null)
        {
            System.out.println("No users found working on department: " + department);
            return null;
        }

        System.out.println("Users working on department '" + department + ": ");

        // 4. Start a linear scan from the first found node.
        Node<UserDepartmentWrapper> current = foundNode;

        // 5. Iterate through the base level of the skip list.
        while (current != null && current.data.department.equalsIgnoreCase(department))
        {
            // Access the original User object.
            User user = current.data.user;

            // Check if the user has a job that matches the second input (if it's not null).
            // The check must be against both job1 and job2 on the user object because a user has both.
            boolean matches = ((user.getDepartment().equalsIgnoreCase(department)));

            // Print the user's data if they have both jobs (or just job1 if job2 is null).
            if (matches)
            {
                foundUsers.add(user);
                System.out.println(user.toString());
            }
            // Move to the next node in the list.
            current = current.forward[0];
        }
        //System.out.println("\nNumber of matching users found: " + foundUsers.size());
        return foundUsers;
    }

    public static ArrayList<ArrayList<User>> getCustomLists()
    {
        return customLists;
    }

    public static void addToCustomLists(ArrayList<User> list)
    {
        customLists.add(list);
    }
    public static void setCustomLists(ArrayList<ArrayList<User>> customLists)
    {
        customLists = customLists;
    }
}
