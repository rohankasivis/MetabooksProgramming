import java.util.Date;

public class TimePrinter implements Runnable
{
    private int timeElapsed;

    public TimePrinter(int timeElapsed)
    {
        this.timeElapsed = timeElapsed;
    }

    public void run()
    {
        int difference = 0;
        int numTimesPrinted = 0;
        Date temp = new Date();
        int prevSecond = temp.getSeconds();
        while(numTimesPrinted < 12)
        {
            Date date = new Date();
            int hours = date.getHours();
            int minutes = date.getMinutes();
            int seconds = date.getSeconds();

            String currHour = "";
            String currMinute = "";
            String currSecond = "";

            if (hours < 10)
                currHour = "0" + hours;
            else
                currHour = hours + "";

            if (minutes < 10)
                currMinute = "0" + minutes;
            else
                currMinute = minutes + "";

            if (seconds < 10)
                currSecond = "0" + seconds;
            else
                currSecond = seconds + "";

            if(seconds > prevSecond)
                difference = seconds - prevSecond;
            else
            {
                difference = seconds + (60 - prevSecond);
            }

            if(difference == timeElapsed || numTimesPrinted == 0)
            {
                System.out.println("Current Time: " + currHour + ":" + currMinute + "." + currSecond);
                numTimesPrinted++;
                prevSecond = seconds;
            }
        }
    }
}