package frontend;

import javax.swing.*;
import java.awt.*;

import java.io.File;

/*
 * Buttons created within the app use this for formatting.
 */
public class AppPanels extends JPanel{
    protected final static Color BLACK = new Color(24, 25, 26);
    protected final static Color WHITE = new Color(255, 255, 255);
    protected final static Color GREY = new Color(52, 53, 62);
    protected final static Color LIGHT_GREY = new Color(100,100,100);
    protected final static Color GREEN = new Color(0,255,0);
    protected final static Color TEAL = new Color(0,126,126);
    protected final MyFont myFont = new MyFont("src/fonts/OpenSans-Regular.ttf", 18);;
    protected final String historyFilePath = "src/backend/history.json";
}