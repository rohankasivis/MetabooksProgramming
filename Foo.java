import org.joda.time.DateTime;
import org.joda.time.Instant;

public class Foo
{
    private final DateTime clock;
    public Foo(DateTime clock) {
        this.clock = clock;
    }

    public void someMethod() {
        int now = clock.getDayOfMonth();   // this is changed to make test easier
        System.out.println(now);   // Do something with 'now'
    }
}