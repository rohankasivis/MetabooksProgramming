
package documents;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;

public interface Stream
{
    public boolean fileExistsFTP() throws IOException;
    public boolean fileExistsLocal() throws IOException;
    public String fileName();
    public void processFiles() throws InterruptedException, IOException;
    public void createFile() throws FileNotFoundException;
    public void handleFile() throws IOException, InterruptedException;
    public boolean hasReadFile(DateTime time);
    public void readFile(File file) throws IOException;
    public void ftpFile() throws UnknownHostException;
    public void logFile(String fileName);
}