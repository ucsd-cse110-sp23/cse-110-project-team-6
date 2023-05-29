package frontend;

import middleware.Answer;
import middleware.HistoryManager;
import middleware.Question;

import javax.swing.*;
import java.awt.*;

@Deprecated
/*
 * This panel simply contains the clear all button.
 */
public class ClearAllPanel extends JPanel {

    /*
     * Initializes and structures the panel for the clear all button.
     * 
     * @param historyManager:   manages the history
     * @param qna:              panel for the questions / answers
     * @param history:          panel for the history
     */
    public ClearAllPanel(HistoryManager historyManager, PromptAndResponsePanel qna, HistoryPanel history) {
        this.setLayout(new GridLayout(1,1));

        // creates the button and adds it to the panel to be displayed
        ClearAllButton clearAllButton = new ClearAllButton(historyManager, qna, history);
        this.addClearAllButton(clearAllButton);
    }

    /*
     * Adds the clear all button to the clear all panel.
     * 
     * @param clearAllButton: the button to be added
     */
    public void addClearAllButton(ClearAllButton clearAllButton) {
        this.add(clearAllButton);
    }


}

/*
 * The clear all button clears all of the questions and answers from the history and has the 
 * history panel update accordingly.
 */
class ClearAllButton extends AppButtons {

    // formatting for the clear all button
    private final int ClearAllButtonWidth = 600;
    private final int ClearAllButtonHeight = 50;
    private final static String clearAllLabel = "Clear All";


    /*
     * Creates the button to clear everything from the history.
     * 
     * @param historyManager:   manages the history
     * @param qna:              panel for the questions and answers
     * @param history:          panel for the history
     */
    public ClearAllButton(HistoryManager historyManager, PromptAndResponsePanel qna, HistoryPanel history) {

        // formats the clear all button
        super(clearAllLabel);
        this.setBackground(TEAL);
        this.setForeground(BLACK);
        setHorizontalAlignment(SwingConstants.CENTER);
        setPreferredSize(new Dimension(ClearAllButtonWidth, ClearAllButtonHeight));

        // When the clear all button is pressed, the history gets deleted, the question / answer panels
        // are returned to their original state, and the display is updated.
        addActionListener(e -> {
            historyManager.clearAll();
            history.revalidateHistory(qna);
            qna.setPrompt(new Question("Welcome to SayIt Assistant"));
            qna.setResponse(new Answer(""));
            revalidate();
            qna.isSet = false; 
        });
    }

}