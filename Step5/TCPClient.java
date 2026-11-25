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

                    if (response == null) {
                        System.err.println("Server closed the connection.");
                        break;
                    }

                System.out.println(response);
            }
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