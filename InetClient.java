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
        Socket sock;
        BufferedReader fromServer;
        PrintStream toServer;
        String textFromServer;

        try {
            sock = new Socket(serverName, 1565);
            fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            toServer = new PrintStream(sock.getOutputStream());
            toServer.println(name);
            toServer.flush();
            for (int i = 1; i <= 3; i++) {
                textFromServer = fromServer.readLine();
                if (textFromServer != null) {
                    System.out.println(textFromServer);
                }
            }
            sock.close();
        } catch (IOException x) {
            System.out.println("Socket error.");
            x.printStackTrace();
        }
    }
}