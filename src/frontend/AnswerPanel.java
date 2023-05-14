package frontend;

import javax.swing.*;

import middleware.Answer;

import java.awt.*;

public class AnswerPanel extends AppPanels{

    private JTextPane answer = new JTextPane();
    private JScrollPane answerScrollPane = new JScrollPane
        (this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


    public AnswerPanel() {
        this.setLayout(new GridLayout(1, 1));
        answer.setBackground(LIGHT_GREY);
        answer.setForeground(WHITE);
        add(answer);
    }

    public void setAnswer(Answer answer) {
        this.answer.setText(answer.toString());
    }

    public JScrollPane getAnswerScrollPane() {
        return this.answerScrollPane;
    }
}
