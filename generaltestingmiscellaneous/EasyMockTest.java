package generaltestingmiscellaneous;

import org.easymock.*;
import org.easymock.samples.ClassTested;
import org.junit.Rule;
import org.junit.Test;

public class EasyMockTest extends EasyMockSupport
{
    @Rule
    public EasyMockRule rule = new EasyMockRule(this);

    @Mock
    private Collaborator collaborator; // 1

    @TestSubject
    private final ClassTested classUnderTest = new ClassTested(); // 2

    @Test
    public void addDocument() {
        collaborator.documentAdded("New Document"); // 3
        replayAll(); // 4
    }
}
