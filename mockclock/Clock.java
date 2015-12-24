
package mockclock;

<<<<<<< HEAD
=======
import documents.DocumentStream;
>>>>>>> ff3d78988148663c85ffc3ce26282a142fbf3e6e
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
<<<<<<< HEAD
    public  void at(int hours, int minutes, int seconds, Runnable closure) throws IOException, InterruptedException;
=======
    public  void at(int hours, int minutes, Runnable closure) throws IOException;
>>>>>>> ff3d78988148663c85ffc3ce26282a142fbf3e6e
}