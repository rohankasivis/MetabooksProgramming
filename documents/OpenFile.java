package documents;

import java.util.Scanner;
import java.io.*;

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