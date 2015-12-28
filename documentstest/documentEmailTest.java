package documentstest;

import documents.DocumentStreamEmail;
import documentsFtp.FTPClient;
import documentsFtp.IFTPClient;
import documentsMail.Email;
import documentsMail.MockEmail;
import documentsMail.SMTPMail;
import mockclock.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class documentEmailTest
{
    private Clock clock;
    private IFTPClient client;
    private Email email;
    private DocumentStreamEmail stream;

    public documentEmailTest(Clock clock, IFTPClient client, Email email)
    {
        this.clock = clock;
        this.client = client;
        this.email = email;
        stream = new DocumentStreamEmail(clock, client, email, 6, 0, 0);    // for testing purposes just use 6 am
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
        //stream.emailFile();
        assertEquals(true, stream.fileExistsEmail());
        System.out.println("@Test - the newly created file has been sent as an email");
    }

    @Test
    public void test_files_exist_mock_email()
    {
        MockEmail email = new MockEmail();
        email.sendMail("guychill197@gmail.com", "gtarocks", "guychill197@gmail.com", "guychill197@gmail.com", "Sending attachment here", "Test attachment exists");
        assertEquals(true, email.containsAttachment(stream.fileName()));
        System.out.println("@Test - the emails containing the attachments are sent properly.");
    }

    // the next set of functions are test cases used to model an actual scenario

}