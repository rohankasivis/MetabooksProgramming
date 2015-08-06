package documentsFtp;

import java.net.UnknownHostException;

public class MockFTPClientTest
{
    public static void main(String [] args) throws UnknownHostException
    {
        MockFtpClient client = new MockFtpClient();
        client.ftpFile("Hello.txt");
        client.printFile("Hello.txt");

        client.delFile("Hello.txt");
        client.ftpFile("World.txt");
        client.printFile("World.txt");

        client.delFile("Hello.txt");
        client.getFile("Hello.txt");

        client.getFile("World.txt");
    }
}