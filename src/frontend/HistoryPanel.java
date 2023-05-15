package frontend;

import middleware.Question;
import middleware.Answer;
import middleware.HistoryManager;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;


/**
 * Displays history of questions and answers.
 */
public class HistoryPanel extends AppPanels {
    
    // wraps the history panel and adds a scrollbar to it when needed
    private JScrollPane scrollPane = new JScrollPane
        (this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    private HistoryManager historyManager;  // manages the history

    /**
     * Constructor for HistoryPanel class.
     * 
     * @param historyManager: manages the history
     */
    public HistoryPanel(HistoryManager historyManager) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(BLACK);
        this.historyManager = historyManager;
    }

    /**
     * Revalidates (updates) the history panel.
     * 
     * @param qnaPanel: the panel that holds the current question and answer
     */
    public void revalidateHistory(QnAPanel qnaPanel) {
        this.removeAll();
        this.populateHistoryPanel(qnaPanel);
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

    /**
     * Returns the history manager.
     * 
     * @return HistoryManager: manages the history
     */
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    /**
     * Populates the history panel with the history of questions and answers
     * 
     * @param qnaPanel: panel that contains the current question and answer
     */
    public void populateHistoryPanel(QnAPanel qnaPanel) {

        ArrayList<Question> questions = historyManager.getQuestions();  // list of the questions that have been asked

        // for each question, creates a button that when clicked displays the full question and the answer associated with it
        for (int i = 0; i < questions.size(); i++) {

            // sets up the question and answer that are to be associated with the button
            Question question = questions.get(i);
            //set the question with its index from the history:
            question.setQestionNumber(i);
            Answer answer = historyManager.getAnswer(i);
            HistoryButton historyButton = new HistoryButton(i, question.toString());
            
            historyButton.setFont(this.myFont.getFont());
            
            // updates the question and answer panels when clicked
            historyButton.addActionListener(e -> {
                qnaPanel.setQuestion(question);
                qnaPanel.setAnswer(answer);
                qnaPanel.setFont(this.myFont.getFont());
                qnaPanel.setForeground(WHITE);  
            });

            this.addHistoryButton(historyButton); // add the button to the display
        }
    }
}

/*
 * The history button displays the full question and answer associated with it when pressed.
 */
class HistoryButton extends AppButtons {
    
    /*
     * Creates the history button.
     * 
     * @param id:           the question number
     * @param displayText:  the text to be displayed
     */
    public HistoryButton(int id, String displayText) {
        super(displayText + " ".repeat(100));
        this.buttonWidth = 200;
        this.buttonHeight = 50;
        this.id = id;
        this.setBackground(BLACK);
        this.setForeground(WHITE);
        setHorizontalAlignment(SwingConstants.LEFT);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setPreferredSize(new Dimension(buttonWidth, buttonHeight));
    }
}