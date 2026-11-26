
import java.io.IOException;

/**
 * Represents a basic TCP server configured to listen on a specified port.
 * The server maintains its running state and provides a method to launch it.
 */
public class TCPServer {

    /** Port used when none is given. */
    public static final int DEFAULT_PORT = 8080;

    /** TCP port the server listens on. */
    private final int port;

    /** True while the server is running. */
    private boolean running = false;

    /**
     * Creates a TCP server with the specified port.
     *
     * @param port the TCP port to listen on
     */
    public TCPServer(int port) {
        this.port = port;
    }

    /**
     * Creates a TCP server using the default port.
     */
    public TCPServer() {
        this(DEFAULT_PORT);
    }

    /**
     * Launches the TCP server.
     * The implementation is provided in Step 2.
     *
     * @throws IOException if an I/O error occurs
     */
    public void launch() throws IOException {
        // To be implemented in Step 2
    }

    /**
     * Returns a string representation of the server state.
     *
     * @return a string containing the port and running state
     */
    @Override
    public String toString() {
        return "TCPServer[port=" + port
                + ", running=" + running + "]";
    }

    /**
     * Entry point of the TCP server application.
     * Creates and launches a server using the specified port
     * or the default port if no argument is provided.
     *
     * @param args command-line arguments, optionally containing the port number
     */
    public static void main(String[] args) {
        TCPServer server = (args.length >= 1)
                ? new TCPServer(Integer.parseInt(args[0]))
                : new TCPServer();

        try {
            server.launch();
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
