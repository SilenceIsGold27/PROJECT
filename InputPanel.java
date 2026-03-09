import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {

    public InputPanel() {

        setLayout(new FlowLayout(FlowLayout.CENTER));

        JTextField inputfield = new JTextField(30);
        JButton sendbutton = new JButton("Send");

        add(inputfield);
        add(sendbutton);

        sendbutton.addActionListener(e -> {
            String message = inputfield.getText();
            System.out.println(message);
            inputfield.setText("");
        });

        inputfield.addActionListener(e -> sendbutton.doClick());
    }
}