package SayItAssistant.frontend;

import SayItAssistant.middleware.IResponse;
import SayItAssistant.middleware.IPrompt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

/**
 * This panel contains space for a prompt and response to be displayed.
 */
public class PromptAndResponsePanel extends AppPanels {

    PromptPanel promptPanel;     // panel for the prompt
    ResponsePanel responsePanel; // panel for the response
    Boolean isSet = false;       // Confirmation for set or not

    /*
     * Creates and formats the panel for prompts and responses.
     */
    public PromptAndResponsePanel() {
      
        this.setFormatting(); // gets the panel formatting setup

        // adds the prompt and response panels to the display
        addPromptPanel(promptPanel);
        addResponsePanel(responsePanel);
    }

    /*
     * Adds the prompt panel to the prompt and response panel.
     * 
     * @param promptPanel: the prompt panel to be added
     */
    public void addPromptPanel(PromptPanel promptPanel) {
        add(promptPanel.getPromptScrollPane(), BorderLayout.NORTH);
    }

    /*
     * Adds the response panel to the prompt and response panel.
     * 
     * @param responsePanel: the response panel to be added
     */
    public void addResponsePanel(ResponsePanel responsePanel) {
        add(responsePanel.getResponseScrollPane(), BorderLayout.SOUTH);
    }

    /*
     * Allows for the prompt text to be directly set.
     * 
     * @param prompt: the prompt to be displayed
     */
    public void setPrompt(IPrompt prompt) {
        promptPanel.setPrompt(prompt);
        isSet = true; //confirm the display is set
    }

    public int getCurrentPromptNumber(){
        return promptPanel.getCurrentPromptNumber(); 
    }

    /*
     * Defines the formatting for the prompt / response panel.
     */
    private void setFormatting() {
        // formats the panel
        this.setLayout(new GridLayout(2,1));
        this.setBackground(GREY);
        this.setPreferredSize(new Dimension(30,30));
        this.promptPanel = new PromptPanel();
        this.responsePanel = new ResponsePanel();
        this.setFont(this.myFont.getFont());
        this.setForeground(BLACK); 
    }
    /*
     * Allows for the response text to be directly set.
     * 
     * @param response: the response to be displayed
     */
    public void setResponse(IResponse response) {
        responsePanel.setResponse(response);
    }
    
    public boolean getStatus(){
        return this.isSet;
    }
}