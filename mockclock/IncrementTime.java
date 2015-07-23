package mockclock;

import org.junit.*;
import org.joda.time.DateTime;

public class IncrementTime implements Clock
{
    private int year;
    private int month;
    private int day;
    private int seconds;
    private int minutes;
    private int hours;

    public IncrementTime()
    {
        year = 0;
        month = 0;
        day = 0;
    }

    public IncrementTime(int year, int month, int day, int seconds, int minutes, int hours)
    {
        this.year = year;
        this.month = month;
        this.day = day;
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
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

    public int getMinutes()
    {
        return DateTime.now().getMinuteOfHour() + minutes;
    }

    public int getHour()
    {
        return DateTime.now().getHourOfDay() + hours;
    }

    public int getSeconds()
    {
        return DateTime.now().getSecondOfMinute() + seconds;
    }

    public void waitTill(int hour, int minute, int seconds) throws InterruptedException
    {
    }

    public long getMilliseconds()
    {
        return 0;
    }
}