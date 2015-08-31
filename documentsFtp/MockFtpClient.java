
package documentsFtp;

import OpenFile.OpenFile;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.CopyStreamListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MockFtpClient implements IFTPClient
{
    private Map<String, File> fileNames;

    public MockFtpClient()
    {
        fileNames = new HashMap<>();
    }

    public void ftpFile(String fileToRead) throws UnknownHostException
    {
        File file = new File("C:/input/" + fileToRead);
        fileNames.put(fileToRead, file);
    }

    // method stubbed here not needed for mockftp
    public void printFile(String fileName)
    {
        File file = fileNames.get(fileName);
        Scanner inFile = OpenFile.openToRead("C:/input/" + fileName);
        while (inFile.hasNextLine())
        {
            System.out.println(inFile.nextLine());
        }
    }

    public boolean fileExists(String fileName)
    {
        return fileNames.containsKey(fileName);
    }

    public File getFile(String fileName) throws IOException
    {
        if(!fileNames.containsKey(fileName))
        {
            System.out.println("This is bad input. The file does not exist within the directory anymore.");
            return null;
        }
        return fileNames.get(fileName);
    }

    public void delFile(String fileName)
    {
        if(!fileNames.containsKey(fileName))
            System.out.println("This is bad input. The file has already been deleted.");
        else
            fileNames.remove(fileName);
    }

    public void deleteFile(FTPClient ftp)
    {

    }

    public CopyStreamListener createListener()
    {
        return null;
    }
}