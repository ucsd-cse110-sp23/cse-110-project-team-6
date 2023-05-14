package frontend;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import middleware.Question;
import middleware.Answer;
import middleware.HistoryManager;

/**
 * Displays history of questions and answers
 */
public class HistoryPanel extends AppPanels {
    
    private JScrollPane scrollPane = new JScrollPane
        (this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    private HistoryManager historyManager;

    /**
     * Constructor for HistoryPanel class
     * @param historyManager
     */
    public HistoryPanel(HistoryManager historyManager) {
        this.setLayout(new GridLayout(0, 1));
        this.setBackground(BLACK);
        this.historyManager = historyManager;
    }

    /**
     * Revalidates (updates) the history panel
     * @param display DisplayPanel object which contains the display panel
     */
    public void revalidateHistory(QnAPanel qnaPanel) {
        this.removeAll();
        this.populateHistoryPanel(qnaPanel);
        revalidate();
    }

    /**
     * Returns the scroll pane
     * @return JScrollPane object which contains the scroll pane
     */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * Adds a history button to the history panel
     * @param historyButton HistoryButton object which contains the history button
     */
    public void addHistoryButton(HistoryButton historyButton) {
        this.add(historyButton);
    }

    /**
     * Returns the history manager
     * @return HistoryManager object
     */
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    /**
     * Populates the history panel with the history of questions and answers
     * @param display DisplayPanel object which contains the display panel
     */
    public void populateHistoryPanel(QnAPanel qnaPanel) {

        ArrayList<Question> questions = historyManager.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            Answer answer = historyManager.getAnswer(i);
            HistoryButton historyButton = new HistoryButton(i, question.toString());
            
            historyButton.setFont(this.myFont.getFont());
            
            historyButton.addActionListener(e -> {
                qnaPanel.setQuestion(question);
                qnaPanel.setAnswer(answer);
                qnaPanel.setFont(this.myFont.getFont());
                qnaPanel.setForeground(WHITE);
            });

            this.addHistoryButton(historyButton);
        }
    }
}


class HistoryButton extends AppButtons {
    
    public HistoryButton(int id, String displayText) {
        super(displayText);
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
