package documentsFtp;/*
* This program is used to read and print out
* on the server using the apache FTP client.
* */

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.util.TrustManagerUtils;

/**
 * This is an example program demonstrating how to use the FTPClient class.
 * This program connects to an FTP server and retrieves the specified
 * file.  If the -s flag is used, it stores the local file at the FTP server.
 * Just so you can see what's happening, all reply strings are printed.
 * If the -b flag is used, a binary transfer is assumed (default is ASCII).
 * See below for further options.
 */
public final class FTPClient implements IFTPClient
{
    public static final String USAGE =
            "Usage: ftp [options] <hostname> <username> <password> [<remote file> [<local file>]]\n" +
                    "\nDefault behavior is to download a file and use ASCII transfer mode.\\n\" +\n" +
                    "                    \"\\t-a - use local active mode (default is local passive)\n" +
                    "\t-A - anonymous login (omit username and password parameters)\n" +
                    "\t-b - use binary transfer mode\n" +
                    "\t-c cmd - issue arbitrary command (remote is used as a parameter if provided) \n" +
                    "\t-d - list directory details using MLSD (remote is used as the pathname if provided)\n" +
                    "\t-e - use EPSV with IPv4 (default false)\n" +
                    "\t-f - issue FEAT command (remote and local files are ignored)\n" +
                    "\t-h - list hidden files (applies to -l and -n only)\n" +
                    "\t-k secs - use keep-alive timer (setControlKeepAliveTimeout)\n" +
                    "\t-l - list files using LIST (remote is used as the pathname if provided)\n" +
                    "\t     Files are listed twice: first in raw mode, then as the formatted parsed data.\n" +
                    "\t-L - use lenient future dates (server dates may be up to 1 day into future)\n" +
                    "\t-n - list file names using NLST (remote is used as the pathname if provided)\n" +
                    "\t-p true|false|protocol[,true|false] - use FTPSClient with the specified protocol and/or isImplicit setting\n" +
                    "\t-s - store file on server (upload)\n" +
                    "\t-t - list file details using MLST (remote is used as the pathname if provided)\n" +
                    "\t-w msec - wait time for keep-alive reply (setControlKeepAliveReplyTimeout)\n" +
                    "\t-T  all|valid|none - use one of the built-in TrustManager implementations (none = JVM default)\n" +
                    "\t-PrH server[:port] - HTTP Proxy host and optional port[80] \n" +
                    "\t-PrU user - HTTP Proxy server username\n" +
                    "\t-PrP password - HTTP Proxy server password\n" +
                    "\t-# - add hash display during transfers\n";

    public void ftpFile(String fileToRead) throws UnknownHostException
    {
        boolean storeFile = false, binaryTransfer = false, error = false, listFiles = false, listNames = false, hidden = false;
        boolean localActive = false, useEpsvWithIPv4 = false, feat = false, printHash = false;
        boolean mlst = false, mlsd = false;
        boolean lenient = false;
        long keepAliveTimeout = -1;
        int controlKeepAliveReplyTimeout = -1;
        int minParams = 5; // listings require 3 params
        String protocol = null; // SSL protocol
        String doCommand = null;
        String trustmgr = null;
        String proxyHost = null;
        int proxyPort = 80;
        String proxyUser = null;
        String proxyPassword = null;
        String username = null;
        String password = null;

        String specification = "";
        String inUsername = "";
        String inPassword = "";
        String server = "";
        String locationToPlace = "";
        String line = null;
        try {
            FileReader fileReader =
                    new FileReader("Config.txt");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null)
            {
                String[] data = line.split(":");

                if(line.startsWith("Specification: "))
                {
                    if(data[1] != null)
                        specification=data[1];

                    System.out.println(specification);
                }
                else if (line.startsWith("username:"))
                {
                    inUsername =data[1];
                    System.out.println(inUsername);
                }
                else if (line.startsWith("password:"))
                {
                    inPassword = data[1];
                    System.out.println(inPassword);
                }
                else if (line.startsWith("server:"))
                {
                    server = data[1];
                    System.out.println(server);
                }
                else if (line.startsWith("locationToPlace:"))
                {
                    locationToPlace = data[1] + ":" + data[2];
                    System.out.println(locationToPlace);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (specification.equals("-s")) {
            storeFile = true;
        } else if (specification.equals("-a")) {
            localActive = true;
        } else if (specification.equals("-A")) {
            username = "anonymous";
            password = System.getProperty("user.name") + "@" + InetAddress.getLocalHost().getHostName();
        } else if (specification.equals("-b")) {
            binaryTransfer = true;
        } else if (specification.equals("-c")) {
            doCommand = server;
            minParams = 3;
        } else if (specification.equals("-d")) {
            mlsd = true;
            minParams = 3;
        } else if (specification.equals("-e")) {
            useEpsvWithIPv4 = true;
        } else if (specification.equals("-f")) {
            feat = true;
            minParams = 3;
        } else if (specification.equals("-h")) {
            hidden = true;
        }  else if (specification.equals("-l")) {
            listFiles = true;
            minParams = 3;
        } else if (specification.equals("-L")) {
            lenient = true;
        } else if (specification.equals("-n")) {
            listNames = true;
            minParams = 3;
        } else if (specification.equals("-p")) {
            protocol = server;
        } else if (specification.equals("-t")) {
            mlst = true;
            minParams = 3;
        }
        else if (specification.equals("-PrH")) {
            proxyHost = server;
        } else if (specification.equals("-PrU")) {
            proxyUser = inUsername;
        } else if (specification.equals("-PrP")) {
            proxyPassword = inPassword;
        } else if (specification.equals("-#")) {
            printHash = true;
        }

        if (username != null) {
            minParams -= 2;
        }
        /*
        if (remain < minParams) // server, user, pass, remote, local [protocol]
        {
            System.err.println(USAGE);
            System.exit(1);
        }
        */

        int port = 0;
        String parts[] = server.split(":");
        if (parts.length == 2){
            server=parts[0];
            port=Integer.parseInt(parts[1]);
        }
        if (username == null) {
            username = inUsername;
            password = inPassword;
        }

        String remote = locationToPlace;

        String local = fileToRead;

        final org.apache.commons.net.ftp.FTPClient ftp;
        if (protocol == null ) {
            if(proxyHost !=null) {
                System.out.println("Using HTTP proxy server: " + proxyHost);
                ftp = new FTPHTTPClient(proxyHost, proxyPort, proxyUser, proxyPassword);
            }
            else {
                ftp = new org.apache.commons.net.ftp.FTPClient();
            }
        } else {
            FTPSClient ftps;
            if (protocol.equals("true")) {
                ftps = new FTPSClient(true);
            } else if (protocol.equals("false")) {
                ftps = new FTPSClient(false);
            } else {
                String prot[] = protocol.split(",");
                if (prot.length == 1) { // Just protocol
                    ftps = new FTPSClient(protocol);
                } else { // protocol,true|false
                    ftps = new FTPSClient(prot[0], Boolean.parseBoolean(prot[1]));
                }
            }
            ftp = ftps;
            if ("all".equals(trustmgr)) {
                ftps.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
            } else if ("valid".equals(trustmgr)) {
                ftps.setTrustManager(TrustManagerUtils.getValidateServerCertificateTrustManager());
            } else if ("none".equals(trustmgr)) {
                ftps.setTrustManager(null);
            }
        }

        if (printHash) {
            ftp.setCopyStreamListener(createListener());
        }
        if (keepAliveTimeout >= 0) {
            ftp.setControlKeepAliveTimeout(keepAliveTimeout);
        }
        if (controlKeepAliveReplyTimeout >= 0) {
            ftp.setControlKeepAliveReplyTimeout(controlKeepAliveReplyTimeout);
        }
        ftp.setListHiddenFiles(hidden);

        // suppress login details
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

        try
        {
            int reply;
            if (port > 0) {
                ftp.connect(server, port);
            } else {
                ftp.connect(server);
            }
            System.out.println("Connected to " + server + " on " + (port>0 ? port : ftp.getDefaultPort()));

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }
        }
        catch (IOException e)
        {
            if (ftp.isConnected())
            {
                try
                {
                    ftp.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
            System.err.println("Could not connect to server.");
            e.printStackTrace();
            System.exit(1);
        }

        __main:
        try
        {
            if (!ftp.login(username, password))
            {
                ftp.logout();
                error = true;
                break __main;
            }

            System.out.println("Remote system is " + ftp.getSystemType());

            if (binaryTransfer) {
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
            } else {
                // in theory this should not be necessary as servers should default to ASCII
                // but they don't all do so - see NET-500
                ftp.setFileType(FTP.ASCII_FILE_TYPE);
            }

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            if (localActive) {
                ftp.enterLocalActiveMode();
            } else
            {
                ftp.enterLocalPassiveMode();
            }

            InputStream input;
            input = new FileInputStream(local);
            ftp.storeFile(remote, input);
            input.close();

            ftp.setUseEPSVwithIPv4(useEpsvWithIPv4);

            if (storeFile)
            {
                InputStream inputV;

                inputV = new FileInputStream(local);

                ftp.storeFile(remote, inputV);

                inputV.close();
            }
            else if (listFiles)
            {
                if (lenient) {
                    FTPClientConfig config = new FTPClientConfig();
                    config.setLenientFutureDates(true);
                    ftp.configure(config );
                }

                for (FTPFile f : ftp.listFiles(remote)) {
                    System.out.println(f.getRawListing());
                    System.out.println(f.toFormattedString());
                }

            }
            else if (mlsd)
            {
                for (FTPFile f : ftp.mlistDir(remote)) {
                    System.out.println(f.getRawListing());
                    System.out.println(f.toFormattedString());
                }
            }
            else if (mlst)
            {
                FTPFile f = ftp.mlistFile(remote);
                if (f != null){
                    System.out.println(f.toFormattedString());
                }
            }
            else if (listNames)
            {
                for (String s : ftp.listNames(remote)) {
                    System.out.println(s);
                }
            }
            else if (feat)
            {
                // boolean feature check
                if (remote != null) { // See if the command is present
                    if (ftp.hasFeature(remote)) {
                        System.out.println("Has feature: "+remote);
                    } else {
                        if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                            System.out.println("FEAT "+remote+" was not detected");
                        } else {
                            System.out.println("Command failed: "+ftp.getReplyString());
                        }
                    }

                    // Strings feature check
                    String []features = ftp.featureValues(remote);
                    if (features != null) {
                        for(String f : features) {
                            System.out.println("FEAT "+remote+"="+f+".");
                        }
                    } else {
                        if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                            System.out.println("FEAT "+remote+" is not present");
                        } else {
                            System.out.println("Command failed: "+ftp.getReplyString());
                        }
                    }
                } else {
                    if (ftp.features()) {
//                        Command listener has already printed the output
                    } else {
                        System.out.println("Failed: "+ftp.getReplyString());
                    }
                }
            }
            else if (doCommand != null)
            {
                if (ftp.doCommand(doCommand, remote)) {
//                  Command listener has already printed the output
//                    for(String s : ftp.getReplyStrings()) {
//                        System.out.println(s);
//                    }
                } else {
                    System.out.println("Failed: "+ftp.getReplyString());
                }
            }
            else
            {
                OutputStream output;

                output = new FileOutputStream(local);

                ftp.retrieveFile(remote, output);

                output.close();
            }

            ftp.noop(); // check that control connection is working OK

            ftp.logout();
        }
        catch (FTPConnectionClosedException e)
        {
            error = true;
            System.err.println("Server closed connection.");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            error = true;
            e.printStackTrace();
        }
        finally
        {
            if (ftp.isConnected())
            {
                try
                {
                    ftp.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
        }



        System.exit(error ? 1 : 0);
    }

    // This method is used to print the file out.
    public void printFile(InputStream input)
    {
        Scanner console = new Scanner(input);
        while(console.hasNextLine())
        {
            System.out.println(console.nextLine());
        }
    }

    // This method is used to delete a specific file.
    public void deleteFile(org.apache.commons.net.ftp.FTPClient ftp)
    {
        try
        {
            ftp.deleteFile("C://eula.1036.txt");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("deletion failed");
            System.exit(1);
        }
    }

    public CopyStreamListener createListener(){
        return new CopyStreamListener(){
            private long megsTotal = 0;
            //            @Override
            public void bytesTransferred(CopyStreamEvent event) {
                bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
            }

            //            @Override
            public void bytesTransferred(long totalBytesTransferred,
                                         int bytesTransferred, long streamSize) {
                long megs = totalBytesTransferred / 1000000;
                for (long l = megsTotal; l < megs; l++) {
                    System.err.print("#");
                }
                megsTotal = megs;
            }
        };
    }

    public void delFile(File file)
    {

    }
}