package frontend;

import middleware.HistoryManager;
import middleware.SayItAssistant;

import java.awt.*;

/*
 * Panel that contains all of the main action buttons (clear, delete, new question).
 */
public class ButtonPanel extends AppPanels{

    SayItAssistant assistant;

    /*
     * Creates the layout for the button panel.  The top half is for the delete and clear buttons, while the bottom half is 
     * for the new question button.
     * 
     * @param sayItAssistant:   helps with API calls
     * @param qna:              the panel that holds the questions and answers
     * @param history:          the panel that holds the history
     * @param historyManager:   manages the history of the app
     */
    public ButtonPanel(SayItAssistant sayItAssistant, QnAPanel qna, HistoryPanel history, HistoryManager historyManager) {

        // formatting and structuring the button panel
        this.assistant = sayItAssistant;
        this.setPreferredSize(new Dimension(400, 20));
        this.setLayout(new GridLayout(2,1));
        this.setBackground(GREY);

        // set the new question panel to be on the bottom and the rest of the buttons to be on top
        NewQuestionPanel newQuestionPanel = new NewQuestionPanel(assistant, qna, history);
        SecondaryButtonsPanel SecondaryButtonsPanel = new SecondaryButtonsPanel(historyManager, qna, history);
        addSecondaryButtonsPanel(SecondaryButtonsPanel);
        addNewQuestionPanel(newQuestionPanel);
        revalidate();
    }

    /*
     * Adds the new question panel to the button panel.
     * 
     * @param newQuestionPanel: the question panel to be added
     */
    public void addNewQuestionPanel(NewQuestionPanel newQuestionPanel) {
        add(newQuestionPanel, BorderLayout.SOUTH);
    }

    /*
     * Adds the rest of the buttons to the button panel.
     * 
     * @param secondaryButtonsPanel: the other buttons to be added
     */
    public void addSecondaryButtonsPanel(SecondaryButtonsPanel secondaryButtonsPanel) {
        add(secondaryButtonsPanel, BorderLayout.NORTH);
    }
}