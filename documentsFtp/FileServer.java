package documentsFtp;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileServer
{
    // this function is used to return a list of files that exists within the FTP server
    public static String[] getListOfFiles() throws IOException
    {
        FTPClient client = new FTPClient();
        client.connect("localhost");
        client.login("rohan", "rohan123");
        boolean val = client.changeWorkingDirectory("C:/");
        String [] names = client.listNames("C:/");
        return names;
    }

    public static File getFile(String fileName) throws IOException
    {
        String [] listOfFiles = getListOfFiles();
        for(int j = 0; j < listOfFiles.length; j++)
        {
            if(listOfFiles[j].equals(fileName))
                return new File(fileName);
        }

        System.out.println("File does not exist, bad input.");
        return null;
    }
}