package SayItAssistant.frontend;

import java.awt.*;

/**
 * Class which contains the button for asking prompts.
 */
public class StartPanel extends AppPanels {

    /**
     * Constructs the panel for Start button.
     */
    public StartPanel() {
        this.setLayout(new GridLayout(0,1));
    }

    /**
     * Adds a Start button to the Start panel.
     * 
     * @param StartButton: StartButton object which contains the Start button
     */
    public void addStartButton(StartButton StartButton) {
        this.add(StartButton);
    }

}

