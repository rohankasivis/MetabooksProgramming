import org.joda.time.*;
import java.io.File;

public class RunDocuments
{
    public static void main(String [] args) throws InterruptedException
    {
        DateTime curr = DateTime.now();
        DocumentStream stream = new DocumentStream(curr);
        if(stream.fileExists())
            stream.ftpFile();
        else
        {
            int numTimesChecked = 1;
            while(numTimesChecked <= 6 && !stream.fileExists())
            {
                if(stream.fileExists())
                    stream.ftpFile();
                else
                    Thread.sleep(3600000);
            }

            if(!stream.fileExists())
                stream.logFile();
        }
    }
}
