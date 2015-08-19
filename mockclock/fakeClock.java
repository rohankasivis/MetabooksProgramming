package mockclock;

import com.sun.corba.se.spi.orbutil.closure.Closure;
import com.sun.org.apache.xpath.internal.operations.Bool;
import documents.DocumentStream;
import org.easymock.samples.BasicClassMockTest;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

public class fakeClock implements Clock
{
    private int secondsDifference;
    private int minutesDifference;
    private int hoursDifference;
    private ArrayList<String> toDoList;
    //private DocumentStream stream;

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

    public void at(int hours, int minutes, Runnable closure) throws IOException
    {
        waitTill(hours, minutes, 0);
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