package documentsMail;

import java.util.HashMap;
import java.util.Map;

public class mockSendEmail
{
    private Map<String, String> emails; // a map of email subjects to messages
    private Map<String, String> usernames; // a map of usernames to passwords
    private Map<String, String> recipients; // a map of senders to recipients

    public mockSendEmail()
    {
        emails = new HashMap<>();
        usernames = new HashMap<>();
        recipients = new HashMap<>();
    }

    public void sendMail(final String username, final String password, String recipient, String sender, String messageInfo, String subject)
    {
        usernames.put(username, password);
        emails.put(subject, messageInfo);
        recipients.put(sender, recipient);
    }

    public boolean emailSent(String messageInfo)
    {
        return emails.containsValue(messageInfo);
    }
}