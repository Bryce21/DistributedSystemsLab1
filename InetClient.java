import java.io.*;
import java.net.*;

public class InetClient {
    public static void main(String args[]) {
        // read argument from user to set ServerName. If not specified use localhost
        String serverName;
        if (args.length < 1) {
            serverName = "localhost";
        } else {
            serverName = args[0];
        }

        System.out.println("B. Reinhard Inet Client, 1.8\n");
        System.out.println("Using Server: " + serverName + ", Port: 1565");
        // Makes an input reader
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            String name;
            do {
                System.out.print("Enter a hostname or an IP address, (quit to end): ");
                System.out.flush();
                // read in name to send to server
                name = in.readLine();
                // if it doesn't equal quit call getRemoteAddress
                if (name.indexOf("quit") < 0) {
                    getRemoteAddress(name, serverName);
                }
            } while (name.indexOf("quit") < 0);
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    // pretty print method
    static String toText(byte ip[]) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < ip.length; ++i) {
            if (i > 0)
                result.append(".");
            result.append(0xff & ip[i]);
        }
        return result.toString();
    }

    static void getRemoteAddress(String name, String serverName) {
        // initialize variables. Sock will be established socket connection
        // BufferedRead to get data from server - made from socket stream
        // Stream to send data to server - made from socket
        Socket sock;
        BufferedReader fromServer;
        PrintStream toServer;
        String textFromServer;

        try {
            // Socket pointing to serverName: 1565
            sock = new Socket(serverName, 1565);
            // Stream reader to read response from server. Using established socket
            fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            // Stream writer pointing to server
            toServer = new PrintStream(sock.getOutputStream());
            // Write the name to lookup
            toServer.println(name);
            // flush the stream - no values inside the stream
            toServer.flush();
            // read three lines from the server to match the three lines of expected.
            // print response out
            // response:
            // Looking up name: google.com
            // Host name: google.com
            // Host IP: 555.555.555.555

            for (int i = 1; i <= 3; i++) {
                textFromServer = fromServer.readLine();
                if (textFromServer != null) {
                    System.out.println(textFromServer);
                }
            }
            // Close the socket - request is done
            sock.close();
        } catch (IOException x) {
            System.out.println("Socket error.");
            x.printStackTrace();
        }
    }
}