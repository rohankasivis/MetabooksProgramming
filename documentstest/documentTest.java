package documentstest;

import documents.DocumentStream;
import mockclock.*;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.io.IOException;
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
                {new AccurateTime()},
                {new DecrementTime(0, 0, 5, 10, 14, 2)},
                {new IncrementTime(0, 0, 5, 12, 29, 14)},
                {new KeepFixedTime(2014, 4, 5, 19, 0, 12)}
        });
    }

    @Before
    public void set_up() throws IOException, InterruptedException
    {
        stream.processFiles();
    }

    @Test
    public void test_ftp() throws IOException
    {
        assertEquals(true, stream.fileExists());
        System.out.println("@Test - newly created file does exist in the server");
    }

    @Test
    public void test_time()
    {
        assertEquals(true, DateTime.now().getHourOfDay() == 6);
        System.out.println("@Test - file is put into ftp at 6 a.m.");
    }
}
