package documentsMail;

public interface Email
{
    public boolean sendMail(final String username, final String password, String recipient, String sender, String messageInfo, String subject);
    public boolean sendMailWithAttachment(final String username, final String password, String recipient, String sender, String messageInfo, String subject, String path, String attachment);
    public boolean emailSent(String messageInfo);
}
