package documentsMail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockEmail implements Email
{
    private Map<String, String> emails; // a map of email subjects to messages
    private Map<String, String> usernames; // a map of usernames to passwords
    private Map<String, String> recipients; // a map of senders to recipients
    private List<String> attachments;

    public MockEmail()
    {
        emails = new HashMap<>();
        usernames = new HashMap<>();
        recipients = new HashMap<>();
        attachments = new ArrayList<>();
    }

    public boolean sendMail(final String username, final String password, String recipient, String sender, String messageInfo, String subject)
    {
        usernames.put(username, password);
        emails.put(subject, messageInfo);
        recipients.put(sender, recipient);
        return true;
    }

    public boolean sendMailWithAttachment(final String username, final String password, String recipient, String sender, String messageInfo, String subject, String path, String attachment)
    {
        usernames.put(username, password);
        emails.put(subject, messageInfo);
        recipients.put(sender, recipient);
        attachments.add(attachment);
        return true;
    }

    public boolean emailSent(String messageInfo)
    {
        return emails.containsValue(messageInfo);
    }

    public Map<String, String> getEmails()
    {
        return emails;
    }

    public List<String> getAttachments()
    {
        return attachments;
    }

    public boolean containsAttachment(String attachment)
    {
        return attachments.contains(attachment);
    }
}