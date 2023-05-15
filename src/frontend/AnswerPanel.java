package frontend;

import middleware.Answer;

import javax.swing.*;
import java.awt.*;

/*
 * Panel that contains the area for answers to be displayed.
 */
public class AnswerPanel extends AppPanels{

    private JTextArea answer = new JTextArea(); // area for the answer to be displayed with text
    private JScrollPane answerScrollPane = new JScrollPane
        (this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);   // wraps the answer area and allows it to be scrollable

    /*
     * Initializes and formats the panel for the answer.
     */
    public AnswerPanel() {
        this.setLayout(new GridLayout(1, 1));
        answer.setBackground(LIGHT_GREY);
        answer.setForeground(WHITE);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        add(answer);
    }

    /*
     * Allows the answer area to be explicitly set.
     * 
     * @param answer: The answer to be displayed
     */
    public void setAnswer(Answer answer) {
        this.answer.setText(answer.toString());
    }

    /*
     * Returns the answer area with the scrollbar.
     * 
     * @return JScrollPane: the answer pane with scrollbar
     */
    public JScrollPane getAnswerScrollPane() {
        return this.answerScrollPane;
    }
}