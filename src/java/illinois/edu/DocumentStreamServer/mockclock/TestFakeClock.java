package java.illinois.edu.DocumentStreamServer.mockclock;

// this class is responsible for testing the fake clock, in order to make sure that
// it implements the functionality of an actual clock using the correct time, and
// also makes sure that the waitTill function works properly
public class TestFakeClock
{
    public static void main(String [] args) throws InterruptedException
    {
        fakeClock clock = new fakeClock(12, 41, 1);
        Thread.sleep(10000);
        clock.waitTill(12, 30, 2);
        Thread.sleep(4000);
        System.out.println(clock.getTime());
    }
}