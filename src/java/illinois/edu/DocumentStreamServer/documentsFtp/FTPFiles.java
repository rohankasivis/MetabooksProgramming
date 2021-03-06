package java.illinois.edu.DocumentStreamServer.documentsFtp;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FTPFiles
{
    public static String[] getListOfFiles() throws IOException
    {
        FTPClient client = new FTPClient();

        String [] names;
        File file = new File("C:/FTPFilesTesting/" + "8-2-2015data.txt");
        PrintWriter out = new PrintWriter(file);
        out.println("Test new file created.");
        out.close();
        client.connect("localhost");
        client.login("rohan", "rohan123");
        boolean val = client.changeWorkingDirectory("C:/");
        names = client.listNames("C:/");
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