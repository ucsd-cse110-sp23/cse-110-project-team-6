package frontend;

import middleware.HistoryManager;

import javax.swing.*;
import java.awt.*;

/*
 * Panel that contains the secondary function buttons (delete, clear all).
 */
public class SecondaryButtonsPanel extends AppPanels{

    /*
     * Constructs the panel for the secondary function buttons.
     * There is a panel for deleting and clearing all.
     * 
     * @param historyManager:   manages the history
     * @param qnaPanel:         panel for the questions and answers
     * @param history:          panel for the history
     */
    public SecondaryButtonsPanel(HistoryManager historyManager, QnAPanel qna, HistoryPanel history) {
        this.setPreferredSize(new Dimension(400,20));
        this.setLayout(new GridLayout(1,2));

        ClearAllPanel clearAllPanel = new ClearAllPanel(historyManager, qna, history);

        this.addDeletePanel();
        this.addClearAllPanel(clearAllPanel);
       
    }

    /*
     * Adds the delete panel to the display.
     */
    public void addDeletePanel() {
        JTextArea jt = new JTextArea();
        jt.setFont(myFont.getFont());
        jt.setText("Delete Button Area");
        jt.setBackground(TEAL);
        jt.setBorder(BorderFactory.createLineBorder(BLACK));
        add(jt);
    }

    /*
     * Adds the clear all panel to the display.
     * 
     * @param clearAllPanel: panel to be added
     */
    public void addClearAllPanel(ClearAllPanel clearAllPanel) {
        add(clearAllPanel);
    }

}