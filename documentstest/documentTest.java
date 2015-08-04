package documentstest;

import documents.DocumentStream;
import documentsMail.SMTPMail;
import mockclock.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class documentTest
{
    private Clock clock;
    private DocumentStream stream;

    public documentTest(Clock clock)
    {
        this.clock = clock;
        stream = new DocumentStream(clock);
    }

    @Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
                {new fakeClock(14, 39, 12)}
        });
    }

    // the next set of test cases are meant to check that various parts of the program work properly,
    // such as the fake clock, getting the right file name, making sure that the file can be found, etc.
    @Test
    public void test_fake_clock() throws InterruptedException
    {
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
    public void test_file_name()
    {
        assertEquals(stream.fileName(), clock.getMonthOfYear() + "-" + clock.getDayOfMonth() + "-" + clock.getYear() + "data.txt");
        System.out.println("@Test - the newly created file has the appropriate name.");
    }

    @Test
    public void test_file_exists_local() throws IOException
    {
        stream.createFile();
        assertEquals(true, stream.fileExistsLocal());
        System.out.println("@Test - The newly created file exists within the local server.");
    }

    @Test
    public void test_file_exists_FTP() throws IOException
    {
        stream.createFile();
        stream.ftpFile();
        assertEquals(true, stream.fileExistsFTP());
        System.out.println("@Test - The newly created file exists within the ftp server.");
    }

    @Test
    public void test_mail()
    {
        SMTPMail.sendMail("guychill197@gmail.com", "gtarocks", "guychill197@gmail.com", "guychill197@gmail.com", "ftp failed to open", "Test result");
        System.out.println("@Test - emails can be send properly");
    }

    @Test
    public void check_all_files_read() throws IOException
    {
        stream.readFiles();
        int date [] = stream.parseLogFile();
        assertEquals(8, date[0]);
        assertEquals(4, date[1]);
        assertEquals(2015, date[2]);
        System.out.println("@Test - all of the files are being read and the date is printed properly in the output file.");
    }

    // The next three test cases are intended to make sure that the process of FTPing the file and then
    // checking at various times follows properly.
    @Test
    public void test_at_6() throws InterruptedException, IOException
    {
        clock.setDocumentStream(stream);
        clock.at(5, 0, "ftpFile");
        clock.at(6, 0, "check if in ftp");
        assertEquals(true, stream.fileExistsFTP());
        assertEquals(true, clock.getHour() == 6);
        System.out.println("@Test - The clock has successfully put the file into FTP at 6 as planned.");
    }

    @Test
    public void test_at_7() throws InterruptedException, IOException
    {
        if(!stream.fileExistsFTP())
        {
            clock.at(7, 0, "check if in ftp");
            assertEquals(true, stream.fileExistsFTP());
            assertEquals(true, clock.getHour() == 7);
            System.out.println("@Test - The clock has successfully put the file into FTP one hour later than planned.");
        }
        else
            System.out.println("@Test - This test need not be completed as the file is already in ftp.");
    }

    @Test
    public void test_at_8() throws InterruptedException, IOException
    {
        if(!stream.fileExistsFTP())
        {
            clock.at(8, 0, "check if in ftp - otherwise send email");
            assertEquals(true, stream.fileExistsFTP());
            assertEquals(true, clock.getHour() == 8);
            System.out.println("@Test - The clock has successfully put the file into FTP one hour later than planned.");
        }
        else
            System.out.println("@Test - This test need not be completed as the file is already in ftp.");
    }

    // this is the main and final test that puts everything together
    @Test
    public void set_up() throws IOException, InterruptedException
    {
        stream.processFiles();
        System.out.println("@Test - This test simply runs processFiles, the key method of documentStream.");
    }
}