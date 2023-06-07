package SayItAssistant.frontend;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

import SayItAssistant.middleware.IPrompt;
import SayItAssistant.middleware.IResponse;
import SayItAssistant.middleware.Observer;

/*
 * This panel holds the prompt, response, and start panels.  It's primary function is to keep the overall 
 * format of these panels standardized.
 */
public class DisplayPanel extends AppPanels implements Observer {

    // panels that are contained within the display panel
    PromptAndResponsePanel promptAndResponsePanel;
    StartPanel startPanel;
    CommandPanel commandPanel;
    private final String WELCOME = "Welcome to SayIt Assistant";
    private final String NO_COMMAND_WARN = "No command found";

    /*
     * Creates and formats the display panel.
     */
    public DisplayPanel() {

        // formats the panel
        this.setLayout(new GridBagLayout());
        GridBagConstraints displayFormat = new GridBagConstraints(); // this allows for finer control over formatting
                                                                     // than GridLayout previously did

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
     * @param promptAndResponsePanel: panel to be added
     * 
     * @param displayFormat: defines the formatting of the panel
     */
    public void addPromptAndResponsePanel(PromptAndResponsePanel promptAndResponsePanel,
            GridBagConstraints displayFormat) {

        // prompt and response panel will fill up the display panel all the way
        // horizontally and only 70% of the screen vertically
        displayFormat.fill = GridBagConstraints.BOTH;
        displayFormat.weightx = 1.0;
        displayFormat.weighty = 0.70;
        displayFormat.gridy = 1; // prompt / response panel is in the middle
        add(promptAndResponsePanel, displayFormat);
    }

    /*
     * Formats the panel for the main buttons (new question, clear all, delete).
     * 
     * @param buttonPanel: panel to be added
     * 
     * @param displayFormat: defines the formatting of the panel
     */
    public void addStartPanel(StartPanel startPanel, GridBagConstraints displayFormat) {

        // button panel will fill up the display panel all the way horizontally and only
        // 20% of the screen vertically
        displayFormat.fill = GridBagConstraints.BOTH;
        displayFormat.weightx = 1.0;
        displayFormat.weighty = 0.20;
        displayFormat.gridy = 2; // start panel is on the bottom
        startPanel.setMaximumSize(new Dimension(20, 20)); // keeping the maximum size small ensures that it is exceeded,
                                                          // making the button and prompt and response panels size ratio
                                                          // remain constant
        add(startPanel, displayFormat);
    }

    public void addCommandPanel(CommandPanel commandPanel, GridBagConstraints displayFormat) {
        // button panel will fill up the display panel all the way horizontally and only
        // 20% of the screen vertically
        displayFormat.fill = GridBagConstraints.BOTH;
        displayFormat.weightx = 1.0;
        displayFormat.weighty = 0.10;
        displayFormat.gridy = 0; // command panel should be on top
        startPanel.setMaximumSize(new Dimension(20, 20)); // keeping the maximum size small ensures that it is exceeded,
                                                          // making the button and prompt and response panels size ratio
                                                          // remain constant
        add(commandPanel, displayFormat);
    }

    /*
     * Returns the prompt and response panel on the display.
     * 
     * @return PromptAndResponsePanel: the prompt and response panel that is on the
     * display
     */
    public PromptAndResponsePanel getPromptAndResponsePanel() {
        return this.promptAndResponsePanel;
    }

    public CommandPanel getCommandPanel() {
        return this.commandPanel;
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

    public int getCurrentPromptNumber() {
        return promptAndResponsePanel.getCurrentPromptNumber();
    }

    @Override
    public void update(IPrompt prompt, IResponse response) {
        if (prompt.getMessage().equals("Create Email")) {
            try {
                FileWriter fw = new FileWriter("e.txt");
                fw.write(response.toString());
                fw.close();
            } catch (IOException e) {
            }
        }
        promptAndResponsePanel.setPrompt(prompt);
        promptAndResponsePanel.setResponse(response);
        if (prompt.toString() == NO_COMMAND_WARN) {
            commandPanel.setCommand(WELCOME);
        } else
            commandPanel.setCommand(prompt.getMessage());
    }
}