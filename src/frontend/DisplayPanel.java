package frontend;

import middleware.StartButton;

import java.awt.*;

/*
 * This panel holds the question, answer, and button panels.  It's primary function is to keep the overall 
 * format of these panels standardized.
 */
public class DisplayPanel extends AppPanels {

    // panels that are contained within the display panel
    PromptAndResponsePanel promptAndResponsePanel;
    StartPanel startPanel;

    /*
     * Creates and formats the display panel.
     */
    public DisplayPanel() {

        // formats the panel
        this.setLayout(new GridBagLayout());
        GridBagConstraints displayFormat = new GridBagConstraints();    // this allows for finer control over formatting than GridLayout previously did
        
        this.promptAndResponsePanel = new PromptAndResponsePanel();
        this.startPanel = new StartPanel();

        // adds the subpanels to the display
        addPromptAndResponsePanel(promptAndResponsePanel, displayFormat);
        addStartPanel(startPanel, displayFormat);
    }

    /*
     * Formats the panel for questions and answers and adds it to the display.
     * 
     * @param promptAndResponsePanel:         panel to be added
     * @param displayFormat:    defines the formatting of the panel
     */
    public void addPromptAndResponsePanel(PromptAndResponsePanel promptAndResponsePanel, GridBagConstraints displayFormat) {

        // prompt and response panel will fill up the display panel all the way horizontally and only 75% of the screen vertically
        displayFormat.fill = GridBagConstraints.BOTH;
        displayFormat.weightx = 1.0;
        displayFormat.weighty = 0.75;
        displayFormat.gridy = 0;
        add(promptAndResponsePanel, displayFormat);
    }

    /*
     * Formats the panel for the main buttons (new question, clear all, delete).
     * 
     * @param buttonPanel:      panel to be added
     * @param displayFormat:    defines the formatting of the panel
     */
    public void addStartPanel(StartPanel startPanel, GridBagConstraints displayFormat) {

        // button panel will fill up the display panel all the way horizontally and only 25% of the screen vertically
        displayFormat.fill = GridBagConstraints.BOTH;
        displayFormat.weightx = 1.0;
        displayFormat.weighty = 0.25;
        displayFormat.gridy = 1;    // button panel should be on top
        startPanel.setMaximumSize(new Dimension(20,20));   // keeping the maximum size small ensures that it is exceeded, making the button and prompt and response panels size ratio remain constant
        add(startPanel, displayFormat);
    }

    /*
     * Returns the prompt and response panel on the display.
     * 
     * @return PromptAndResponsePanel: the prompt and response panel that is on the display
     */
    public PromptAndResponsePanel getPromptAndResponsePanel() {
        return this.promptAndResponsePanel;
    }
    
    public void addStartButton(StartButton startButton) {
        startPanel.addStartButton(startButton);
    }
}