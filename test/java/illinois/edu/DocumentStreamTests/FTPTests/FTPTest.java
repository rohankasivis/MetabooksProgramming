package java.illinois.edu.DocumentStreamTests.FTPTests;

import java.illinois.edu.DocumentStreamServer.documentsFtp.MockFtpClient;

import java.net.UnknownHostException;

public class FTPTest
{
    public static void main(String [] args) throws UnknownHostException
    {
        MockFtpClient client = new MockFtpClient();
        client.ftpFile("eula.1036.txt");
    }
}
