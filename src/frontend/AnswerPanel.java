package frontend;

import javax.swing.*;

import middleware.Answer;

import java.awt.*;

public class AnswerPanel extends AppPanels{

    private JTextPane answer = new JTextPane();

    public AnswerPanel() {
        this.setLayout(new GridLayout(1, 1));
        answer.setBackground(LIGHT_GREY);
        answer.setForeground(WHITE);
        add(answer);
    }

    public void setAnswer(Answer answer) {
        this.answer.setText(answer.toString());
    }

}
