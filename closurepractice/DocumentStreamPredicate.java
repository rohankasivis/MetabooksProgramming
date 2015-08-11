package closurepractice;

import documents.DocumentStream;
import mockclock.AccurateTime;

import java.util.function.Predicate;

public class DocumentStreamPredicate
{
    public static void function(DocumentStream stream, Predicate predicate)
    {

    }

    public static void main(String [] args)
    {
        DocumentStream stream = new DocumentStream(new AccurateTime());

    }
}
