import javax.swing.*;
import java.awt.*;

public class DisplayPanel extends JPanel{
    JTextArea display = new JTextArea();
    public DisplayPanel(){
        Color background = new Color(52,53,62);
        add(display);
        setBackground(background);
        display.setBackground(background);
        display.setText("Bitch");
    }
}
