
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
 * TCP client (TP 4, Step 11).
 * <p>
 * Sends lines of text to the server and displays
 * the received responses using UTF-8 encoding.
 * Handles connection errors, invalid ports, unknown hosts
 * and server disconnections.
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
     * The client stops when the user enters /quit, the standard input
     * reaches its end or the server closes the connection.
     *
     * @throws IOException if an I/O error occurs during connection
     *                     or communication with the server
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

                writer.println(line);

                String response = reader.readLine();

                if (response == null) {
                    System.err.println("Server closed the connection.");
                    break;
                }

                System.out.println(response);
            }
        }

        System.out.println("Client stopped.");
    }

    /**
     * Entry point of the TCP client application.
     * <p>
     * Creates and starts a client using the hostname and port
     * provided as command-line arguments.
     * Handles unknown hosts, connection failures, invalid ports
     * and other I/O errors.
     *
     * @param args command-line arguments containing the hostname
     *             and port number
     */
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
