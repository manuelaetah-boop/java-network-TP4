import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * Simple UDP client (TP 2, Steps 3 to 9).
 * <p>
 * Reads lines of text from the standard input and sends each one, encoded
 * in UTF-8 in a separate datagram to the server given on the command line.
 * The client stops when the standard input is closed.
 * <p>
 * Usage: {@code java UDPClient <host> <port>}
 */
public class UDPClient {

    /** Address of the server the datagrams are sent to. */
    private final InetAddress serverAddress;

    /** Port of the server the datagrams are sent to. */
    private final int serverPort;

    /**
     * Creates a client that sends to the given server.
     *
     * @param host name or IP address of the server (e.g. "localhost")
     * @param port UDP port of the server
     * @throws UnknownHostException if the host name cannot be resolved
     */
    public UDPClient(String host, int port) throws UnknownHostException {
        this.serverAddress = InetAddress.getByName(host);
        this.serverPort = port;
    }

    /**
     * Reads lines from the standard input and sends each one in a datagram,
     * until the standard input is closed.
     *
     * @throws IOException if the socket cannot be created or a datagram cannot be sent
     */
    public void launch() throws IOException {
        // One socket for the whole session, so the source port stays the same
        try (DatagramSocket socket = new DatagramSocket();
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

            System.out.println("Sending to " + serverAddress.getHostAddress() + ":" + serverPort
                               + " (Ctrl+D on Linux/macOS, Ctrl+Z then Enter on Windows to quit)");

            String line;
            // readLine() returns null when the standard input is closed
            while ((line = in.readLine()) != null) {
                byte[] data = line.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet =
                        new DatagramPacket(data, data.length, serverAddress, serverPort);
                socket.send(packet);
            }
        }
        System.out.println("Client stopped.");
    }

    /**
     * Starts a client towards the server given on the command line.
     *
     * @param args the host and the port of the server
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java UDPClient <host> <port>");
            return;
        }

        try {
            UDPClient client = new UDPClient(args[0], Integer.parseInt(args[1]));
            client.launch();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid port: " + args[1]);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}