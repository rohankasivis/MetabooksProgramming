package documents;

import documentsFtp.FTPClient;
import documentsMail.SMTPMail;
import mockclock.AccurateTime;
import mockclock.Clock;
import org.joda.time.*;
import java.util.Scanner;

import java.io.*;
import java.net.UnknownHostException;

// This class is responsible for reading all of the documents and keeping a state of
// which files are read and what still needs to be done.
public class DocumentStream implements Runnable
{
    private Clock time;
    private boolean [] hasRead;
    private File [] listOfFiles;
    private PrintWriter writer;

    // This is the constructor for the class, which takes a DateTime that will
    // represent the time that run is based on
    public DocumentStream(Clock time)
    {
        this.time = time;
        try
        {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("filedata.txt", true)));
        }catch (IOException e)
        {
            System.exit(1);
        }
    }

    // This reads all of the files in the FTPFiles server, and also parses the log file to determine
    // the last time files were read.
    public File [] readFiles() throws IOException
    {
        File folder = new File("C:/FTPFilesTesting");
        File[] list = folder.listFiles();
        listOfFiles = new File[list.length];
        hasRead = new boolean[list.length];

        int date[] = parseLogFile();
        int month = date[0];
        int day = date[1];
        int year = date[2];

        int pos = 0;

        for (int i = 0; i < list.length; i++)
        {
            if (list[i].isFile())
            {
                listOfFiles[pos] = list[i];
                pos++;

                long time = list[i].lastModified();
                if(this.time.getMilliseconds() > time)
                {
                    readFile(list[i]);
                }
            }
        }

        // once done reading through the files, print the current date: this indicates as of now the last time the files have been read
        writer.println("Last time read: " + DateTime.now().getMonthOfYear() + "-" + DateTime.now().getDayOfMonth() + "-" + DateTime.now().getYear());

        return listOfFiles;
    }

    // This is the method responsible for parsing the log file
    // and getting the last date the files were read.
    public int[] parseLogFile()
    {
        String lastDateRead = "";

        // uses Scanner to open the parse file and then determine the date
        Scanner console = OpenFile.openToRead("filedata.txt");
        while (console.hasNext())
        {
            String line = console.nextLine();
            if(line.contains("Last time read:"))
            {
                lastDateRead = line;
            }
        }

        // parses the string which contains the last date modified
        if(lastDateRead.length() == 0)
            return new int[3];
        else
        {
            lastDateRead = lastDateRead.substring(16);
            String values[] = new String[3];
            values = lastDateRead.split("\\-");
            int month = Integer.parseInt(values[0]);
            int day = Integer.parseInt(values[1]);
            int year = Integer.parseInt(values[2]);
            int[] date = new int[3];
            date[0] = month;
            date[1] = day;
            date[2] = year;

            return date;
        }
    }

    // checks to see whether the file passed in exists or not
    public boolean fileExists() throws IOException
    {
        listOfFiles = readFiles();
        if(listOfFiles != null && listOfFiles.length > 0)
        {
            for(int j = 0; j < listOfFiles.length; j++)
            {
                if(listOfFiles[j].getName().equals(fileName()))
                    return true;
            }
            return false;
        }
        return false;
    }

    // returns the appropriate file name for the file that is currently being set on ftp
    public String fileName()
    {
        int year = time.getYear();
        int month = time.getMonthOfYear();
        int day = time.getDayOfMonth();

        return month + "-" + day + "-" + year + "data.txt";
    }

    // Processes all of the files and ftps the current file at 6 am
    public void processFiles() throws InterruptedException, IOException
    {
        time.waitTill(6, 0, 0);
        ftpFile();

        /*
        // if the date passed in is later than the current date, wait until the appropriate time
        while(!isAfterDate(DateTime.now()))
        {
            Thread.sleep(36000000);
        }
        */

        listOfFiles = readFiles();

        handleFile();
    }

    // This method is used to handle the file and log/send emails appropriately
    public void handleFile() throws IOException, InterruptedException
    {
        // if the file exists, then read the file
        if (fileExists())
            readFile(new File("C:/FTPFilesTesting/" + fileName()));
        else
        {
            // otherwise, wait for 3 hours and see if the file will be updated by then
            int numTimesChecked = 1;
            while (numTimesChecked <= 3 && !fileExists())
            {
                if (fileExists())
                {
                    readFile(new File("C:/FTPFilesTesting/" + fileName()));
                    return;
                }
                else
                    Thread.sleep(3600000);
                numTimesChecked++;
            }

            // if it still does not exist, then log the file and send an email stating that the test failed
            if (!fileExists())
            {
                logFile(fileName());
                SMTPMail.sendMail("guychill168@gmail.com", "gtarocks", "guychill168@gmail.com", "guychill168@gmail.com", "ftp failed to open", "Test result");
            }
        }
    }

    // the run method of this class which calls processfiles, the key method of the class
    public void run()
    {
        try {
            processFiles();
        }catch (UnknownHostException e)
        {
            System.exit(1);
        }
        catch (IOException e)
        {
            System.exit(1);
        }
        catch (InterruptedException e)
        {
            System.exit(1);
        }
    }

    // this method checks to see whether the file has been read or not
    public boolean hasReadFile(DateTime time)
    {
        for(int j = 0; j < listOfFiles.length; j++)
        {
            if(listOfFiles[j].lastModified() == time.getMillis())
                return hasRead[j];
        }
        return false;
    }

    /*
    // this method checks to see if the current date is after the date specified by the constructor
    // as an indicator for reading the files
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
    */

    // This method is used to read the file, and once it does this, it prints out the date the specific file was read
    public void readFile(File file) throws IOException
    {
        for(int j = 0; j < listOfFiles.length; j++)
        {
            if(listOfFiles[j] == file)
                hasRead[j] = true;
        }
        FileReader fileToRead = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileToRead);

        writer.println(file.getName() + ": Read on: " + time.getMonthOfYear() + "-" + time.getDayOfMonth() + "-" + time.getYear());

        reader.close();
    }

    // uses ftpclient to ftp the file at 6 am
    public void ftpFile() throws UnknownHostException
    {
        FTPClient client = new FTPClient();
        client.ftpFile(fileName());
    }

    // print an error message in the log file that the specific file was not found on the specific date it was searched
    public void logFile(String fileName)
    {
        DateTime curr = DateTime.now();
        writer.println("File " + fileName + " was not found on " + curr.getMonthOfYear() + "-" + curr.getDayOfMonth() + "-" + curr.getYear());
    }
}