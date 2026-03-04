import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;

public class App {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("CS Messenger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());
        Utilities palette = new Utilities(); //get a palette of color
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT)) { //create the main panel
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                int width = getWidth();
                int height = getHeight();

                g2.setPaint(Utilities.BACKGROUND_DARK);
                g2.fillRect(0, 0, width, height);
                g2.dispose();
            }
        };
        panel.setName("panel");
        panel.setBorder(emptyBorder);
        JButton settingButton = new CustomButton("Settings");
        panel.add(settingButton);

        /* Side bar for settings v */
        JPanel settingBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                int width = getWidth();
                int height = getHeight();

                g2.setPaint(Utilities.BACKGROUND);
                g2.fillRect(0, 0, width, height);
                g2.dispose();
            }
        };
        //for the setting menu
        settingBar.setName("settingBar");
        settingBar.setBorder(emptyBorder);
        
        /*Side bar for setting ^ */

        /* To change the theme v*/

        Font defaultFont = new Font("Dialog", Font.BOLD,(int)(Utilities.HEADING*Utilities.MULTIPLIER));
        JLabel settingText = new JLabel("SETTINGS", SwingConstants.CENTER);
        settingText.setFont(defaultFont);
        settingBar.add(settingText, BorderLayout.NORTH);

        CustomButton closeButton = new CustomButton("Close");
        closeButton.setName("closeButton");
        closeButton.setCustomGradient(new Color(255, 153, 153), new Color(204, 0, 0), new Color(255,139,142), new Color(255,0,0));
        settingBar.add(closeButton, BorderLayout.SOUTH);

        JPanel mainSettingBarPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                int width = getWidth();
                int height = getHeight();

                g2.setPaint(Utilities.BACKGROUND);
                g2.fillRect(0, 0, width, height);
                g2.dispose();
            }
        };
        mainSettingBarPanel.setName("mainSettingBarPanel");
        mainSettingBarPanel.setBorder(emptyBorder);
        settingBar.add(mainSettingBarPanel, BorderLayout.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        JButton themeButton = new CustomButton("Dark/White Mode");
        mainSettingBarPanel.add(themeButton, constraints);

        JPanel circlePanel = new JPanel(new GridLayout(3,6,10,10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                int width = getWidth();
                int height = getHeight();

                g2.setPaint(Utilities.BACKGROUND);
                g2.fillRect(0, 0, width, height);
                g2.dispose();
            }
        };

        //for the circle theme panel
        CircleDrawing[] circList = new CircleDrawing[17];
        for (int i = 0; i <circList.length;i++){
            circList[i] = new CircleDrawing(BubbleUtilities.THEMES.get(i));
            circlePanel.add(circList[i]);
        }
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        mainSettingBarPanel.add(circlePanel, constraints);

        settingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                settingButton.setVisible(false);
                settingBar.setPreferredSize(new Dimension(300, 0));
                settingBar.setBackground(Utilities.BACKGROUND);
                frame.getContentPane().add(settingBar, BorderLayout.EAST);
                frame.revalidate();
                frame.repaint();
                 
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingButton.setVisible(true);
                frame.getContentPane().remove(settingBar);
                frame.revalidate();
                frame.repaint();
            }
        });

        themeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (Utilities.ModeCheck() == false) {
                    //change to dark mode
                    Utilities.setDarkTheme();
                    panel.repaint();

                    settingBar.repaint();

                    palette.modeChange(true);
                    Utilities.changeComponentTheme(panel);
                    Utilities.changeComponentTheme(settingBar);
                    Utilities.changeComponentTheme(mainSettingBarPanel);
                }
                else {
                    //change to light mode
                    Utilities.setLightTheme();
                    panel.repaint();

                    settingBar.repaint();

                    palette.modeChange(false);
                    Utilities.changeComponentTheme(panel);
                    Utilities.changeComponentTheme(settingBar);
                    Utilities.changeComponentTheme(mainSettingBarPanel);
                }

            }
        });
        /*To change the theme ^ */

        Utilities.changeComponentTheme(panel);
        Utilities.changeComponentTheme(settingBar);
        Utilities.changeComponentTheme(mainSettingBarPanel);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
