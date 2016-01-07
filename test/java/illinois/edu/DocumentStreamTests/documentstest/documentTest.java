package java.illinois.edu.DocumentStreamTests.documentstest;

import java.illinois.edu.DocumentStreamServer.documents.DocumentStream;
import java.illinois.edu.DocumentStreamServer.documentsFtp.FTPClient;
import java.illinois.edu.DocumentStreamServer.documentsFtp.IFTPClient;
import java.illinois.edu.DocumentStreamServer.documentsFtp.MockFtpClient;
import java.illinois.edu.DocumentStreamServer.documentsMail.Email;
import java.illinois.edu.DocumentStreamServer.documentsMail.MockEmail;
import java.illinois.edu.DocumentStreamServer.documentsMail.SMTPMail;
import java.illinois.edu.DocumentStreamServer.mockclock.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class documentTest {
    private Clock clock;
    private IFTPClient client;
    private Email email;
    private DocumentStream stream;


    public documentTest(Clock clock, IFTPClient client, Email email) {
        this.clock = clock;
        this.client = client;
        this.email = email;
        stream = new DocumentStream(clock, client, email, 6, 0, 0);
    }

    @Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
                {new fakeClock(14, 39, 12), new FTPClient(), new SMTPMail()}
        });
    }

    // the next set of test cases are meant to check that various parts of the program work properly,
    // such as the fake clock, getting the right file name, making sure that the file can be found, etc.
    @Test
    public void test_fake_clock() throws InterruptedException {
        fakeClock testClock = new fakeClock(23, 41, 12);
        assertEquals("12:41.23", testClock.getTime());
        System.out.println("@Test - the mock clock does print the time correctly.");
        Thread.sleep(10000);
        assertEquals("12:41.33", testClock.getTime());
        System.out.println("@Test - the mock clock is able to return the time correctly at any given point.");
        testClock.waitTill(15, 12, 21);
        assertEquals("15:12.21", testClock.getTime());
        System.out.println("@Test - the mock clock's waitTill method runs correctly.");
        Thread.sleep(10000);
        assertEquals("15:12.31", testClock.getTime());
        System.out.println("@Test - the mock clock is able to return the time correctly after the waitTill method.");
    }

    @Test
    public void test_file_name() {
        assertEquals(stream.fileName(), clock.getMonthOfYear() + "-" + clock.getDayOfMonth() + "-" + clock.getYear() + "data.txt");
        System.out.println("@Test - the newly created file has the appropriate name.");
    }

    @Test
    public void test_file_exists_local() throws IOException {
        stream.createFile();
        assertEquals(true, stream.fileExistsLocal(new File(stream.fileName())));
        System.out.println("@Test - The newly created file exists within the local server.");
    }

    @Test
    public void test_file_exists_FTP() throws IOException {
        stream.createFile();
        stream.ftpFile();
        assertEquals(true, stream.fileExistsFTP());
        System.out.println("@Test - The newly created file exists within the ftp server.");
    }

    @Test
    public void check_all_files_read() throws IOException {
        stream.recoverState();
        int date[] = stream.parseLogFile();
        assertEquals(8, date[0]);
        assertEquals(4, date[1]);
        assertEquals(2015, date[2]);
        System.out.println("@Test - all of the files are being read and the date is printed properly in the output file.");
    }

    // The next few test cases are meant to check the fake FTP Client()
    @Test
    public void test_file_exists_mock() throws UnknownHostException {
        MockFtpClient client = new MockFtpClient();
        client.ftpFile("filedata.txt");
        assertEquals(true, client.fileExists("filedata.txt"));
        System.out.println("@Test - the mockftp has correctly put the file into ftp, and now it exists.");
    }

    @Test
    public void test_delete_mock() throws UnknownHostException {
        MockFtpClient client = new MockFtpClient();
        client.ftpFile("filedata.txt");
        client.delFile("filedata.txt");
        assertEquals(false, client.fileExists("filedata.txt"));
    }

    // the next few test cases are meant to check if the emails (both fake and ordinary) are working properly
    @Test
    public void check_actual_mail() {
        boolean result = email.sendMail("guychill197@gmail.com", "gtarocks", "guychill197@gmail.com", "guychill197@gmail.com", "random test", "Test result");
        assertEquals(true, result);
        System.out.println("@Test - The actual email server is sent properly.");
    }

    @Test
    public void check_fake_mail() {
        MockEmail email = new MockEmail();
        email.sendMail("guychill197@gmail.com", "gtarocks", "guychill197@gmail.com", "guychill197@gmail.com", "random test", "Test result");
        assertEquals(true, email.emailSent("ftp failed to open"));
        System.out.println("@Test - The mock email server works properly.");
    }

    // The following few tests combine the functionality of both the mock ftp and mock clock
    @Test
    public void test_at_10() throws IOException {
        IFTPClient client = new MockFtpClient();
        clock.at(9, 0, 0, (() -> {
            try {
                client.ftpFile("filedata.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        clock.at(10, 0, 0, (() -> {
            try {
                client.fileExists("filedata.txt");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        assertEquals(true, clock.getHour() == 10);
        assertEquals(true, client.fileExists("filedata.txt"));
        if (!client.fileExists("filedata.txt")) {
            MockEmail email = new MockEmail();
            email.sendMail("guychill197@gmail.com", "gtarocks", "guychill197@gmail.com", "guychill197@gmail.com", "failed to ftp", "Test result");
            assertEquals(true, email.emailSent("failed to ftp"));
        }
    }

    // The next three test cases are intended to make sure that the process of FTPing (actual FTP) the file and then
    // checking at various times follows properly.
    @Test
    public void test_at_6() throws InterruptedException, IOException {
        clock.at(5, 0, 0, (() -> {
            try {
                stream.ftpFile();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }));
        clock.at(6, 0, 0, (() -> {
            try {
                stream.fileExistsFTP();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        assertEquals(true, stream.fileExistsFTP());
        assertEquals(true, clock.getHour() == 6);
        System.out.println("@Test - The clock has successfully put the file into FTP at 6 as planned.");
    }

    @Test
    public void test_at_7() throws InterruptedException, IOException {
        if (!stream.fileExistsFTP()) {
            clock.at(7, 0, 0, (() -> {
                try {
                    stream.fileExistsFTP();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            assertEquals(true, stream.fileExistsFTP());
            assertEquals(true, clock.getHour() == 7);
            System.out.println("@Test - The clock has successfully put the file into FTP one hour later than planned.");
        } else
            System.out.println("@Test - This test need not be completed as the file is already in ftp.");
    }

    @Test
    public void test_at_8() throws InterruptedException, IOException {
        if (!stream.fileExistsFTP()) {
            clock.at(8, 0, 0, (() -> {
                try {
                    stream.fileExistsFTP();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            assertEquals(true, stream.fileExistsFTP());
            assertEquals(true, clock.getHour() == 8);
            System.out.println("@Test - The clock has successfully put the file into FTP one hour later than planned.");
        } else
            System.out.println("@Test - This test need not be completed as the file is already in ftp.");
    }

    // this is the main and final test that puts everything together
    @Test
    public void set_up() throws IOException, InterruptedException {
        stream.handleFile();
        System.out.println("@Test - This test simply runs processFiles, the key method of documentStream.");
    }
}