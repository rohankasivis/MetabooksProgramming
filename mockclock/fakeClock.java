package mockclock;

import org.joda.time.DateTime;

public class fakeClock implements Clock
{
    private int seconds;
    private int minutes;
    private int hours;
    private int secondsDifference;
    private int minutesDifference;
    private int hoursDifference;

    public fakeClock(int seconds, int minutes, int hours)
    {
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
        secondsDifference = seconds - DateTime.now().getSecondOfMinute();
        minutesDifference = minutes - DateTime.now().getMinuteOfHour();
        hoursDifference = hours - DateTime.now().getHourOfDay();
    }

    public void waitTill(int hours, int minutes, int seconds)
    {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getSeconds()
    {
        return (seconds + secondsDifference) % 60;
    }

    public int getMinutes()
    {
        return (minutes + minutesDifference) % 60;
    }

    public int getHour()
    {
        return (hours + hoursDifference) % 24;
    }

    public String getTime()
    {
        return getHour() + ":" + getMinutes() + "." + getSeconds();
    }
}
