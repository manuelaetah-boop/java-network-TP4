
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * TCP server (TP 4, Step 7).
 * <p>
 * Listens on a specified port and handles client connections
 * sequentially. Receives and sends text messages using UTF-8
 * encoding, prefixing each response with the client's IP address.
 * Distinguishes server socket errors from client communication errors.
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
     * Launches the TCP server and waits for incoming client connections.
     * <p>
     * Each client is handled sequentially. Received messages are
     * displayed on the server console and sent back to the client
     * with its IP address.
     * Client communication errors are handled without stopping
     * the server, while server socket errors are propagated.
     *
     * @throws IOException if an I/O error occurs when opening
     *                     or closing the server socket or accepting
     *                     a client connection
     */
    public void launch() throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            running = true;
            System.out.println("Started: " + this);

            while (true) {

                System.out.println("Waiting for a client...");

                Socket clientSocket = serverSocket.accept();

                try (Socket socket = clientSocket) {

                    System.out.println("Client connected: "
                            + socket.getInetAddress());

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream(),
                                    StandardCharsets.UTF_8));

                    PrintWriter writer = new PrintWriter(
                            socket.getOutputStream(),
                            true,
                            StandardCharsets.UTF_8);

                    String msg;

                    while ((msg = reader.readLine()) != null) {

                        String response = socket.getInetAddress()
                                .getHostAddress() + " > " + msg;

                        System.out.println(response);

                        writer.println(response);
                    }

                } catch (IOException e) {
                    System.err.println("Client error: "
                            + e.getMessage());
                }

                System.out.println("Client disconnected.");
            }

        } finally {
            running = false;
        }
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
     * @param args command-line arguments, optionally containing
     *             the port number
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
