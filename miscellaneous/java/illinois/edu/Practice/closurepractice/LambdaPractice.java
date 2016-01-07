package java.illinois.edu.Practice.closurepractice;
import java.util.function.Function;

public class LambdaPractice
{
    public void lambdaExample()
    {
        String result = function("value", String::toString);
        System.out.println(result);
    }

    public static String function(String value, Function<String, String> function)
    {
        return function.apply(value);
    }

    public static void main(String [] args)
    {
        LambdaPractice practice = new LambdaPractice();
        practice.lambdaExample();
    }
}
