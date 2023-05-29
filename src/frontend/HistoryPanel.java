package frontend;

import middleware.HistoryButton;

import javax.swing.*;

/**
 * Displays history of questions and answers.
 */
public class HistoryPanel extends AppPanels {
    
    // wraps the history panel and adds a scrollbar to it when needed
    private JScrollPane scrollPane = new JScrollPane
        (this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    /**
     * Constructor for HistoryPanel class.
     */
    public HistoryPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(BLACK);
    }

    /**
     * Revalidates (updates) the history panel.
     * 
     * @param promptAndResponsePanel: the panel that holds the current question and answer
     */
    public void revalidateHistory(PromptAndResponsePanel promptAndResponsePanel) {
        this.removeAll();
        //this.populateHistoryPanel(promptAndResponsePanel);
        revalidate();
        repaint();
    }

    /**
     * Returns the scroll pane.
     * 
     * @return JScrollPane: object which contains the scroll pane
     */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * Adds a history button to the history panel.
     * 
     * @param historyButton: the history button to be added
     */
    public void addHistoryButton(HistoryButton historyButton) {
        this.add(historyButton);
    }

}