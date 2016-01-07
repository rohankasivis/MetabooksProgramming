package java.illinois.edu.DocumentStreamServer.documents;

import java.illinois.edu.DocumentStreamServer.documentsMail.Email;
import java.illinois.edu.DocumentStreamServer.documentsMail.ReadMail;
import java.illinois.edu.DocumentStreamServer.mockclock.Clock;
import org.joda.time.DateTime;

import java.io.*;
import java.net.UnknownHostException;
import java.util.List;

public class DocumentStreamEmail extends DocumentStream implements Runnable
{
    private PrintWriter writer;
    private Clock time;
    private Email email;

    // newly added - private variables that indicate the time at which files will be placed in FTP server
    private int hourAtEmail;
    private int minuteAtEmail;
    private int secondAtEmail;

    public DocumentStreamEmail(Clock time, Email email, int hourAtEmail, int minuteAtEmail, int secondAtEmail)
    {
        super(time, null, email, hourAtEmail, minuteAtEmail, secondAtEmail);
        this.time = time;
        this.email = email;
        this.hourAtEmail = hourAtEmail;
        this.minuteAtEmail = minuteAtEmail;
        this.secondAtEmail = secondAtEmail;

        try
        {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("filedata.txt", true)));
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // todo - needs changes
    public void recoverState() throws IOException
    {
        ReadMail mail = new ReadMail();
        List<File> files = mail.getAttachments();
        File [] file = new File[files.size()];
        for(int j = 0; j < files.size(); j++)
        {
            file[j] = files.get(j);
        }

        writer.println("Last time read: " + DateTime.now().getMonthOfYear() + "-" + DateTime.now().getDayOfMonth() + "-" + DateTime.now().getYear());
    }

    // this function checks to see whether the file has successfully been sent by checking the most recent email
    public boolean fetchEmail(File file) throws IOException
    {
        ReadMail mail = new ReadMail();
        List<File> files = mail.getAttachments();
        if(files.contains(file))
        {
            logFileRead(file.getName());
            return true;
        }
        else
            return false;
    }

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

    // overriding the original handleFile() method in order to send/handle emails rather than process FTP input/output
    // new changes - it now models documentStream
    public void handleFile() throws IOException, InterruptedException
    {
        File file = super.createFile();
        time.at(hourAtEmail, minuteAtEmail, secondAtEmail, (() -> {email.sendMailWithAttachment("guychill197@gmail.com", "gtarocks", "guychill197@gmail.com", "guychill197@gmail.com", "This is the email to process.", "Attachments", "FTPFilesTesting " + super.fileName(), super.fileName());}));
        recoverState();

        // If the file exists within email and it is able to be fetched from email
        if(fileExistsEmail() && fetchEmail(file))
            super.logFileRead(file.getName());
        else
        {
            // otherwise, wait for 3 hours and see if the file will be updated by then
            int numTimesChecked = 1;
            while (numTimesChecked <= 3 && !fileExistsEmail())
            {
                if (fileExistsEmail() && fetchEmail(file))
                {
                    logFileRead(file.getName());
                    return;
                }
                else
                    time.waitTill(time.getHour() + 1, time.getMinutes(), time.getSeconds());
                numTimesChecked++;
            }

            // if it still does not exist, then log the file and send an email stating that the test failed
            if (!fileExistsEmail())
            {
                super.logFileError(file.getName());
                email.sendMail("guychill197@gmail.com", "gtarocks", "guychill197@gmail.com", "guychill197@gmail.com", "ftp failed to open", "Test result");
            }
        }
    }

    public boolean fileExistsEmail() throws IOException
    {
        ReadMail mail = new ReadMail();
        List<File> files = mail.getAttachments();

        return files.contains(new File(fileName()));
    }
}