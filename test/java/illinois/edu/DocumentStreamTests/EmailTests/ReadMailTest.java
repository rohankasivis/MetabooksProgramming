package java.illinois.edu.DocumentStreamTests.EmailTests;

import java.illinois.edu.DocumentStreamServer.documentsMail.ReadMail;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReadMailTest
{
    public static void main(String [] args) throws MessagingException, IOException
    {
        ReadMail mail = new ReadMail();
        mail.readEmail("guychill197@gmail.com", "gtarocks");
        List<File> attachments = mail.getAttachments();
        for(int j = 0; j < attachments.size(); j++)
        {
            File file = attachments.get(j);
            System.out.println(file.getName());
        }
    }
}