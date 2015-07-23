package documentsFtp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.CopyStreamListener;

import java.io.File;
import java.io.InputStream;
import java.net.UnknownHostException;

public interface IFTPClient
{
    public void ftpFile(String fileToRead) throws UnknownHostException;
    public void printFile(InputStream input);
    public void deleteFile(FTPClient ftp);
    public CopyStreamListener createListener();
    public void delFile(File file);
}
