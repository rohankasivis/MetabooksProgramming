package mockclock;

public interface Clock
{
    int getMinutes();
    int getHour();
    int getSeconds();
    String getTime();
    void waitTill(int hour, int minute, int seconds) throws InterruptedException;
}
