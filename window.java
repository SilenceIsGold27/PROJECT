import javax.swing.*;
import java.awt.*;

public class window {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CS Message Interface");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 500);
            frame.setLayout(new BorderLayout());
            
            ChatPanel chatPanel = new ChatPanel();
            InputPanel inputPanel = new InputPanel();
            ProfilePanel profilePanel = new ProfilePanel(chatPanel); // Pass chatPanel to ProfilePanel
            
            frame.add(profilePanel, BorderLayout.WEST);
            frame.add(chatPanel, BorderLayout.CENTER);
            frame.add(inputPanel, BorderLayout.SOUTH);
            
            // Set default messages for Tommy
            chatPanel.setCurrentContact("Tommy");
            chatPanel.addMessage("Tommy", "Hello!");
            chatPanel.addMessage("You", "Hi Tommy!");
            chatPanel.addMessage("Tommy", "How are you?");
            
            frame.setVisible(true);
        });
    }
}