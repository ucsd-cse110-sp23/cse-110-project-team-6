package SayItAssistant.frontend;

import java.awt.*;

/*
 * This panel holds the prompt, response, and start panels.  It's primary function is to keep the overall 
 * format of these panels standardized.
 */
public class DisplayPanel extends AppPanels {

    // panels that are contained within the display panel
    PromptAndResponsePanel promptAndResponsePanel;
    StartPanel startPanel;
    CommandPanel commandPanel;

    /*
     * Creates and formats the display panel.
     */
    public DisplayPanel() {

        // formats the panel
        this.setLayout(new GridBagLayout());
        GridBagConstraints displayFormat = new GridBagConstraints();    // this allows for finer control over formatting than GridLayout previously did
        
        this.promptAndResponsePanel = new PromptAndResponsePanel();
        this.startPanel = new StartPanel();
        this.commandPanel = new CommandPanel();

        // adds the subpanels to the display
        addPromptAndResponsePanel(promptAndResponsePanel, displayFormat);
        addStartPanel(startPanel, displayFormat);
        addCommandPanel(commandPanel, displayFormat);
    }

    /*
     * Formats the panel for questions and answers and adds it to the display.
     * 
     * @param promptAndResponsePanel:         panel to be added
     * @param displayFormat:    defines the formatting of the panel
     */
    public void addPromptAndResponsePanel(PromptAndResponsePanel promptAndResponsePanel, GridBagConstraints displayFormat) {

        // prompt and response panel will fill up the display panel all the way horizontally and only 70% of the screen vertically
        displayFormat.fill = GridBagConstraints.BOTH;
        displayFormat.weightx = 1.0;
        displayFormat.weighty = 0.70;
        displayFormat.gridy = 1;    // prompt / response panel is in the middle
        add(promptAndResponsePanel, displayFormat);
    }

    /*
     * Formats the panel for the main buttons (new question, clear all, delete).
     * 
     * @param buttonPanel:      panel to be added
     * @param displayFormat:    defines the formatting of the panel
     */
    public void addStartPanel(StartPanel startPanel, GridBagConstraints displayFormat) {

        // button panel will fill up the display panel all the way horizontally and only 20% of the screen vertically
        displayFormat.fill = GridBagConstraints.BOTH;
        displayFormat.weightx = 1.0;
        displayFormat.weighty = 0.20;
        displayFormat.gridy = 2;   // start panel is on the bottom
        startPanel.setMaximumSize(new Dimension(20,20));   // keeping the maximum size small ensures that it is exceeded, making the button and prompt and response panels size ratio remain constant
        add(startPanel, displayFormat);
    }

    public void addCommandPanel(CommandPanel commandPanel, GridBagConstraints displayFormat) {
        // button panel will fill up the display panel all the way horizontally and only 20% of the screen vertically
        displayFormat.fill = GridBagConstraints.BOTH;
        displayFormat.weightx = 1.0;
        displayFormat.weighty = 0.10;
        displayFormat.gridy = 0;    // command panel should be on top
        startPanel.setMaximumSize(new Dimension(20,20));   // keeping the maximum size small ensures that it is exceeded, making the button and prompt and response panels size ratio remain constant
        add(commandPanel, displayFormat);
    }

    /*
     * Returns the prompt and response panel on the display.
     * 
     * @return PromptAndResponsePanel: the prompt and response panel that is on the display
     */
    public PromptAndResponsePanel getPromptAndResponsePanel() {
        return this.promptAndResponsePanel;
    }
    
    /*
     * Adds the start button to the start panel.
     */
    public void addStartButton(StartButton startButton) {
        startPanel.addStartButton(startButton);
    }

    /*
     * Sets the current command that is being looked at.
     */
    public void setCommand(String command) {
        this.commandPanel.setCommand(command);
        revalidate();
    }
}