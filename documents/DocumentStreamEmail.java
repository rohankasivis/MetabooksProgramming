package documents;

import documentsFtp.IFTPClient;
import documentsMail.Email;
import documentsMail.ReadMail;
import mockclock.Clock;
import org.joda.time.DateTime;

import java.io.*;
import java.net.UnknownHostException;
import java.util.List;

public class DocumentStreamEmail extends DocumentStream implements Runnable
{
    private PrintWriter writer;
    private Clock time;
    private IFTPClient client;
    private Email email;
    private File[] listOfFiles;

    public DocumentStreamEmail(Clock time, IFTPClient client, Email email)
    {
        super(time, client, email, 6, 0, 0);
        try
        {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("filedata.txt", true)));
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public File[] recoverState() throws IOException
    {
        ReadMail mail = new ReadMail();
        List<File> files = mail.getAttachments();
        File [] file = new File[files.size()];
        for(int j = 0; j < files.size(); j++)
        {
            file[j] = files.get(j);
        }

        writer.println("Last time read: " + DateTime.now().getMonthOfYear() + "-" + DateTime.now().getDayOfMonth() + "-" + DateTime.now().getYear());

        return file;
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

    public void createFile()
    {

    }

    // overriding the original handleFile() method in order to process emails differently
    public void handleFile() throws IOException, InterruptedException
    {
        time.waitTill(6, 0, 0);
        createFile();
        email.sendMail("guychill197@gmail.com", "gtarocks", "guychill197@gmail.com", "guychill197@gmail.com", "This is the email to process.", "Attachments");
        listOfFiles = recoverState();

        if(fileExistsEmail())
            readFile(new File("C:/FTPFilesTesting/" + fileName()));
        else
        {
            // otherwise, wait for 3 hours and see if the file will be updated by then
            int numTimesChecked = 1;
            while (numTimesChecked <= 3 && !fileExistsEmail())
            {
                if (fileExistsEmail())
                {
                    readFile(new File("C:/FTPFilesTesting/" + fileName()));
                    return;
                }
                else
                    time.waitTill(time.getHour() + 1, time.getMinutes(), time.getSeconds());
                numTimesChecked++;
            }

            // if it still does not exist, then log the file and send an email stating that the test failed
            if (!fileExistsEmail())
            {
                logFile(fileName());
                email.sendMail("guychill197@gmail.com", "gtarocks", "guychill197@gmail.com", "guychill197@gmail.com", "ftp failed to open", "Test result");
            }
        }
    }

    public boolean fileExistsEmail() throws IOException
    {
        File [] files = recoverState();
        for(int j = 0; j < files.length; j++)
        {
            if(files[j].getName().equals(fileName()))
                return true;
        }

        return false;
    }
}