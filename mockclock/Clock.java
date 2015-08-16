package mockclock;

import documents.DocumentStream;
import documents.FTPExists;

import java.io.IOException;
import java.util.function.Function;

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
    public  void at(int hours, int minutes, Runnable closure) throws IOException;
}
