package documents;

import mockclock.AccurateTime;
import mockclock.Clock;
import org.joda.time.*;

import java.io.IOException;
import java.net.UnknownHostException;

public class RunDocuments
{
    public static void main(String [] args) throws InterruptedException, UnknownHostException, IOException
    {
        Clock clock = new AccurateTime();
        (new Thread(new DocumentStream(clock))).start();
    }
}