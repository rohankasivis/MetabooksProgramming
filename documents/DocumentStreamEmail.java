package documents;

import documentsMail.ReadMail;
import mockclock.Clock;
import org.joda.time.DateTime;

import java.io.*;
import java.util.List;

public class DocumentStreamEmail extends DocumentStream implements Runnable
{
    private PrintWriter writer;
    private Clock time;

    public DocumentStreamEmail(Clock time)
    {
        super(time);
        try
        {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("filedata.txt", true)));
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public File[] readFiles() throws IOException
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

    public void processFiles() throws InterruptedException, IOException
    {
        time.waitTill(6, 0, 0);
        readFiles();
    }
}