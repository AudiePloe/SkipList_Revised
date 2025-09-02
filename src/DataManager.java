import java.io.*;
import java.util.ArrayList;

public class DataManager {


    static SkipList ssnList = new SkipList(); // the skip list that holds all the nodes
    static SkipList jobList = new SkipList();
    static SkipList workdayList = new SkipList();
    static SkipList floorList = new SkipList();


    private static final int MAX_LEVEL = 32;
    public static ArrayList<String> jobs = new ArrayList<>();
    public static ArrayList<String> workdays = new ArrayList<>();
    public static ArrayList<String> floors = new ArrayList<>();

    // A counter to keep track of the current item for balanced distribution
    private static int jobCounter = 0;
    private static int workdayCounter = 0;
    private static int floorCounter = 0;



    public void createAllLists() throws IOException {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get the next item in a balanced, cycling order
    public static String getNextJob() {
        return jobs.get(jobCounter++ % jobs.size());
    }

    public static String getRandJob()
    {
        return jobs.get((int)(Math.random() * jobs.size()));
    }

    public static String getNextWorkday() {
        return workdays.get(workdayCounter++ % workdays.size());
    }
    public static String getRandWorkDay()
    {
        return workdays.get((int)(Math.random() * workdays.size()));
    }

    public static String getNextFloor() {
        return floors.get(floorCounter++ % floors.size());
    }
    public static String getRandFloor()
    {
        return floors.get((int)(Math.random() * floors.size()));
    }

    // Call this method before starting your main user-reading loop
    public static void loadAllCategories() {
        readCategoriesFromFile("jobs.txt", jobs);
        readCategoriesFromFile("workdays.txt", workdays);
        readCategoriesFromFile("floors.txt", floors);

        jobCounter = (int)(Math.random() * 10);
        workdayCounter = (int)(Math.random() * 10);
        floorCounter = (int)(Math.random() * 10);
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
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return lines;
    }

    @SuppressWarnings("unchecked")
    public void readFromFile(File file) throws IOException {

        RandomAccessFile database = new RandomAccessFile(file, "rw");
        String line = "";
        long lineCount = countLines(file.getName());

        System.out.print("\n");

        for (int i = 0; i < lineCount; i++) {
            try {
                line = database.readLine();
                User newUser = new User();
                newUser = newUser.getUserFromTxt(line);

                // Assign two jobs, two workdays, and two floors
                // Use i * 2 to ensure a different item for the second assignment
                newUser.job1 = getNextJob();
                newUser.job2 = getRandJob();

                newUser.workDay1 = getNextWorkday();
                newUser.workDay2 = getRandWorkDay();

                newUser.floor1 = getNextFloor();
                newUser.floor2 = getRandFloor();


                ssnList.add(new UserBySSN(newUser));

                jobList.add(new UserJobWrapper(newUser.job1, newUser));
                jobList.add(new UserJobWrapper(newUser.job2, newUser));

                // Add both workdays to the workday list
                workdayList.add(new UserWorkdayWrapper(newUser.workDay1, newUser));
                workdayList.add(new UserWorkdayWrapper(newUser.workDay2, newUser));

                // Add both floors to the floor list
                floorList.add(new UserFloorWrapper(newUser.floor1, newUser));
                floorList.add(new UserFloorWrapper(newUser.floor2, newUser));


                if (i % 10000 == 0) // for every 10000 users print a .
                    System.out.print(".");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        database.close();
    }

    public void findAndPrintUsersByJobs(String job1, String job2) {
        // 1. Create a temporary wrapper object for the initial search.
        UserJobWrapper searchWrapper = new UserJobWrapper(job1, new User());

        // 2. Perform a log search to find the first occurrence of job1.
        Node<UserJobWrapper> foundNode = jobList.logSearch(searchWrapper);

        // 3. Handle the case where no users are found with the first job.
        if (foundNode == null) {
            System.out.println("No users found with the job: " + job1);
            return;
        }

        System.out.println("Users with jobs '" + job1 + "'");
        if (job2 != null) {
            System.out.println(" and '" + job2 + "':");
        } else {
            System.out.println(":");
        }

        // 4. Start a linear scan from the first found node.
        Node<UserJobWrapper> current = foundNode;

        int foundUsers = 0;
        // 5. Iterate through the base level of the skip list.
        while (current != null && current.data.jobName.equalsIgnoreCase(job1)) {
            // Access the original User object.
            User user = current.data.user;

            // Check if the user has a job that matches the second input (if it's not null).
            // The check must be against both job1 and job2 on the user object because a user has both.
            boolean matchesSecondJob = (job2 == null || (user.job1.equalsIgnoreCase(job1) && user.job2.equalsIgnoreCase(job2)) ||
                    (user.job1.equalsIgnoreCase(job2) && user.job2.equalsIgnoreCase(job1)));

            // Print the user's data if they have both jobs (or just job1 if job2 is null).
            if (matchesSecondJob) {
                System.out.println("SSN: " + user.SSN);
                System.out.println("Name: " + user.FirstName + " " + user.LastName);
                System.out.println("Job 1: " + user.job1 + " " + user.workDay1 + " " + user.floor1);
                System.out.println("Job 2: " + user.job2 + " " + user.workDay2 + " " + user.floor2);
                System.out.println("---------------------------------------------------------------");
                foundUsers++;
            }

            // Move to the next node in the list.
            current = current.forward[0];

        }
        System.out.println("\nNumber of matching users found: " + foundUsers);
    }

}
