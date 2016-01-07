package java.illinois.edu.DocumentStreamServer.OpenFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class OpenFile
{
    public static Scanner openToRead(String inputFileName)
    {
        Scanner input = new Scanner(System.in);
        File file = new File(inputFileName);

        try
        {
            input = new Scanner(file);
        }catch (FileNotFoundException e)
        {
            System.out.println("File not found.");
            System.exit(1);
        }

        return input;
    }

    public static PrintWriter openToWrite(String outputFileName)
    {
        PrintWriter outFile = null;
        try
        {
            outFile = new PrintWriter(new File(outputFileName));
        }catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return outFile;
    }
}
