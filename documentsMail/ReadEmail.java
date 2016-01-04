package documentsMail;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ReadEmail
{
    public void readEmail(String email, String password);
    public List<File> getAttachments() throws IOException;
}
