package documents;

import org.joda.time.*;

import java.io.IOException;
import java.net.UnknownHostException;

public class RunDocuments
{
    public static void main(String [] args) throws InterruptedException, UnknownHostException, IOException
    {
        (new Thread(new DocumentStream(DateTime.now()))).start();
    }
}