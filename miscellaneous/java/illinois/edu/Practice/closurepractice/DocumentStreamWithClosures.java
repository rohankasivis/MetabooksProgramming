package java.illinois.edu.Practice.closurepractice;

import java.illinois.edu.DocumentStreamServer.documents.DocumentStream;

import java.util.function.Consumer;
import java.util.function.Function;

public class DocumentStreamWithClosures
{
    public static String function(DocumentStream value, Function<DocumentStream, String> function)
    {
        return function.apply(value);
    }

    public static void functionWithoutReturn(DocumentStream value, Consumer<DocumentStream> function)
    {
        function.accept(value);
    }

    public static void main(String [] args)
    {
        DocumentStreamWithClosures closures = new DocumentStreamWithClosures();
    }
}