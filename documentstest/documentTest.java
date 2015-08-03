package documentstest;

import documents.DocumentStream;
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

    @Test
    public void test_fake_clock() throws InterruptedException
    {
        fakeClock testClock = new fakeClock(23, 41, 12);
        assertEquals(testClock.getTime(), "12:41.23");
        System.out.println("@Test - the mock clock does print the time correctly.");
        Thread.sleep(10000);
        assertEquals(testClock.getTime(), "12:41.33");
        System.out.println("@Test - the mock clock is able to return the time correctly at any given point.");
        testClock.waitTill(15, 12, 21);
        assertEquals(testClock.getTime(), "15:12.21");
        System.out.println("@Test - the mock clock's waitTill method runs correctly.");
        Thread.sleep(10000);
        assertEquals(testClock.getTime(), "15:12.31");
        System.out.println("@Test - the mock clock is able to return the time correctly after the waitTill method.");
    }

    @Test
    public void test_file_name()
    {
        assertEquals(stream.fileName(), clock.getMonthOfYear() + "-" + clock.getDayOfMonth() + "-" + clock.getYear() + "data.txt");
        System.out.println("@Test - the newly created file has the appropriate name.");
    }

    @Test
    public void test_file_exists_local() throws UnknownHostException, IOException
    {
        stream.createFile();
        assertEquals(true, stream.fileExistsLocal());
        System.out.println("@Test - The newly created file exists within the local server.");
    }

    @Test
    public void test_file_exists_FTP() throws UnknownHostException, IOException
    {
        stream.createFile();
        stream.ftpFile();
        assertEquals(true, stream.fileExistsFTP());
        System.out.println("@Test - The newly created file exists within the ftp server.");
    }

    /*
    @Test
    public void set_up() throws IOException, InterruptedException
    {
        stream.processFiles();
        assertEquals(true, stream.fileExistsFTP());
        System.out.println("@Test - newly created file does exist in the server");
        assertEquals(true, clock.getHour() == 6);
        System.out.println("@Test - file is put into ftp at 6 a.m.");
    }
    */
}