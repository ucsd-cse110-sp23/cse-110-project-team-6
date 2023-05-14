package frontend;

import javax.swing.*;
import java.awt.*;

public class AppButtons extends JButton {
    protected final static Color BLACK = new Color(24, 25, 26);
    protected final static Color WHITE = new Color(255, 255, 255);
    protected final static Color TEAL = new Color(0,126,126);
    protected final static Color GREEN = new Color(0,50,0);
    protected int id;
    protected int buttonWidth;
    protected int buttonHeight;

    AppButtons(String label) {
        super(label);
    }
}
