package documents;

import mockclock.AccurateTime;
import mockclock.Clock;
import org.joda.time.*;

import java.io.IOException;
import java.net.UnknownHostException;

public class DocumentServer
{
    public static void main(String [] args) throws InterruptedException, UnknownHostException, IOException
    {
        AccurateTime clock = new AccurateTime();
        (new Thread(new DocumentStream(clock))).start();
    }
}