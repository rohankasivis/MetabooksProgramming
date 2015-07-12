package mockclock;

import org.joda.time.DateTime;

public class AccurateTime implements Clock
{
    private int year;
    private int month;
    private int day;

    public AccurateTime()
    {
        year = DateTime.now().getYear();
        month = DateTime.now().getMonthOfYear();
        day = DateTime.now().getDayOfMonth();
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
