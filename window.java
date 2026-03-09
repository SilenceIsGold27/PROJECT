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
            InputPanel inputPanel = new InputPanel(chatPanel);
            ProfilePanel profilePanel = new ProfilePanel(chatPanel);
            
            // Create a center panel that holds both chatPanel and inputPanel
            // this was needed since adding the emoji broke the layout
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(chatPanel, BorderLayout.CENTER);
            centerPanel.add(inputPanel, BorderLayout.SOUTH);
            
            frame.add(profilePanel, BorderLayout.WEST);
            frame.add(centerPanel, BorderLayout.CENTER);
            
            
            chatPanel.setCurrentContact("Tommy");
            chatPanel.addMessage("Tommy", "Hello! 👋");
            chatPanel.addMessage("You", "Hi Tommy! 😊");
            chatPanel.addMessage("Tommy", "How are you? 🤔");
            
            frame.setVisible(true);
        });
    }
}