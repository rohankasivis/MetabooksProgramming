package java.illinois.edu.Practice.generaltestingmiscellaneous;

import org.joda.time.DateTime;

public class Foo
{
    private final DateTime clock;
    public Foo(DateTime clock) {
        this.clock = clock;
    }

    public void someMethod() {
        int now = clock.getDayOfMonth();   // this is changed to make java.illinois.edu.Practice.generaltestingmiscellaneous.test easier
        System.out.println(now);   // Do something with 'now'
    }
}