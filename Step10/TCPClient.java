import java.io.OutputStream;
import java.util.Arrays;
import java.net.ConnectException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * TCP client (TP 3, Step 4).
 * Sends lines of text to the server and displays
 * the received responses.
 */
public class TCPClient {

    /** Address of the server. */
    private final InetAddress serverAddress;

    /** Port of the server. */
    private final int serverPort;

    /** Creates a client towards the given server. */
    public TCPClient(String host, int port)
            throws UnknownHostException {

        this.serverAddress = InetAddress.getByName(host);
        this.serverPort = port;
    }

    /** Starts the TCP client. */
    public void start() throws IOException {

    try (Socket socket = new Socket(serverAddress, serverPort)) {

        System.out.println("Connected to "
                + serverAddress.getHostAddress()
                + ":" + serverPort);

        OutputStream output = socket.getOutputStream();

        // Test 1: Several consecutive writes
        output.write("Hello".getBytes(StandardCharsets.UTF_8));
        output.write("World".getBytes(StandardCharsets.UTF_8));
        output.write("TCP".getBytes(StandardCharsets.UTF_8));

        output.flush();

        System.out.println("Test 1: 13 bytes sent in 3 writes.");

        // Separate the two experiments
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        // Test 2: One large write
        byte[] data = new byte[10000];

        Arrays.fill(data, (byte) 'A');

        output.write(data);
        output.flush();

        System.out.println("Test 2: 10000 bytes sent in 1 write.");
    }

    System.out.println("Client stopped.");
    }
    /** Starts the client from the command line. */
    public static void main(String[] args) {

    if (args.length < 2) {
        System.err.println(
                "Usage: java TCPClient <host> <port>");
        return;
    }

    try {
        TCPClient client = new TCPClient(
                args[0], Integer.parseInt(args[1]));

        client.start();

    } catch (UnknownHostException e) {
        System.err.println("Unknown host: " + args[0]);

    } catch (ConnectException e) {
        System.err.println(
                "Cannot connect: server is not running or unreachable.");

    } catch (IllegalArgumentException e) {
        System.err.println("Invalid port: " + args[1]);

    } catch (IOException e) {
        System.err.println("Connection error: " + e.getMessage());
    }
    }
}