package java.illinois.edu.Practice.junittests;

import org.junit.*;

/**
 * JUnit TimeOut Test
 * @author mkyong
 *
 */
public class JunitTest4 {

    @Test(timeout = 1000)
    public void infinity() {
        while (true);
    }

}