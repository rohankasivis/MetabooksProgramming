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
                {new fakeClock(14, 39, 12)}
        });
    }

    @Test
    public void set_up() throws IOException, InterruptedException
    {
        stream.processFiles();
        assertEquals(true, stream.fileExists());
        System.out.println("@Test - newly created file does exist in the server");
        clock.waitTill(6, 0, 0);
        assertEquals(true, clock.getHour() == 6);
        System.out.println("@Test - file is put into ftp at 6 a.m.");
    }

    @Test
    public void test_ftp() throws IOException
    {

    }

    @Test
    public void test_time()
    {

    }
}
