package java.illinois.edu.Practice.generaltestingmiscellaneous;

import org.easymock.tests2.AnswerTest;

public class MyClass
{
    private final AFactory aFactory;

    public MyClass(AFactory aFactory) {
        this.aFactory = aFactory;
    }

    public void doSomething() {
        AnswerTest.A a = aFactory.create(100, 101);
        // do something with the A ...
    }
}