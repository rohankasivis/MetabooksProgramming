package closurepractice;

public class Repeat
{
    public static void main(String [] args)
    {
        repeatMessage("Hello", 1000);
    }

    public static void repeatMessage(String text, int count)
    {
        Runnable r = () -> {
            for (int i = 0; i < count; i++) {
                System.out.println(text);
                Thread.yield();
            }
        };
        new Thread(r).start();
    }
}
