package java.illinois.edu.DocumentStreamServer.documents;

import java.illinois.edu.DocumentStreamServer.documentsMail.Email;
import java.illinois.edu.DocumentStreamServer.documentsMail.SMTPMail;
import java.illinois.edu.DocumentStreamServer.mockclock.AccurateTime;
import java.illinois.edu.DocumentStreamServer.mockclock.Clock;

import java.util.Scanner;

public class DocumentEmailServer
{
    public static void main(String [] args)
    {
        Clock clock = new AccurateTime();
        Email email = new SMTPMail();
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the hour at which you want the files to be fetched from email: ");
        int hour = input.nextInt();
        System.out.print("\nPlease enter the minute at the specified hour: ");
        int minute = input.nextInt();
        System.out.print("\nPlease enter the second at the specified minute: ");
        int second = input.nextInt();
        (new Thread(new DocumentStreamEmail(clock, email, hour, minute, second))).start();
    }
}