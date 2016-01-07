package java.illinois.edu.DocumentStreamServer.documents;

import java.illinois.edu.DocumentStreamServer.OpenFile.OpenFile;
import java.illinois.edu.DocumentStreamServer.documentsFtp.FileServer;
import java.illinois.edu.DocumentStreamServer.documentsFtp.IFTPClient;
import java.illinois.edu.DocumentStreamServer.documentsMail.Email;
import java.illinois.edu.DocumentStreamServer.mockclock.Clock;
import org.joda.time.*;

import java.util.Scanner;

import java.io.*;
import java.net.UnknownHostException;

// This class is responsible for reading all of the java.illinois.edu.DocumentStreamServer.documents and keeping a state of
// which files are read and what still needs to be done.
public class DocumentStream implements Runnable, Stream
{
    private Clock time;
    private PrintWriter writer;
    private Email email;
    private IFTPClient ftp;

    // newly added - the time at which the files will be placed in ftp server
    private int hourAtFTP;
    private int minuteAtFTP;
    private int secondAtFTP;

    // This is the constructor for the class, which takes an appropriate clock/ftp/email
    // newly added - it takes in the time to wait until
    public DocumentStream(Clock time, IFTPClient ftp, Email email, int hourAtFTP, int minuteAtFTP, int secondAtFTP)
    {
        this.time = time;
        this.ftp = ftp;
        this.email = email;
        this.hourAtFTP = hourAtFTP;
        this.minuteAtFTP = minuteAtFTP;
        this.secondAtFTP = secondAtFTP;

        try
        {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("filedata.txt", true)));
        }catch (IOException e)
        {
            System.exit(1);
        }
    }

    // This function is used to return a list of files from the local server.
    public File [] getLocalFiles() throws IOException
    {
        File folder = new File("C:/FTPFilesTesting");
        File[] list = folder.listFiles();
        return list;
    }

    // This reads all of the files in the FTPFiles server, and also parses the log file to determine
    // the last time files were read.
    public void recoverState() throws IOException
    {
        File[] list = getLocalFiles();

        int date[] = parseLogFile();
        int month = date[0];
        int day = date[1];
        int year = date[2];

        for (int i = 0; i < list.length; i++)
        {
            // todo - complete comparing dates and then decide how to read the files
        }

        // once done reading through the files, print the current date: this indicates as of now the last time the files have been read
        writer.println("Last time read: " + DateTime.now().getMonthOfYear() + "-" + DateTime.now().getDayOfMonth() + "-" + DateTime.now().getYear());
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

    // checks to see whether the file is visible within FTP or not
    public boolean fileExistsFTP() throws IOException
    {
        String [] filenames = FileServer.getListOfFiles();
        for(int j = 0; j < filenames.length; j++)
        {
            if(filenames[j].equals(fileName()))
                return true;
        }
        return false;
    }

    // checks to see whether the file passed in exists within the local server or not - somewhat of a helper function too with regards to creating the new file
    public boolean fileExistsLocal(File file) throws IOException
    {
        File [] listOfFiles = getLocalFiles();
        if(listOfFiles != null && listOfFiles.length > 0)
        {
            for(int j = 0; j < listOfFiles.length; j++)
            {
                if(listOfFiles[j].getName().equals(file.getName()));
                    return true;
            }
            return false;
        }
        return false;
    }

    // a reference helper function to state the appropriate file name
    public String fileName()
    {
        int year = time.getYear();
        int month = time.getMonthOfYear();
        int day = time.getDayOfMonth();

        return month + "-" + day + "-" + year + "data.txt";
    }

    public File createFile() throws FileNotFoundException, IOException
    {
        // If the file already exists within the local server (as it is supposed to), simply return it
        File file = new File("C:/FTPFilesTesting/" + fileName());
        if(fileExistsLocal(file))
            return file;
        // otherwise, add input into it stating that no data exists for the current day
        PrintWriter out = new PrintWriter(file);
        out.println("Empty file today. There is no data.");
        out.close();
        return file;
    }

    // This method is used to handle the file and log/send emails appropriately
    // -new changes added - now uses at and closures to perform the task
    // In this case, we wait 3 hours to check if the file has been put into ftp/fetched properly, once each hour if not successful in the first attempt
    public void handleFile() throws IOException, InterruptedException
    {
        File file = createFile();
        time.at(hourAtFTP, minuteAtFTP, secondAtFTP, (() -> {try {ftpFile();} catch (UnknownHostException e) {e.printStackTrace();}}));  // newly added - FTP Files

        recoverState();

        // if the file exists, then read the file
        if (fileExistsFTP() && fetchFile(file))
            logFileRead(file.getName());
        else
        {
            // otherwise, wait for 3 hours and see if the file will be updated by then
            int numTimesChecked = 1;
            while (numTimesChecked <= 3 && !fileExistsFTP())
            {
                if (fileExistsFTP() && fetchFile(file))
                {
                    logFileRead(file.getName());
                    return;
                }
                else
                    time.waitTill(time.getHour() + 1, time.getMinutes(), time.getSeconds());
                numTimesChecked++;
            }

            // if it still does not exist, then log the file and send an email stating that the test failed
            if (!fileExistsFTP())
            {
                logFileError(file.getName());
                email.sendMail("guychill197@gmail.com", "gtarocks", "guychill197@gmail.com", "guychill197@gmail.com", "ftp failed to open", "Test result");
            }
        }
    }

    // the run method of this class which calls handleFiles, the key method of the class
    public void run()
    {
        try {
            handleFile();
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

    // this method will be used to fetch the file from FTP, and will return false if not able to fetch successfully
    public boolean fetchFile(File file) throws IOException
    {
        // newly added - code to fetch the file
        File ftpFile = FileServer.getFile(file.getName());
        if(ftpFile == null)
            return false;
        else
        {
            logFileRead(file.getName());
            return true;
        }
    }

    // uses ftpclient to ftp the file at 6 am
    public void ftpFile() throws UnknownHostException
    {
        ftp.ftpFile("C:/FTPFilesTesting/" + fileName());
    }

    // prints a message in the log file indicating the time a particular file was read at
    public void logFileRead(String fileName)
    {
        DateTime curr = DateTime.now();
        writer.println("File " + fileName + " was read on " + curr.getMonthOfYear() + "-" + curr.getDayOfMonth() + "-" + curr.getYear() + ", at time " + curr.getHourOfDay() + ":" + curr.getMinuteOfHour());
    }

    // print an error message in the log file that the specific file was not found on the specific date it was searched
    public void logFileError(String fileName)
    {
        DateTime curr = DateTime.now();
        writer.println("File " + fileName + " was not found on " + curr.getMonthOfYear() + "-" + curr.getDayOfMonth() + "-" + curr.getYear() + ", at time " + curr.getHourOfDay() + ":" + curr.getMinuteOfHour());
    }
}