package clockpractice;

public class TimeTester
{
    public static void main(String [] args) throws InterruptedException
    {
        (new Thread(new TimePrinter(3))).start();
        (new Thread(new TimePrinter(5))).start();
        (new Thread(new TimePrinter(7))).start();
    }
}