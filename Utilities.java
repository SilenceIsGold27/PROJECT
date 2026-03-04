import java.awt.*;
import javax.swing.*;

public class Utilities {
     //white by default
    public static Color TRANSPARENT_COLOR = new Color(0,0,0,0);
    public static Color BACKGROUND_DARK = new Color(230,230,230);
    public static Color BACKGROUND = new Color(242,242,242);
    public static Color BACKGROUND_LIGHT = new Color(255,255,255);
    public static Color TEXT = new Color(13,13,13);
    public static Color TEXT_MUTED = new Color(77,77,77);
    public static Color BORDER = new Color(179, 179, 179);
    public static Color HIGHLIGHT = new Color(255,255,255);
    public static Color ACCENT = new Color(220, 220, 220);
    public static Color ACCENT_LIGHT = new Color(245, 245, 245);
    private static boolean dark = false;

    public static String newFont = "Arial";
    public static int fontType = Font.PLAIN;
    public static int HEADING = 27;
    public static int SUBHEADING = 18;
    public static int BODY = 12;
    public static float MULTIPLIER = 1.0f;

    public static void setLightTheme() {
        TRANSPARENT_COLOR = new Color(0,0,0,0);
        BACKGROUND_DARK = new Color(230,230,230);
        BACKGROUND = new Color(242,242,242);
        BACKGROUND_LIGHT = new Color(255,255,255);
        TEXT = new Color(13,13,13);
        TEXT_MUTED = new Color(77,77,77);
        BORDER = new Color(179, 179, 179);
        HIGHLIGHT = new Color(255,255,255);
        ACCENT = new Color(220, 220, 220);
        ACCENT_LIGHT = new Color(245, 245, 245);
    }

    public static void setDarkTheme() {
        BACKGROUND_DARK = new Color(0,0,0);
        BACKGROUND = new Color(13,13,13);
        BACKGROUND_LIGHT = new Color(26,26,26);
        TEXT = new Color(242,242,242);
        TEXT_MUTED = new Color(179, 179, 179);
        BORDER = new Color(77, 77, 77);
        HIGHLIGHT = new Color(153, 153, 153);
        ACCENT = new Color(40,40,40);
        ACCENT_LIGHT = new Color(60,60,60);
    }

    public static void changeComponentTheme(Container container){
        //this will change all the buttons to the right color of theme
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                if (component.getName() == "closeButton") {
                    ((JButton) component).setForeground(TEXT);
                }
                ((JButton) component).setBackground(BACKGROUND);
                ((JButton) component).setForeground(TEXT);
            }
            else if (component instanceof JLabel){
                ((JLabel) component).setForeground(TEXT);
            }
        }
    }

    public static boolean settingBarPresent(Container container, String target){
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                if ((component.getName()).equals(target)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean ModeCheck() {
        return dark;
    }

    public void modeChange(boolean mode){
        dark = mode;
    }
    
}
