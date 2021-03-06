package java.illinois.edu.DocumentStreamServer.mockclock;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import java.io.IOException;

public class fakeClock implements Clock
{
    private int secondsDifference;
    private int minutesDifference;
    private int hoursDifference;

    public fakeClock(int seconds, int minutes, int hours)
    {
        waitTill(hours, minutes, seconds);
    }

    public void waitTill(int hours, int minutes, int seconds)
    {
        secondsDifference = seconds - DateTime.now().getSecondOfMinute();
        minutesDifference = minutes - DateTime.now().getMinuteOfHour();
        hoursDifference = hours - DateTime.now().getHourOfDay();
    }

    // this is the main at method which will run the closure at the specific time
    public void at(int hours, int minutes, int seconds, Runnable closure) throws IOException
    {
        waitTill(hours, minutes, seconds);
        closure.run();
    }

    public int getSeconds()
    {
        return (DateTime.now().getSecondOfMinute() + secondsDifference) % 60;
    }

    public int getMinutes()
    {
        return (DateTime.now().getMinuteOfHour() + minutesDifference) % 60;
    }

    public int getHour()
    {
        return (DateTime.now().getHourOfDay() + hoursDifference) % 24;
    }

    public String getTime()
    {
        return getHour() + ":" + getMinutes() + "." + getSeconds();
    }

    public void fixTime(long millis)
    {
        DateTimeUtils.setCurrentMillisFixed(millis);
    }

    public int getDayOfMonth()
    {
        return DateTime.now().getDayOfMonth();
    }

    public int getMonthOfYear()
    {
        return DateTime.now().getMonthOfYear();
    }

    public int getYear()
    {
        return DateTime.now().getYear();
    }

    public long getMilliseconds()
    {
        return DateTime.now().getMillis();
    }
}