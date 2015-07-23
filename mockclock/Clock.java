package mockclock;

public interface Clock
{
    int getYear();
    int getDayOfMonth();
    int getMonthOfYear();
    int getMinutes();
    int getHour();
    int getSeconds();
    void waitTill(int hour, int minute, int seconds) throws InterruptedException;
    long getMilliseconds();
}
