import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Flow;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
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

        CustomPanel panel = new CustomPanel(new FlowLayout(FlowLayout.RIGHT), Utilities.BACKGROUND_DARK);
        panel.setName("panel");
        panel.setBorder(emptyBorder);
        JButton settingButton = new CustomButton("Settings");
        panel.add(settingButton);

        /* Side bar for settings v */
        CustomPanel settingBar = new CustomPanel(new BorderLayout(), Utilities.BACKGROUND);
        settingBar.setPreferredSize(new Dimension(300, 0));
        settingBar.setName("settingBar");
        settingBar.setBorder(emptyBorder);
        /*Side bar for setting ^ */

        /* To change the theme v*/

        CustomLabel settingText = new CustomLabel("SETTINGS", SwingConstants.CENTER, Utilities.HEADING, Font.BOLD);
        settingBar.add(settingText, BorderLayout.NORTH);

        CustomButton closeButton = new CustomButton("Close");
        closeButton.setName("closeButton");
        closeButton.setCustomGradient(new Color(255, 153, 153), new Color(204, 0, 0), new Color(255,139,142), new Color(255,0,0));
        settingBar.add(closeButton, BorderLayout.SOUTH);

        //setting bar panel
        CustomPanel mainSettingBarPanel = new CustomPanel(Utilities.BACKGROUND);
        mainSettingBarPanel.setLayout(new BoxLayout(mainSettingBarPanel, BoxLayout.Y_AXIS));
        mainSettingBarPanel.setName("mainSettingBarPanel");
        mainSettingBarPanel.setBorder(emptyBorder);
        mainSettingBarPanel.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
        settingBar.add(mainSettingBarPanel, BorderLayout.CENTER);

        CustomLabel themeLabel = new CustomLabel("Theme:", SwingConstants.CENTER, Utilities.SUBHEADING, Font.PLAIN);
        mainSettingBarPanel.add(themeLabel);

        JButton themeButton = new CustomButton("Dark/White Mode");
        themeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        themeButton.setBorder(emptyBorder);
        themeButton.setMaximumSize(themeButton.getPreferredSize());
        mainSettingBarPanel.add(themeButton);

        CustomPanel circlePanel = new CustomPanel(new GridLayout(3,6,7,7), Utilities.BACKGROUND);
        //for the circle theme panel
        CircleDrawing[] circList = new CircleDrawing[17];
        for (int i = 0; i <circList.length;i++){
            circList[i] = new CircleDrawing(BubbleUtilities.THEMES.get(i));
            circList[i].getPreferredSize();
            circlePanel.add(circList[i]);
        }

        circlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        circlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, circlePanel.getPreferredSize().height + 15));
        circlePanel.setBorder(emptyBorder);
        mainSettingBarPanel.add(circlePanel);

        CustomLabel fontLabel = new CustomLabel("Font: ", 0, Utilities.SUBHEADING, Font.PLAIN);
        mainSettingBarPanel.add(fontLabel);

        CustomPanel sliderText = new CustomPanel(Utilities.BACKGROUND);
        sliderText.setLayout(new BoxLayout(sliderText, BoxLayout.X_AXIS));
        sliderText.setBorder(emptyBorder);

        CustomLabel sizeLabel = new CustomLabel("Size", 0, Utilities.BODY, Font.PLAIN);
        sliderText.setBorder(emptyBorder);
        sliderText.add(sizeLabel);

        JSlider multiplierSlider = new JSlider(JSlider.HORIZONTAL, 75, 175, 100){
            @Override
            public Dimension getPreferredSize(){
                Dimension dim = super.getPreferredSize();
                dim.height = Math.max(dim.height, 40);
                return dim;
            }
        };
        multiplierSlider.setOpaque(false);
        multiplierSlider.setUI(new CustomSlider(multiplierSlider)); //set the color of the slider
        multiplierSlider.setMaximumSize(multiplierSlider.getPreferredSize());
        sliderText.add(multiplierSlider);
        sliderText.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderText.setMaximumSize(new Dimension(Integer.MAX_VALUE, sliderText.getPreferredSize().height));
        mainSettingBarPanel.add(sliderText);

        CustomButton fontButton = new CustomButton("Change Font");
        fontButton.setBorder(emptyBorder);
        fontButton.setMaximumSize(themeButton.getPreferredSize());
        mainSettingBarPanel.add(fontButton);

        fontButton.addActionListener(e ->{
            //get fonts
            String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            JList<String> listFont = new JList<>(fonts);
            listFont.setVisibleRowCount(12); // show 12 at a time
            listFont.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listFont.setFixedCellHeight(20);
            //get font and written in their style
            listFont.setCellRenderer(new DefaultListCellRenderer(){
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object val, int index, boolean isSelected, boolean cellHasFocus){
                    JLabel lab = (JLabel) super.getListCellRendererComponent(list, val, index, isSelected, cellHasFocus);
                    String name = val.toString();
                    lab.setFont(new Font(name, Font.PLAIN, 14));

                    return lab;
                }
            });

            //
            JScrollPane scroll = new JScrollPane(listFont);
            scroll.setPreferredSize(new Dimension(250, listFont.getFixedCellHeight() * 12));

            JPopupMenu pop = new JPopupMenu();
            pop.setLayout(new BorderLayout());
            pop.add(scroll, BorderLayout.CENTER);

            listFont.addListSelectionListener(ex -> {
                if (!ex.getValueIsAdjusting()){
                    String sel = listFont.getSelectedValue();
                    CustomLabel.applyFont(sel);
                    pop.setVisible(false);
                }
            });
            pop.show(fontButton, 0, fontButton.getHeight());
        });

        multiplierSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = multiplierSlider.getValue();
                CustomLabel.MULTIPLIER = (float)value / 100;
                CustomLabel.changeFont();
            }
        });

        settingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                settingButton.setVisible(false);
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
                    Utilities.changeComponentTheme(circlePanel);
                    Utilities.changeComponentTheme(sliderText);
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
                    Utilities.changeComponentTheme(circlePanel);
                    Utilities.changeComponentTheme(sliderText);
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
