
package documentstest;

import documentsFtp.FTPClient;
import documentsFtp.FileServer;

import java.io.IOException;
import java.net.UnknownHostException;

public class FTPTest
{
    public static void main(String [] args) throws UnknownHostException, IOException
    {
        FTPClient client = new FTPClient();
        client.ftpFile("C:/FTPFilesTesting/8-2-2015data.txt");
        String [] fileNames = FileServer.getListOfFiles();
        for(int j = 0; j < fileNames.length; j++)
        {
            System.out.println(fileNames[j]);
        }
    }
}