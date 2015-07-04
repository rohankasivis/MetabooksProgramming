import org.joda.time.DateTime;

public class IncrementDays implements MockDateTime
{
    private int year;
    private int month;
    private int day;

    public IncrementDays()
    {
        year = 1;
        month = 1;
        day = 1;
    }

    public IncrementDays(int year, int month, int day)
    {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear()
    {
        return DateTime.now().getYear() + year;
    }

    public int getDayOfMonth()
    {
        return DateTime.now().getDayOfMonth() + day;
    }

    public int getMonthOfYear()
    {
        return DateTime.now().getMonthOfYear() + month;
    }
}
