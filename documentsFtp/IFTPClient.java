package documentsFtp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.CopyStreamListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

public interface IFTPClient
{
    public void ftpFile(String fileToRead) throws UnknownHostException;
    public void printFile(String fileName);
    public File getFile(String fileName) throws IOException;
    public void deleteFile(FTPClient ftp);
    public CopyStreamListener createListener();
    public void delFile(String fileName);
}
