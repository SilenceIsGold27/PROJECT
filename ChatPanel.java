import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {
    
    JPanel messageArea;
    JPanel headerPanel;
    JLabel contactNameLabel;
    String currentContact = "Tommy";

    public ChatPanel() {
        setLayout(new BorderLayout());
 
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contactNameLabel = new JLabel("Chat with: " + currentContact);
        contactNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(contactNameLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        messageArea = new JPanel();
        messageArea.setLayout (new BoxLayout(messageArea, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setCurrentContact(String contactName) {
        this.currentContact = contactName;
        contactNameLabel.setText("Chat with: " + currentContact);
    }

    public void clearMessages() {
        messageArea.removeAll();
        messageArea.revalidate();
        messageArea.repaint();
    }

    public void addMessage(String sender, String message) {
        JPanel messageRow = new JPanel();
        messageRow.setLayout(new FlowLayout(FlowLayout.LEFT));    

        JLabel senderLabel = new JLabel(sender + ": ");
        senderLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        messageRow.add(senderLabel);
        messageRow.add(messageLabel);

        messageRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageArea.add(messageRow);
        messageArea.revalidate();
        messageArea.repaint();
    }

    public void createBubble(String sender, String message) {
        
    }
}
