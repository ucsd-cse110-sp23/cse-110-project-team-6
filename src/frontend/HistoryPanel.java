package frontend;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import middleware.Question;
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
     * @param myFont MyFont object which contains the font
     */
    public HistoryPanel(MyFont myFont, HistoryManager historyManager) {
        this.setLayout(new GridLayout(0, 1));
        this.setBackground(BLACK);
        this.myFont = myFont;
        this.historyManager = historyManager;
    }

    /**
     * Revalidates (updates) the history panel
     * @param display DisplayPanel object which contains the display panel
     */
    public void revalidateHistory(DisplayPanel display) {
        this.removeAll();
        this.populateHistoryPanel(display);
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
    public void populateHistoryPanel(DisplayPanel display) {
        ArrayList<Question> questions = historyManager.getQuestions();
        
        for (int i = 0; i < questions.size(); i++) {
            String question = questions.get(i).getQuestion();
            String answer   = historyManager.getAnswer(i).getAnswer();
            HistoryButton historyButton = new HistoryButton(i, question);
            
            historyButton.setFont(this.myFont.getFont());
            
            historyButton.addActionListener(e -> {
                display.question.setText(question);
                display.answer.setText(answer);
                display.setFont(this.myFont.getFont());
                display.setForeground(WHITE);
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
