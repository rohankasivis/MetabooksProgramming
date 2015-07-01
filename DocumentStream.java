import org.joda.time.*;
import java.io.File;
import java.net.UnknownHostException;

public class DocumentStream implements Runnable
{
    private DateTime time;
    private File currFile;

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

    public boolean fileExists(File file)
    {
        String fileName = returnFileName();

        if(file != null)
        {
            if(file.getName().equals(fileName))
                return true;
        }

        return false;
    }

    public String returnFileName()
    {
        int year = time.getYear();
        int month = time.getMonthOfYear();
        int day = time.getDayOfMonth();

        return month + "-" + day + "-" + year + "data.txt";
    }

    public void run()
    {
        fileExists(currFile);
    }

    public boolean isAfterDate(File file)
    {
        if(file != null)
        {
            long time = file.lastModified();
            DateTime fileTime = new DateTime(time);
            int fileYear = fileTime.getYear();
            int setYear = this.time.getYear();
            int fileMonth = fileTime.getMonthOfYear();
            int setMonth = this.time.getMonthOfYear();
            int fileDay = fileTime.getDayOfMonth();
            int setDay = this.time.getDayOfMonth();

            if(fileYear > setYear)
                return true;
            else if(fileYear == setYear)
                if(fileMonth > setMonth)
                    return true;
                else if(fileMonth == setMonth)
                    return fileDay > setDay;
                else
                    return false;
            else
                return false;
        }
        else
            return false;
    }

    public void ftpFile() throws UnknownHostException
    {
        FTPTheClient.ftpFile(returnFileName());
    }

    public void logFile()
    {

    }
}