import java.net.*;
import java.util.concurrent.*;
import java.util.*;

/**
 * MulticastChatServer
 * Listens for messages from all clients and rebroadcasts them to each channel's multicast group.
 * Channels:
 *   1 - General  → 230.0.0.1
 *   2 - Sports   → 230.0.0.2
 *   3 - News     → 230.0.0.3
 */
public class MulticastChatServer {

    public static final String[] GROUPS = {
        "230.0.0.1",  // General
        "230.0.0.2",  // Sports
        "230.0.0.3"   // News
    };

    public static final String[] CHANNEL_NAMES = { "General", "Sports", "News" };

    // Clients send to these ports per channel
    public static final int[] RECEIVE_PORTS = { 5001, 5002, 5003 };

    // Server rebroadcasts on this port (clients listen here)
    public static final int BROADCAST_PORT = 4446;
    public static final int TTL = 4;
    public static final int BUFFER_SIZE = 2048;

    public static void main(String[] args) throws Exception {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║   Multicast Chat Server          ║");
        System.out.println("╚══════════════════════════════════╝");

        ExecutorService executor = Executors.newFixedThreadPool(GROUPS.length);

        for (int i = 0; i < GROUPS.length; i++) {
            final int channelIdx = i;
            executor.submit(() -> runChannel(channelIdx));
        }

        System.out.println("Server running. Press Ctrl+C to stop.\n");
        // Keep alive
        Thread.currentThread().join();
    }

    private static void runChannel(int idx) {
        String channelName = CHANNEL_NAMES[idx];
        String groupAddr   = GROUPS[idx];
        int    receivePort = RECEIVE_PORTS[idx];

        System.out.println("[" + channelName + "] Listening for client messages on port " + receivePort);

        try (DatagramSocket receiveSocket = new DatagramSocket(receivePort);
             MulticastSocket broadcastSocket = new MulticastSocket()) {

            broadcastSocket.setTimeToLive(TTL);
            InetAddress group = InetAddress.getByName(groupAddr);
            byte[] buf = new byte[BUFFER_SIZE];

            while (true) {
                DatagramPacket incoming = new DatagramPacket(buf, buf.length);
                receiveSocket.receive(incoming);

                String msg = new String(incoming.getData(), 0, incoming.getLength());
                System.out.println("[" + channelName + "] " + msg);

                // Rebroadcast to multicast group
                byte[] out = msg.getBytes();
                DatagramPacket outPacket = new DatagramPacket(out, out.length, group, BROADCAST_PORT);
                broadcastSocket.send(outPacket);
            }
        } catch (Exception e) {
            System.err.println("[" + channelName + "] Error: " + e.getMessage());
        }
    }
}
