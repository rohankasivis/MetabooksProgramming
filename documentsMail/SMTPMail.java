package documentsMail;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SMTPMail implements Email
{
    public boolean sendMail(final String username, final String password, String recipient, String sender, String messageInfo, String subject)
    {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(messageInfo);

            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", username, password);
            System.out.println("Transport: "+transport.toString());
            transport.sendMessage(message, message.getAllRecipients());
            // Transport.send(message);

            return true;

        } catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
    }

    public boolean sendMailWithAttachment(final String username, final String password, String recipient, String sender, String messageInfo, String subject, String path, String attachment)
    {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(messageInfo);

            // newly added to perform sending files
            Multipart multipart = new MimeMultipart();

            DataSource source = new FileDataSource(path);
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(attachment);

            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            // -----------------------------------------

            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", username, password);
            System.out.println("Transport: "+transport.toString());
            transport.sendMessage(message, message.getAllRecipients());
            // Transport.send(message);

            return true;

        } catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
    }

    // todo - implement
    public boolean emailSent(String messageInfo)
    {
        return true;
    }
}