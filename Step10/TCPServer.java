
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * TCP server (TP 4, Step 10).
 * <p>
 * Demonstrates the byte-stream nature of TCP by reading raw bytes
 * from client connections and displaying the number of bytes
 * received during each read operation.
 * TCP does not preserve application-level write boundaries.
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
     * Each client is handled sequentially. After accepting a connection,
     * the server waits one second before reading incoming data.
     * Data is read into a 1024-byte buffer, and the number of bytes
     * received during each read operation is displayed.
     * Reading continues until the client closes the connection.
     * The server stops if the waiting thread is interrupted.
     *
     * @throws IOException if an I/O error occurs when opening
     *                     or closing the server socket
     */
    public void launch() throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            running = true;
            System.out.println("Started: " + this);

            while (true) {

                System.out.println("Waiting for a client...");

                try (Socket socket = serverSocket.accept()) {

                    System.out.println("Client connected: "
                            + socket.getInetAddress());

                    // Wait one second before reading
                    Thread.sleep(1000);

                    InputStream input = socket.getInputStream();

                    byte[] buffer = new byte[1024];

                    int bytesRead;

                    while ((bytesRead = input.read(buffer)) != -1) {

                        System.out.println("Bytes received: " + bytesRead);
                    }

                } catch (IOException e) {
                    System.err.println("Client error: " + e.getMessage());

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
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
