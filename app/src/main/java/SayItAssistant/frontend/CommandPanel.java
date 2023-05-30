package SayItAssistant.frontend;

import java.awt.*;
import javax.swing.*;

/**
 * Class which contains the area for displaying the current command.
 */
public class CommandPanel extends AppPanels {

    private JTextArea command = new JTextArea();   // area for the command to be displayed with text

    /**
     * Constructs the panel for Start button.
     */
    public CommandPanel() {
        this.setFormatting();   // formats panel and text area
        add(command);           // adds text area to the panel
    }

    public void setCommand(String command) {
        this.command.setText(command);
    }

    /*
     * Defines the formatting for the command panel.
     */
    private void setFormatting() {
        this.setLayout(new GridLayout(0,1));
        command.setBackground(WHITE);
        command.setForeground(BLACK);
        command.setAlignmentX(BOTTOM_ALIGNMENT);
        command.setAlignmentY(BOTTOM_ALIGNMENT);
        command.setText("Welcome to SayIt Assistant");
        command.setLineWrap(true);
        command.setWrapStyleWord(true);
    }

}
