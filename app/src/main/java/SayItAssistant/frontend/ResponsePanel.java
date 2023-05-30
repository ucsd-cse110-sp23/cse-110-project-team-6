package SayItAssistant.frontend;

import SayItAssistant.middleware.IResponse;

import javax.swing.*;
import java.awt.*;

/*
 * Panel that contains the area for responses to be displayed.
 */
public class ResponsePanel extends AppPanels{

    private JTextArea response = new JTextArea(); // area for the response to be displayed with text
    private JScrollPane responseScrollPane = new JScrollPane
        (this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);   // wraps the response area and allows it to be scrollable

    /*
     * Initializes and formats the panel for the response.
     */
    public ResponsePanel() {
        this.setFormatting();   // formats panel and text area
        add(response);          // adds text area to the panel
    }

    /*
     * Allows the response area to be explicitly set.
     * 
     * @param response: The response to be displayed
     */
    public void setResponse(IResponse response) {
        this.response.setText(response.toString());
    }

    /*
     * Returns the response area with the scrollbar.
     * 
     * @return JScrollPane: the response pane with scrollbar
     */
    public JScrollPane getResponseScrollPane() {
        return this.responseScrollPane;
    }

    /*
     * Defines the formatting for the response panel.
     */
    private void setFormatting() {
        this.setLayout(new GridLayout(1, 1));
        response.setBackground(PLUM);
        response.setForeground(WHITE);
        response.setLineWrap(true);
        response.setWrapStyleWord(true);
    }
}