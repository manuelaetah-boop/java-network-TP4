import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
