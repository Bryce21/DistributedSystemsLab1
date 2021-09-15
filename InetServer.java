import java.io.*;
import java.net.*;

public class InetServer {
    public static void main(String a[]) throws IOException {
        int q_len = 6;
        // port the server will listen on. Should probably be some sort of param/env
        // variable both server/client read from
        int port = 1565;
        Socket sock;

        // ServerSocket passively waiting on the specified port
        ServerSocket servsock = new ServerSocket(port, q_len);

        System.out.println("B. Reinhard's Inet server 1.8 starting up, listening at port 1565\n");

        /*
         * Start an infinite loop to wait for connections.
         */
        while (true) {
            // ServerSocket is actively hit and creates a socket from the client request.
            // Pass that to worker
            sock = servsock.accept();
            new Worker(sock).start();
        }
        /*
         * Workflow: 1) Start listening for requests on specified port 2) Client
         * establishes Socket connection to server 3) Start a worker thread to handle
         * request/connection, initializing worker with socket 4) Go to 1
         */
    }
}

class Worker extends Thread {
    Socket sock;

    // constructor to take Socket
    // initialize the class variable sock: Socket to passed in Socket
    Worker(Socket s) {
        sock = s;
    }

    // since Worker extends thread it must implement run method. Point of entry
    public void run() {
        // PrintStream to add pretty print output from socket
        PrintStream out = null;
        // BufferedReader to read from socket
        BufferedReader in = null;
        try {
            // Initialize the in read from the socket input stream
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            // Initialize the output stream using the socket input stream
            out = new PrintStream(sock.getOutputStream());
            try {
                String name;
                // read the host/ip address from the client request
                name = in.readLine();
                System.out.println("Looking up: " + name);
                printRemoteAddress(name, out);
            } catch (IOException x) {
                System.out.println("Server read error");
            }
            sock.close();
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    static void printRemoteAddress(String name, PrintStream out) {
        try {
            out.println("Looking up name: " + name);
            // Looks up ip address using hostname passed in from client.
            InetAddress machine = InetAddress.getByName(name);
            out.println("Host name: " + machine.getHostName());
            out.println("Host IP: " + toText(machine.getAddress()));
        } catch (UnknownHostException ex) {
            out.println("Failed in attep to look up:" + name);
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
}
