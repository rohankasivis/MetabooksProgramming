<<<<<<< HEAD
package documentsFtp;

import org.apache.commons.net.ftp.FTPClient;
=======

package documentsFtp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
>>>>>>> ff3d78988148663c85ffc3ce26282a142fbf3e6e

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileServer
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