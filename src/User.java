

public class User // stores the user data
{
    public String SSN;
    public String LastName;
    public String FirstName;

    public String job1;
    public String job2;

    public String workDay1;
    public String workDay2;

    public String floor1;
    public String floor2;

    User()
    {
        SSN = null;
        LastName = null;
        FirstName = null;
    }

    User getUserFromTxt(String s) // reads a string from txt and returns user from it
    {
        User tmpUser = new User();

        String parts[] = s.split(";"); // splits the inputed string by ; marks

        tmpUser.SSN = parts[0];
        tmpUser.LastName = parts[1];
        tmpUser.FirstName = parts[2];

        return tmpUser;
    }

    @Override
    public String toString()
    {
        String userInfo = "";
        userInfo += ("SSN: " + SSN);
        userInfo += ("\nName: " + FirstName + " " + LastName);
        userInfo += ("\nJob 1: " + job1 + " " + workDay1 + " " + floor1);
        userInfo += ("\nJob 2: " + job2 + " " + workDay2 + " " + floor2);
        userInfo += ("\n------------------------------------------------------");

        return userInfo;
    }
}

// Wrapper class to sort by SSN
class UserBySSN implements Comparable<UserBySSN>
{
    public User user;

    public UserBySSN(User user)
    {
        this.user = user;
    }

    @Override
    public int compareTo(UserBySSN other)
    {
        return this.user.SSN.compareTo(other.user.SSN);
    }
}

// Wrapper class to sort by FirstName
class UserJobWrapper implements Comparable<UserJobWrapper>
{
    public String jobName;
    public User user;

    public UserJobWrapper(String jobName, User user)
    {
        this.jobName = jobName;
        this.user = user;
    }

    @Override
    public int compareTo(UserJobWrapper other)
    {
        return this.jobName.compareTo(other.jobName);
    }
}

class UserWorkdayWrapper implements Comparable<UserWorkdayWrapper>
{
    public String workDayName;
    public User user; // Reference to the original User object

    public UserWorkdayWrapper(String workDayName, User user)
    {
        this.workDayName = workDayName;
        this.user = user;
    }

    @Override
    public int compareTo(UserWorkdayWrapper other)
    {
        // Compares by the workday name
        return this.workDayName.compareTo(other.workDayName);
    }
}

class UserFloorWrapper implements Comparable<UserFloorWrapper>
{
    public String floorNumber;
    public User user; // Reference to the original User object

    public UserFloorWrapper(String floorNumber, User user)
    {
        this.floorNumber = floorNumber;
        this.user = user;
    }

    @Override
    public int compareTo(UserFloorWrapper other)
    {
        // Compares by the floor number
        return this.floorNumber.compareTo(other.floorNumber);
    }
}