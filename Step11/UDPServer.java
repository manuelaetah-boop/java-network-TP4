import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 * UDP server (This is the same one as in Step 1).
 * <p>
 * Receives datagrams containing UTF-8 strings and prints each one on the
 * standard output, prefixed with the address and port of the sender.
 * Messages longer than {@link #MAX_SIZE} bytes are truncated.
 * <p>
 * Usage: {@code java UDPServer [port]}
 */
public class UDPServer {

    /** Port used when none is given. */
    public static final int DEFAULT_PORT = 8080;

    /** Maximum size of a received message, in bytes. */
    public static final int MAX_SIZE = 1024;

    /** UDP port the server listens on. */
    private final int port;

    /** True while the server is receiving datagrams. */
    private boolean running = false;

    /**
     * Creates a server listening on the given port.
     *
     * @param port the UDP port to listen on
     */
    public UDPServer(int port) {
        this.port = port;
    }

    /** Creates a server listening on {@link #DEFAULT_PORT}. */
    public UDPServer() {
        this(DEFAULT_PORT);
    }

    /**
     * Starts the server: receives datagrams in an infinite loop and prints them.
     * This method never returns normally.
     *
     * @throws SocketException if the socket cannot be opened (e.g. port already in use)
     */
    public void launch() throws SocketException {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            running = true;
            System.out.println("Started: " + this);

            byte[] buffer = new byte[MAX_SIZE];

            while (true) {
                // New packet each time: receive() shrinks the packet's length
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    System.err.println("Receive error: " + e.getMessage());
                    continue;
                }

                // Decode only the bytes actually received
                String msg = new String(packet.getData(), 0, packet.getLength(),
                                        StandardCharsets.UTF_8);

                System.out.println(packet.getAddress().getHostAddress() + ":"
                                   + packet.getPort() + " > " + msg);
            }
        } finally {
            running = false;
        }
    }

    /**
     * Describes the state of the server.
     *
     * @return the port and whether the server is running
     */
    @Override
    public String toString() {
        return "UDPServer[port=" + port + ", running=" + running + "]";
    }

    /**
     * Starts a server on the port given as first argument, or on the default port.
     *
     * @param args optional port number
     */
    public static void main(String[] args) {
        UDPServer server = (args.length >= 1)
                ? new UDPServer(Integer.parseInt(args[0]))
                : new UDPServer();

        try {
            server.launch();
        } catch (SocketException e) {
            System.err.println("Cannot open socket on port: " + e.getMessage());
        }
    }
}