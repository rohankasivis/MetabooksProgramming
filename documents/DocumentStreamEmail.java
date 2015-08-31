package documents;

import mockclock.Clock;

import java.io.File;
import java.io.IOException;

public class DocumentStreamEmail extends DocumentStream implements Runnable
{
    public DocumentStreamEmail(Clock time)
    {
        super(time);
    }
}