package junittests;

import org.junit.*;

/**
 * JUnit Expected Exception Test
 * @author mkyong
 *
 */
public class JunitTest2 {

    @Test(expected = ArithmeticException.class)
    public void divisionWithException() {
        int i = 1/0;
    }

}