import java.net.*;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * CsChatClient
 * Designed to work with CsChatServer.
 */
public class CsChatClient {

    private static final int BROADCAST_PORT = 4446;
    private static final int BUFFER_SIZE = 2048;

    // Must match CsChatServer exactly
    private static final String[] GROUPS = { "230.0.0.1", "230.0.0.2" };
    private static final String[] CHANNEL_NAMES = { "tommy1", "tommy2" };
    private static final int[] SEND_PORTS = { 5001, 5002 };

    private static volatile boolean running = true;
    private static int currentChannel;
    private static String username;
    
    private static MulticastSocket listenSocket;
    private static DatagramSocket sendSocket;
    private static InetAddress currentGroup;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== CS Chat Messenger ===");
        System.out.print("Enter Username: ");
        username = scanner.nextLine().trim();

        // 1. Pick Channel
        System.out.println("\nSelect Channel:");
        for (int i = 0; i < CHANNEL_NAMES.length; i++) {
            System.out.println((i + 1) + ". " + CHANNEL_NAMES[i]);
        }
        int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
        currentChannel = choice;

        // 2. Setup Sockets
        sendSocket = new DatagramSocket();
        listenSocket = new MulticastSocket(BROADCAST_PORT);
        currentGroup = InetAddress.getByName(GROUPS[currentChannel]);
        
        // Join the multicast group to hear others
        listenSocket.joinGroup(currentGroup);

        // 3. Start Listener Thread (Background)
        Thread listener = new Thread(CsChatClient::listenLoop);
        listener.setDaemon(true);
        listener.start();

        System.out.println("\nJoined [" + CHANNEL_NAMES[currentChannel] + "]. Type away! (Type '/quit' to exit)");

        // 4. Send Loop (Foreground)
        while (running) {
            String text = scanner.nextLine();
            if (text.equalsIgnoreCase("/quit")) {
                running = false;
                break;
            }

            String fullMessage = username + ": " + text;
            byte[] data = fullMessage.getBytes();
            
            // Send to the Server's specific receive port
            DatagramPacket packet = new DatagramPacket(
                data, data.length, InetAddress.getLocalHost(), SEND_PORTS[currentChannel]
            );
            sendSocket.send(packet);
        }

        // Cleanup
        listenSocket.leaveGroup(currentGroup);
        listenSocket.close();
        sendSocket.close();
        System.out.println("Goodbye!");
    }

    private static void listenLoop() {
        byte[] buf = new byte[BUFFER_SIZE];
        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                listenSocket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("\n" + received);
                System.out.print("> "); // Prompt indicator
            } catch (Exception e) {
                if (running) System.err.println("Listener Error: " + e.getMessage());
            }
        }
    }
}
