package mockclock;

import org.joda.time.DateTime;

public class KeepFixedTime implements MockDateTime
{
    private int year;
    private int month;
    private int day;

    public KeepFixedTime()
    {
        year = DateTime.now().getYear();
        month = DateTime.now().getMonthOfYear();
        day = DateTime.now().getDayOfMonth();
    }

    public KeepFixedTime(int year, int month, int day)
    {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear()
    {
        return year;
    }

    public int getDayOfMonth()
    {
        return day;
    }

    public int getMonthOfYear()
    {
        return month;
    }
}