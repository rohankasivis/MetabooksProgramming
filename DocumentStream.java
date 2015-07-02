import org.joda.time.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

public class DocumentStream implements Runnable
{
    private DateTime time;
    private File currFile;
    private boolean [] hasRead;
    private File [] listOfFiles;

    public DocumentStream(DateTime time)
    {
        this.time = time;
    }

    public File [] readFiles()
    {
        File folder = new File("C:/FTPFiles");
        File[] list = folder.listFiles();
        listOfFiles = new File[list.length];
        hasRead = new boolean[list.length];

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
        String fileName = fileName();

        if(file != null)
        {
            if(file.getName().equals(fileName))
            {
                return true;
            }
        }
        return false;
    }

    public String fileName()
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

    public boolean hasReadFile(DateTime time)
    {
        for(int j = 0; j < listOfFiles.length; j++)
        {
            if(listOfFiles[j].lastModified() == time.getMillis())
            {
                return hasRead[j];
            }
        }
        return false;
    }

    public boolean isAfterDate(DateTime newTime)
    {
        int fileYear = newTime.getYear();
        int setYear = this.time.getYear();
        int fileMonth = newTime.getMonthOfYear();
        int setMonth = this.time.getMonthOfYear();
        int fileDay = newTime.getDayOfMonth();
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

    public void readFile(File file) throws IOException
    {
        for(int j = 0; j < listOfFiles.length; j++)
        {
            if(listOfFiles[j] == file)
                hasRead[j] = true;
        }
        FileReader fileToRead = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileToRead);

        int numLines = 0;
        String currLine;
        while ((currLine = reader.readLine()) != null)
            numLines++;

        reader.close();

        System.out.println("Number of lines in the file: " + numLines);
    }

    public void ftpFile() throws UnknownHostException
    {
        FTPTheClient.ftpFile(fileName());
    }

    public void logFile()
    {

    }
}