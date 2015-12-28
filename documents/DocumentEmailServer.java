package documents;

import documentsFtp.FTPClient;
import documentsFtp.IFTPClient;
import documentsMail.Email;
import documentsMail.SMTPMail;
import mockclock.AccurateTime;
import mockclock.Clock;

import java.util.Scanner;

public class DocumentEmailServer
{
    public static void main(String [] args)
    {
        Clock clock = new AccurateTime();
        IFTPClient client = new FTPClient();
        Email email = new SMTPMail();
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the hour at which you want the files to be fetched from email: ");
        int hour = input.nextInt();
        System.out.print("\nPlease enter the minute at the specified hour: ");
        int minute = input.nextInt();
        System.out.print("\nPlease enter the second at the specified minute: ");
        int second = input.nextInt();
        (new Thread(new DocumentStreamEmail(clock, client, email, hour, minute, second))).start();
    }
}