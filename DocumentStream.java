import org.joda.time.*;

import java.io.File;
import java.util.Date;

public class DocumentStream implements Runnable
{
    private DateTime time;

    public DocumentStream(DateTime time)
    {
        this.time = time;
    }

    private File [] readFiles()
    {
        File folder = new File("C:/FTPFiles");
        File[] list = folder.listFiles();
        File[] listOfFiles = new File[list.length];

        int pos = 0;

        for (int i = 0; i < list.length; i++) {
            if (list[i].isFile()) {
                listOfFiles[pos] = list[i];
                pos++;
            }
        }

        return listOfFiles;
    }

    public String returnFileName(DateTime time)
    {
        int year = time.getYear();
        int month = time.getMonthOfYear();
        int day = time.getDayOfMonth();

        return month + "-" + day + "-" + year + "data.txt";
    }

    public void run()
    {
        File [] listOfFiles = readFiles();
        for(int j = 0; j < listOfFiles.length; j++)
        {
            if(listOfFiles[j] != null)
            {
                long time = listOfFiles[j].lastModified();
                DateTime fileTime = new DateTime(time);
                int fileYear = fileTime.getYear();
                int setYear = this.time.getYear();
                int fileMonth = fileTime.getMonthOfYear();
                int setMonth = this.time.getMonthOfYear();
                int fileDay = fileTime.getDayOfMonth();
                int setDay = this.time.getDayOfMonth();

                if(fileYear > setYear)
                    ftpFile(listOfFiles[j]);
                else if(fileYear == setYear)
                    if(fileMonth > setMonth)
                        ftpFile(listOfFiles[j]);
                    else if(fileMonth == setMonth)
                        if(fileDay > setDay)
                            ftpFile(listOfFiles[j]);
                        else
                            logFile(listOfFiles[j]);
                    else
                        logFile(listOfFiles[j]);
                else
                    logFile(listOfFiles[j]);
            }
        }
    }

    private void ftpFile(File file)
    {

    }

    private void logFile(File file)
    {

    }
}