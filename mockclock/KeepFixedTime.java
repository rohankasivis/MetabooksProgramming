/*
package mockclock;

import org.junit.*;
import org.joda.time.DateTime;

public class KeepFixedTime implements Clock
{
    private int year;
    private int month;
    private int day;
    private int seconds;
    private int hours;
    private int minutes;

    public KeepFixedTime()
    {
        year = DateTime.now().getYear();
        month = DateTime.now().getMonthOfYear();
        day = DateTime.now().getDayOfMonth();
        seconds = DateTime.now().getSecondOfMinute();
        hours = DateTime.now().getHourOfDay();
        minutes = DateTime.now().getMinuteOfHour();
    }

    public KeepFixedTime(int year, int month, int day, int seconds, int hours, int minutes)
    {
        this.year = year;
        this.month = month;
        this.day = day;
        this.seconds = seconds;
        this.hours = hours;
        this.minutes = minutes;
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

    public int getSeconds()
    {
        return seconds;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public int getHour()
    {
        return hours;
    }

    public void waitTill(int hour, int minute, int seconds) throws InterruptedException
    {
    }

    public long getMilliseconds()
    {
        return 0;
    }
}
*/