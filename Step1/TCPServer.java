/**
     * Entry point of the TCP server application.
     * Creates and launches a server using the specified port
     * or the default port if no argument is provided.
     *
     * @param args command-line arguments, optionally containing the port number
     */
import java.io.IOException;

public class TCPServer {

    /** Port used when none is given. */
    public static final int DEFAULT_PORT = 8080;

    /** TCP port the server listens on. */
    private final int port;

    /** True while the server is running. */
    private boolean running = false;

    // Constructor with a port
    public TCPServer(int port) {
        this.port = port;
    }

    // Default constructor
    public TCPServer() {
        this(DEFAULT_PORT);
    }

    // Starts the server
    public void launch() throws IOException {
        // To be implemented in Step 2
    }

    // Describes the state of the server
    @Override
    public String toString() {
        return "TCPServer[port=" + port
                + ", running=" + running + "]";
    }

    // Main method
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
