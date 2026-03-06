import java.net.*;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * MulticastChatClient
 * - User picks a channel and enters a username.
 * - A background thread listens for incoming multicast messages.
 * - The main thread reads user input and sends it to the server.
 * - Type "/quit" to disconnect gracefully.
 * - Type "/switch" to change channels.
 */
public class MulticastChatClient {

    private static final int    BROADCAST_PORT = 4446;
    private static final int    BUFFER_SIZE    = 2048;
    private static final int    TTL            = 4;

    // Mirrors server config
    private static final String[] GROUPS        = { "230.0.0.1", "230.0.0.2", "230.0.0.3" };
    private static final String[] CHANNEL_NAMES = { "General",   "Sports",    "News"       };
    private static final int[]    SEND_PORTS    = { 5001,        5002,        5003         };

    private static volatile boolean running = true;
    private static volatile int     currentChannel;
    private static volatile String  username;

    private static MulticastSocket  listenSocket;
    private static DatagramSocket   sendSocket;
    private static InetAddress      currentGroup;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        printBanner();

        System.out.print("Enter your username: ");
        username = scanner.nextLine().trim();
        if (username.isEmpty()) username = "Anonymous";

        currentChannel = pickChannel(scanner);

        initSockets();

        ExecutorService listener = Executors.newSingleThreadExecutor();
        listener.submit(MulticastChatClient::listenLoop);

        sendSystemMessage("joined the channel");
        System.out.println("\nYou joined [" + CHANNEL_NAMES[currentChannel] + "]. Type a message and press ENTER.");
        System.out.println("Commands:  /switch  change channel   |  /quit  disconnect\n");

        // ── Main send loop ──────────────────────────────────────────────
        while (running) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            switch (line.toLowerCase()) {
                case "/quit" -> {
                    sendSystemMessage("left the channel");
                    running = false;
                }
                case "/switch" -> {
                    sendSystemMessage("left the channel");
                    leaveCurrentGroup();
                    currentChannel = pickChannel(scanner);
                    joinCurrentGroup();
                    sendSystemMessage("joined the channel");
                    System.out.println("Switched to [" + CHANNEL_NAMES[currentChannel] + "]\n");
                }
                default -> sendChat(line);
            }
        }

        // ── Shutdown ────────────────────────────────────────────────────
        leaveCurrentGroup();
        listenSocket.close();
        sendSocket.close();
        listener.shutdownNow();
        System.out.println("Disconnected. Goodbye!");
    }

    // ── Socket helpers ───────────────────────────────────────────────────

    private static void initSockets() throws Exception {
        sendSocket  = new DatagramSocket();
        listenSocket = new MulticastSocket(BROADCAST_PORT);
        joinCurrentGroup();
    }

    private static void joinCurrentGroup() throws Exception {
        currentGroup = InetAddress.getByName(GROUPS[currentChannel]);
        listenSocket.joinGroup(currentGroup);
    }

    private static void leaveCurrentGroup() throws Exception {
        listenSocket.leaveGroup(currentGroup);
    }

    // ── Messaging ────────────────────────────────────────────────────────

    private static void sendChat(String text) throws Exception {
        String msg = username + ": " + text;
        sendRaw(msg);
    }

    private static void sendSystemMessage(String action) throws Exception {
        String msg = "*** " + username + " " + action + " ***";
        sendRaw(msg);
    }

    private static void sendRaw(String msg) throws Exception {
        byte[]         data   = msg.getBytes();
        InetAddress    server = InetAddress.getLocalHost(); // change to server IP if remote
        int            port   = SEND_PORTS[currentChannel];
        DatagramPacket packet = new DatagramPacket(data, data.length, server, port);
        sendSocket.send(packet);
    }

    // ── Listener thread ──────────────────────────────────────────────────

    private static void listenLoop() {
        byte[] buf = new byte[BUFFER_SIZE];
        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                listenSocket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                System.out.println("  [" + CHANNEL_NAMES[currentChannel] + "] " + msg);
            } catch (Exception e) {
                if (running) System.err.println("Receive error: " + e.getMessage());
                break;
            }
        }
    }

    // ── UI helpers ───────────────────────────────────────────────────────

    private static int pickChannel(Scanner scanner) {
        while (true) {
            System.out.println("\nAvailable channels:");
            for (int i = 0; i < CHANNEL_NAMES.length; i++) {
                System.out.printf("  %d - %s  (%s)%n", i + 1, CHANNEL_NAMES[i], GROUPS[i]);
            }
            System.out.print("Choose a channel (1-" + CHANNEL_NAMES.length + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= CHANNEL_NAMES.length) return choice - 1;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid choice, try again.");
        }
    }

    private static void printBanner() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║   Multicast Chat Client          ║");
        System.out.println("╚══════════════════════════════════╝");
    }
}
