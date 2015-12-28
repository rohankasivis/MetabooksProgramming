package documents;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;

public interface Stream
{
    public boolean fileExistsFTP() throws IOException;
    public boolean fileExistsLocal(File file) throws IOException;
    public File createFile() throws FileNotFoundException, IOException;
    public void handleFile() throws IOException, InterruptedException;
    public boolean fetchFile(File file) throws IOException;
    public void ftpFile() throws UnknownHostException;
    public void logFileRead(String fileName);
    public void logFileError(String fileName);
    public void recoverState() throws IOException;
}