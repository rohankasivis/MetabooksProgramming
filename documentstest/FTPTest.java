
package documentstest;

import documentsFtp.FTPClient;
import documentsFtp.FTPFiles;

import java.io.IOException;
import java.net.UnknownHostException;

public class FTPTest
{
    public static void main(String [] args) throws UnknownHostException, IOException
    {
        FTPClient client = new FTPClient();
        client.ftpFile("C:/FTPFilesTesting/8-2-2015data.txt");
        String [] fileNames = FTPFiles.getListOfFiles();
        for(int j = 0; j < fileNames.length; j++)
        {
            System.out.println(fileNames[j]);
        }
    }
}