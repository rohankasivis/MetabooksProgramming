package java.illinois.edu.Practice.generaltestingmiscellaneous;

import org.joda.time.DateTime;
import org.junit.*;

public class FooTest
{

    private Foo foo;
    private DateTime mock;

    @Before
    public void setUp()
    {
        //mock = mock(DateTime.now());
        mock = DateTime.now();
        foo = new Foo(mock);
    }

    @Test
    public void ensureDifferentValuesWhenMockIsCalled()
    {
        DateTime first = DateTime.now();                  // e.g. 12:00:00
        DateTime second = first.plusSeconds(1);          // 12:00:01
        DateTime thirdAndAfter = second.plusSeconds(1);  // 12:00:02

        //when(mock.instant()).thenReturn(first, second, thirdAndAfter);

        foo.someMethod();   // string of first
        foo.someMethod();   // string of second
        foo.someMethod();   // string of thirdAndAfter 
        foo.someMethod();   // string of thirdAndAfter 
    }
}