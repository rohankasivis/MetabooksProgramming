import org.joda.time.*;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

public class RunDocuments
{
    public static void main(String [] args) throws InterruptedException, UnknownHostException, IOException
    {
        DateTime curr = DateTime.now();
        DocumentStream stream = new DocumentStream(curr);

        File [] listOfFiles = stream.readFiles();

        while (curr.getHourOfDay() != 6)
        {
            Thread.sleep(60000);
        }

        stream.ftpFile();

        while(!stream.isAfterDate(DateTime.now()))
        {
            Thread.sleep(36000000);
        }

        for(int j = 0; j < listOfFiles.length; j++)
        {
            if((listOfFiles[j].lastModified() == DateTime.now().getMillis()))
                return;

            if(listOfFiles[j] != null)
            {
                if (stream.fileExists(listOfFiles[j]))
                    stream.readFile(listOfFiles[j]);
                else
                {
                    int numTimesChecked = 1;
                    while (numTimesChecked <= 6 && !stream.fileExists(listOfFiles[j]))
                    {
                        if (stream.fileExists(listOfFiles[j]))
                            stream.readFile(listOfFiles[j]);
                        else
                            Thread.sleep(3600000);
                    }

                    if (!stream.fileExists(listOfFiles[j]))
                        stream.logFile();
                }
            }
        }
    }
}