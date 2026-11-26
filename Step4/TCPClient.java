
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * TCP client (TP 4, Step 4).
 * <p>
 * Sends lines of text to the server and displays
 * the received responses using UTF-8 encoding.
 * The client can be stopped using the /quit command.
 */
public class TCPClient {

    /** Address of the server. */
    private final InetAddress serverAddress;

    /** Port of the server. */
    private final int serverPort;

    /**
     * Creates a TCP client for the specified server.
     *
     * @param host the server hostname or IP address
     * @param port the server TCP port
     * @throws UnknownHostException if the hostname cannot be resolved
     */
    public TCPClient(String host, int port)
            throws UnknownHostException {

        this.serverAddress = InetAddress.getByName(host);
        this.serverPort = port;
    }

    /**
     * Starts the TCP client and establishes a connection to the server.
     * <p>
     * Reads messages from the standard input, sends them to the server
     * and displays the received responses.
     * The connection is closed when the user enters /quit
     * or when the standard input reaches its end.
     *
     * @throws IOException if an I/O error occurs during communication
     */
    public void start() throws IOException {

        try (Socket socket = new Socket(serverAddress, serverPort);

             BufferedReader in = new BufferedReader(
                     new InputStreamReader(
                             System.in, StandardCharsets.UTF_8));

             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(
                             socket.getInputStream(),
                             StandardCharsets.UTF_8));

             PrintWriter writer = new PrintWriter(
                     socket.getOutputStream(),
                     true,
                     StandardCharsets.UTF_8)) {

            System.out.println("Connected to "
                    + serverAddress.getHostAddress()
                    + ":" + serverPort);

            System.out.println("Type /quit to stop the client.");

            String line;

            while ((line = in.readLine()) != null) {

                if (line.equals("/quit")) {
                    break;
                }

                // Send the message to the server
                writer.println(line);

                // Receive the server response
                String response = reader.readLine();

                System.out.println(response);
            }
        }

        System.out.println("Client stopped.");
    }

    /**
     * Entry point of the TCP client application.
     * Creates and starts a client using the hostname
     * and port provided as command-line arguments.
     *
     * @param args command-line arguments containing the hostname
     *             and port number
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {

        if (args.length < 2) {
            System.err.println(
                    "Usage: java TCPClient <host> <port>");
            return;
        }

        TCPClient client = new TCPClient(
                args[0], Integer.parseInt(args[1]));

        client.start();
    }
}
