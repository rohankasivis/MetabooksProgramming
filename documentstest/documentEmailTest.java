package documentstest;

import documents.DocumentStreamEmail;
import documentsFtp.FTPClient;
import documentsMail.Email;
import documentsMail.MockEmail;
import documentsMail.SMTPMail;
import mockclock.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class documentEmailTest
{
    private Clock clock;
    private Email email;
    private DocumentStreamEmail stream;

    public documentEmailTest(Clock clock, Email email)
    {
        this.clock = clock;
        this.email = email;
        stream = new DocumentStreamEmail(clock, email, 6, 0, 0);    // for testing purposes just use 6 am
    }

    @Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
                {new fakeClock(14, 39, 12), new FTPClient(), new SMTPMail()}
        });
    }

    // the next set of test cases are generic test cases created to ensure that the various functions
    // of DocumentStreamEmail are working properly
    @Test
    public void test_file_exists_email() throws IOException
    {
        stream.createFile();
        email.sendMailWithAttachment("guychill197@gmail.com", "gtarocks", "guychill197@gmail.com", "guychill197@gmail.com", "Sending attachment here", "Test attachment exists", stream.fileName(), stream.fileName());
        assertEquals(true, stream.fileExistsEmail());
        System.out.println("@Test - the newly created file has been sent as an email");
    }

    @Test
    public void test_files_exist_mock_email()
    {
        MockEmail email = new MockEmail();
        email.sendMailWithAttachment("guychill197@gmail.com", "gtarocks", "guychill197@gmail.com", "guychill197@gmail.com", "Sending attachment here", "Test attachment exists", stream.fileName(), stream.fileName());
        assertEquals(true, email.containsAttachment(stream.fileName()));
        System.out.println("@Test - the emails containing the attachments are sent properly.");
    }

    // the next set of functions are test cases used to model an actual scenario
    @Test
    public void test_at_6() throws InterruptedException, IOException {
        clock.at(5, 0, 0, (() -> {email.sendMailWithAttachment("guychill197@gmail.com", "gtarocks", "guychill197@gmail.com", "guychill197@gmail.com", "This is the email to process.", "Attachments", "FTPFilesTesting " + stream.fileName(), stream.fileName());}));
        clock.at(6, 0, 0, (() -> {try {stream.fileExistsEmail();} catch (IOException e) {e.printStackTrace();}}));

        assertEquals(true, stream.fileExistsEmail());
        assertEquals(true, clock.getHour() == 6);
        System.out.println("@Test - The clock has successfully emailed the file at 6 as planned.");
    }

    @Test
    public void test_at_7() throws InterruptedException, IOException {
        if (!stream.fileExistsFTP()) {
            clock.at(7, 0, 0, (() -> {try {stream.fileExistsEmail();} catch (IOException e) {e.printStackTrace();}}));
            assertEquals(true, stream.fileExistsEmail());
            assertEquals(true, clock.getHour() == 7);
            System.out.println("@Test - The clock has successfully emailed the file one hour later than planned.");
        } else
            System.out.println("@Test - This test need not be completed as the file has already been emailed.");
    }

    @Test
    public void test_at_8() throws InterruptedException, IOException {
        if (!stream.fileExistsFTP()) {
            clock.at(8, 0, 0, (() -> {try {stream.fileExistsEmail();} catch (IOException e) {e.printStackTrace();}}));
            assertEquals(true, stream.fileExistsEmail());
            assertEquals(true, clock.getHour() == 8);
            System.out.println("@Test - The clock has successfully emailed the file two hours two hours later than planned.");
        } else
            System.out.println("@Test - This test need not be completed as the file has already been emailed.");
    }

    // this is the main and final test that puts everything together
    @Test
    public void set_up() throws IOException, InterruptedException {
        stream.handleFile();
        System.out.println("@Test - This test simply runs processFiles, the key method of documentStream.");
    }
}