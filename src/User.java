

public class User // stores the user data
{
    private String SSN;
    private String LastName;
    private String FirstName;

    private String job1;
    private String job2;

    private String workDay1;
    private String workDay2;

    private String floor1;
    private String floor2;

    private String department;

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
        userInfo += ("\nDepartment: " + department);
        userInfo += ("\n------------------------------------------------------");

        return userInfo;
    }

    public void setFloor2(String floor2)
    {
        this.floor2 = floor2;
    }

    public void setFloor1(String floor1)
    {
        this.floor1 = floor1;
    }

    public void setWorkDay2(String workDay2)
    {
        this.workDay2 = workDay2;
    }

    public void setWorkDay1(String workDay1)
    {
        this.workDay1 = workDay1;
    }

    public void setJob2(String job2)
    {
        this.job2 = job2;
    }

    public void setJob1(String job1)
    {
        this.job1 = job1;
    }

    public void setSSN(String SSN)
    {
        this.SSN = SSN;
    }

    public String getJob1()
    {
        return job1;
    }
    public String getJob2()
    {
        return job2;
    }
    public String getWorkDay1()
    {
        return workDay1;
    }
    public String getWorkDay2()
    {
        return workDay2;
    }
    public String getFloor1()
    {
        return floor1;
    }
    public String getFloor2()
    {
        return floor2;
    }
    public String getSSN()
    {
        return SSN;
    }
    public String getFirstName()
    {
        return FirstName;
    }
    public String getLastName()
    {
        return LastName;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
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
        return this.user.getSSN().compareTo(other.user.getSSN());
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
class UserDepartmentWrapper implements Comparable<UserDepartmentWrapper>
{
    public String department;
    public User user; // Reference to the original User object

    public UserDepartmentWrapper(String department, User user)
    {
        this.department = department;
        this.user = user;
    }

    @Override
    public int compareTo(UserDepartmentWrapper other)
    {
        // Compares by the floor number
        return this.department.compareTo(other.department);
    }
}