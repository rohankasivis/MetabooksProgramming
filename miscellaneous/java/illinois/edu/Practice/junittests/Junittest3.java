package java.illinois.edu.Practice.junittests;

import org.junit.*;

/**
 * JUnit Ignore Test
 * @author mkyong
 *
 */
public class Junittest3
{

    @Ignore("Not Ready to Run")
    @Test
    public void divisionWithException() {
        System.out.println("Method is not ready yet");
    }

}