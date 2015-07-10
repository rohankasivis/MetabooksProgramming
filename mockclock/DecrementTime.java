package mockclock;

import org.joda.time.DateTime;

public class DecrementTime implements MockDateTime
{
    private int year;
    private int month;
    private int day;

    public DecrementTime()
    {
        year = 1;
        month = 1;
        day = 1;
    }

    public DecrementTime(int year, int month, int day)
    {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear()
    {
        return DateTime.now().getYear() - year;
    }

    public int getDayOfMonth()
    {
        return DateTime.now().getDayOfMonth() - day;
    }

    public int getMonthOfYear()
    {
        return DateTime.now().getMonthOfYear() - month;
    }
}