package mockclock;

import java.io.IOException;

public interface Clock
{
    int getMinutes();
    int getHour();
    int getSeconds();
    String getTime();
    void waitTill(int hour, int minute, int seconds) throws InterruptedException;
    public int getDayOfMonth();
    public int getMonthOfYear();
    public int getYear();
    public long getMilliseconds();
    public  void at(int hours, int minutes, int seconds, Runnable closure) throws IOException, InterruptedException;
}