package java.illinois.edu.DocumentStreamServer.documents;

import java.illinois.edu.DocumentStreamServer.documentsFtp.FTPClient;
import java.illinois.edu.DocumentStreamServer.documentsFtp.IFTPClient;
import java.illinois.edu.DocumentStreamServer.documentsMail.Email;
import java.illinois.edu.DocumentStreamServer.documentsMail.SMTPMail;
import java.illinois.edu.DocumentStreamServer.mockclock.AccurateTime;
import java.illinois.edu.DocumentStreamServer.mockclock.Clock;
import java.util.Scanner;

import java.io.IOException;

public class DocumentServer
{
    // The main class which is used to perform the sequence of actions. In this case, we will choose
    // the time of FTP to be 6 am
    // -- newly added: takes in input from the user as to the hour, minute, and second
    public static void main(String [] args) throws InterruptedException, IOException
    {
        Clock clock = new AccurateTime();
        IFTPClient client = new FTPClient();
        Email email = new SMTPMail();
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the hour at which you want the files to be fetched from FTP: ");
        int hour = input.nextInt();
        System.out.print("\nPlease enter the minute at the specified hour: ");
        int minute = input.nextInt();
        System.out.print("\nPlease enter the second at the specified minute: ");
        int second = input.nextInt();
        (new Thread(new DocumentStream(clock, client, email, hour, minute, second))).start();
    }
}