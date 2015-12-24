
package mockclock;

import org.joda.time.DateTime;

import java.io.IOException;

public class AccurateTime implements Clock
{
    // no need for constructor

    // at method just like in fake clock
    public void at(int hours, int minutes, int seconds, Runnable closure) throws IOException, InterruptedException
    {
        waitTill(hours, minutes, seconds);
        closure.run();
    }

    public int getMinutes()
    {
        return DateTime.now().getMinuteOfHour();
    }

    public int getHour()
    {
        return DateTime.now().getHourOfDay();
    }

    public int getSeconds()
    {
        return DateTime.now().getSecondOfMinute();
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

    // uses Thread to wait until the given time of the specific day
    public void waitTill(int hour, int minute, int seconds) throws InterruptedException
    {
        int hoursLeft = 0;
        int minutesLeft = 0;

        // this part of the method calculates the remaining time from the current time until 6 am
        if(getMinutes() != 0)
            minutesLeft += 60 - getMinutes();

        if(minute != 0)
            minutesLeft += minute;

        if(getHour() > hour)
            hoursLeft = 24 - getHour() + 6;
        else
            hoursLeft = 6 - getHour();

        Thread.sleep(3600000 * hoursLeft + 3600000 * minutesLeft / 60);
    }

    public long getMilliseconds()
    {
        return DateTime.now().getMillis();
    }

    public String getTime()
    {
        return getHour() + ":" + getMinutes() + "." + getSeconds();
    }
}