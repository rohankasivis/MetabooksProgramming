package documentsFtp;

import OpenFile.OpenFile;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.CopyStreamListener;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MockFtpClient implements IFTPClient
{
    public void ftpFile(String fileToRead) throws UnknownHostException
    {
        Scanner console = OpenFile.openToRead("C:/input/" + fileToRead);
        PrintWriter outFile = OpenFile.openToWrite("C:/COut/out.txt");
        while(console.hasNext())
        {
            outFile.println(console.next());
        }

        console.close();
        outFile.close();
    }

    // method stubbed here not needed for mockftp
    public void printFile(InputStream input)
    {

    }

    // method stubbed here not needed for mockftp
    public void deleteFile(FTPClient ftp)
    {

    }

    public void delFile(File file)
    {
        file.delete();
    }

    public CopyStreamListener createListener()
    {
        return null;
    }
}