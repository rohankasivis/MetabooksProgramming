package mailutilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.io.Util;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SMTPSClient;
import org.apache.commons.net.smtp.SimpleSMTPHeader;

/***
 * This is an example program using the SMTP package to send a message
 * to the specified recipients.  It prompts you for header information and
 * a filename containing the message.
 * <p>
 ***/

public final class SMTPPractice
{
    public static void main(String[] args)
    {
        String sender, recipient, subject, filename, server, cc;
        List<String> ccList = new ArrayList<String>();
        BufferedReader stdin;
        FileReader fileReader = null;
        Writer writer;
        SimpleSMTPHeader header;
        SMTPClient client;

        if (args.length < 1)
        {
            System.err.println("Usage: mail smtpserver");
            System.exit(1);
        }

        server = args[0];

        stdin = new BufferedReader(new InputStreamReader(System.in));

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        try
        {
            System.out.print("From: ");
            System.out.flush();

            sender = stdin.readLine();

            System.out.print("To: ");
            System.out.flush();

            recipient = stdin.readLine();

            System.out.print("Subject: ");
            System.out.flush();

            subject = stdin.readLine();

            header = new SimpleSMTPHeader(sender, recipient, subject);


            while (true)
            {
                System.out.print("CC <enter one address per line, hit enter to end>: ");
                System.out.flush();

                cc = stdin.readLine();

                if (cc== null || cc.length() == 0) {
                    break;
                }

                header.addCC(cc.trim());
                ccList.add(cc.trim());
            }

            System.out.print("Filename: ");
            System.out.flush();

            filename = stdin.readLine();
            if(!(filename.equals(""))) {
                try {
                    fileReader = new FileReader(filename);
                } catch (FileNotFoundException e) {
                    System.err.println("File not found. " + e.getMessage());
                }
            }

            client = new SMTPSClient();
            client.setDefaultPort(465);
            client.addProtocolCommandListener(new PrintCommandListener(
                    new PrintWriter(System.out), true));

            client.connect(server);

            if (!SMTPReply.isPositiveCompletion(client.getReplyCode()))
            {
                client.disconnect();
                System.err.println("SMTP server refused connection.");
                System.exit(1);
            }

            client.login();

            client.setSender(sender);
            client.addRecipient(recipient);

            for (String recpt : ccList) {
                client.addRecipient(recpt);
            }

            writer = client.sendMessageData();

            if (writer != null)
            {
                writer.write(header.toString());
                Util.copyReader(fileReader, writer);
                writer.close();
                client.completePendingCommand();
            }

            if (fileReader != null ) {
                fileReader.close();
            }

            client.logout();

            client.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
}