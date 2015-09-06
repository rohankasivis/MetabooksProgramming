package documents;

import documentsFtp.FTPClient;
import documentsFtp.IFTPClient;
import documentsMail.Email;
import documentsMail.SMTPMail;
import mockclock.AccurateTime;
import mockclock.Clock;
import org.joda.time.*;

import java.io.IOException;
import java.net.UnknownHostException;

public class DocumentServer
{
    public static void main(String [] args) throws InterruptedException, UnknownHostException, IOException
    {
        Clock clock = new AccurateTime();
        IFTPClient client = new FTPClient();
        Email email = new SMTPMail();
        (new Thread(new DocumentStream(clock, client, email))).start();
    }
}