import org.joda.time.*;

import java.io.*;
import java.net.UnknownHostException;

public class DocumentStream implements Runnable
{
    private DateTime time;
    private File currFile;
    private boolean [] hasRead;
    private File [] listOfFiles;
    private PrintWriter writer;

    public DocumentStream(DateTime time)
    {
        this.time = time;
        writer = OpenFile.openToWrite("fileData.txt");
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

    public void processFiles() throws InterruptedException, UnknownHostException, IOException
    {
        DateTime curr = DateTime.now();
        DocumentStream stream = new DocumentStream(curr);

        File [] listOfFiles = stream.readFiles();

        int hoursLeft = 0;
        int minutesLeft = 0;

        if(curr.getMinuteOfHour() != 0)
        {
            minutesLeft = 60 - curr.getMinuteOfHour();
        }

        if(curr.getHourOfDay() > 6)
            hoursLeft = 24 - curr.getHourOfDay() + 6;
        else
            hoursLeft = 6 - curr.getHourOfDay();

        Thread.sleep(3600000 * hoursLeft + 3600000 * minutesLeft / 60);

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
                    while (numTimesChecked <= 3 && !stream.fileExists(listOfFiles[j]))
                    {
                        if (stream.fileExists(listOfFiles[j]))
                            stream.readFile(listOfFiles[j]);
                        else
                            Thread.sleep(3600000);
                    }

                    if (!stream.fileExists(listOfFiles[j]))
                    {
                        stream.logFile(listOfFiles[j]);
                        SMTPMail.sendMail("guychill168@gmail.com", "gtarocks", "guychill168@gmail.com", "guychill168@gmail.com", "ftp failed to open", "Test result");
                    }
                }
            }
        }
    }

    public void run()
    {
        fileExists(currFile);
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

        DateTime curr = DateTime.now();
        writer.println(file.getName() + ": Read on: " + curr.getMonthOfYear() + "/" + curr.getDayOfMonth() + "/" + curr.getYear());

        int numLines = 0;
        String currLine;
        while ((currLine = reader.readLine()) != null)
            numLines++;

        reader.close();

        System.out.println("Number of lines in the file: " + numLines);
    }

    public void ftpFile() throws UnknownHostException
    {
        FTPTheClient client = new FTPTheClient();
        client.ftpFile(fileName());
    }

    public void logFile(File file)
    {
        DateTime curr = DateTime.now();
        writer.println("File " + file.getName() + " was not found on " + curr.getMonthOfYear() + "/" + curr.getDayOfMonth() + "/" + curr.getYear());
    }
}