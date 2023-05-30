package SayItAssistant.frontend;

import javax.swing.*;
import java.awt.*;

/*
 * Panels created within the app use this for formatting.
 */
public class AppPanels extends JPanel {
    protected final static Color BLACK = new Color(24, 25, 26);
    protected final static Color WHITE = new Color(255, 255, 255);
    protected final static Color GREY = new Color(52, 53, 62);
    protected final static Color LIGHT_GREY = new Color(100,100,100);
    protected final static Color GREEN = new Color(0,255,0);
    protected final static Color TEAL = new Color(0,126,126);

    // Fun colors
    protected final static Color CREAM = new Color(255, 248, 214);
    protected final static Color BEIGE = new Color(247, 225, 174);
    protected final static Color MINT  = new Color(164, 208, 164);
    protected final static Color OLIVE = new Color(97, 122, 85);

    // Fun complementary colors
    protected final static Color MEADOW    = new Color(237, 241, 214);
    protected final static Color TEA_GREEN = new Color(157, 192, 139);
    protected final static Color GRASS     = new Color(96, 153, 102);
    protected final static Color SWAMP     = new Color(64, 81, 59);

    // Fun colors two
    protected final static Color NIGHT     = new Color(57, 54, 70);
    protected final static Color DARK_PLUM = new Color(79, 69, 87);
    protected final static Color PLUM      = new Color(109, 93, 110);

    // Detect user OS
    protected final static String OS = System.getProperty("os.name").toLowerCase();
    protected final static boolean IS_WINDOWS = (OS.indexOf("win") >= 0);
    protected final static boolean IS_MAC = (OS.indexOf("mac") >= 0);
    protected final static boolean IS_UNIX = 
        (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

    // Determine font to use based on OS
    protected static String font_style;

    static {
        if (IS_WINDOWS) {
            font_style = "Arial";
        } else if (IS_MAC) {
            font_style = "Arial";
        } else if (IS_UNIX) {
            font_style = "Ubuntu Mono";
        } else {
            font_style = "Arial";
        }
    }

    public final MyFont myFont = new MyFont(font_style, 18);
    protected final String historyFilePath = "src/backend/history.json";
}