package SayItAssistant.frontend;

import SayItAssistant.middleware.IPrompt;

import javax.swing.*;
import java.awt.*;

/*
 * Panel that contains the area for prompts to be displayed.
 */
public class PromptPanel extends AppPanels{
    
    private JTextArea prompt = new JTextArea();   // area for the prompt to be displayed with text
    private JScrollPane promptScrollPane = new JScrollPane
        (this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);   // wraps the prompt area and allows it to be scrollable

    private int currentPromptNumber; //position of the current prompt displayed in the database

    /*
     * Initializes and formats the panel for the prompt.
     */
    public PromptPanel() {
        this.setFormatting();
        add(prompt);
    }

    /*
     * Allows the prompt area to be explicitly set.
     * 
     * @param prompt: the prompt to be displayed
     */
    public void setPrompt(IPrompt prompt) {
        this.prompt.setText(prompt.toString());
        currentPromptNumber = prompt.getPromptNumber(); 
    }

    /*
     * Returns the current prompt being displayed.
     */
    public String getPrompt() {
        return this.prompt.getText();
    }
    
    /*
     * Returns the prompt area with the scrollbar.
     * 
     * @return JScrollPane: the prompt pane with scrollbar
     */
    public JScrollPane getPromptScrollPane() {
        return this.promptScrollPane;
    }

    /*
     * getting the position of the current prompt in the database. 
     */
    public int getCurrentPromptNumber(){
        return currentPromptNumber; 
    }

    /*
     * Defines the formatting for the prompt panel.
     */
    private void setFormatting() {
        this.setLayout(new GridLayout(1, 1));
        prompt.setBackground(DARK_PLUM);
        prompt.setForeground(WHITE);
        prompt.setAlignmentX(BOTTOM_ALIGNMENT);
        prompt.setAlignmentY(BOTTOM_ALIGNMENT);
        prompt.setLineWrap(true);
        prompt.setWrapStyleWord(true);
    }

}