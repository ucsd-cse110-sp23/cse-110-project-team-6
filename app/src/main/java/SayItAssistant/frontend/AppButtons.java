package SayItAssistant.frontend;

import javax.swing.*;
import java.awt.*;

/*
 * Buttons created within the app use this for formatting and initialization.
 */
public class AppButtons extends JButton {

    // preset colors
    protected final static Color BLACK = new Color(24, 25, 26);
    protected final static Color WHITE = new Color(255, 255, 255);
    protected final static Color TEAL = new Color(0,126,126);
    protected final static Color GREEN = new Color(0,50,0);
    protected final static Color LIGHT_GREY = new Color(100,100,100);

    // button attributes
    protected int id;
    protected int buttonWidth;
    protected int buttonHeight;

    /*
     * Creates the button using JButton.
     * 
     * @param label: what the button will dispaly
     */
    protected AppButtons(String label) {
        super(label);
    }
}