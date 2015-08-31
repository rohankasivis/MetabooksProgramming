package documentsMail;

import com.sun.deploy.util.StringUtils;
import com.sun.xml.internal.ws.api.message.Attachment;

import javax.mail.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReadMail
{
    private Message message;
    public void readEmail(String email, String password)
    {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect("imap.gmail.com", email, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message msg = inbox.getMessage(inbox.getMessageCount());
            message = msg;
            Address[] in = msg.getFrom();
            for (Address address : in) {
                System.out.println("FROM:" + address.toString());
            }
            Multipart mp = (Multipart) msg.getContent();
            BodyPart bp = mp.getBodyPart(0);
            System.out.println("SENT DATE:" + msg.getSentDate());
            System.out.println("SUBJECT:" + msg.getSubject());
            System.out.println("CONTENT:" + bp.getContent());
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }

    public List<File> getAttachments() throws MessagingException, IOException
    {
        List<File> attachments = new ArrayList<File>();
            Multipart multipart = (Multipart) message.getContent();
            // System.out.println(multipart.getCount());

            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if(!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()))
                {
                    continue; // dealing with attachments only
                }
                InputStream is = bodyPart.getInputStream();
                File f = new File("/tmp/" + bodyPart.getFileName());
                FileOutputStream fos = new FileOutputStream(f);
                byte[] buf = new byte[4096];
                int bytesRead;
                while((bytesRead = is.read(buf))!=-1) {
                    fos.write(buf, 0, bytesRead);
                }
                fos.close();
                attachments.add(f);
            }


        return attachments;
    }
}