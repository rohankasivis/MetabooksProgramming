package mockclock;

import org.joda.time.DateTime;

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
}