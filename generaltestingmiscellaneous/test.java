package generaltestingmiscellaneous;

public class test
{
    public static void main(String [] args)
    {
        String lastDateRead = "Last time read: 5-12-2014";
        lastDateRead = lastDateRead.substring(16);
        String values[] = new String[3];
        values = lastDateRead.split("\\-");
        int month = Integer.parseInt(values[0]);
        int day = Integer.parseInt(values[1]);
        int year = Integer.parseInt(values[2]);

        System.out.println("month: " + month);
        System.out.println("day: " + day);
        System.out.println("year: " + year);
    }
}
