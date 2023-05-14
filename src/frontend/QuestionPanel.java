package frontend;

import javax.swing.*;

import middleware.Question;

import java.awt.*;

public class QuestionPanel extends AppPanels{
    private JTextPane question = new JTextPane();

    public QuestionPanel() {
        this.setLayout(new GridLayout(1, 1));
        question.setBackground(GREY);
        question.setForeground(WHITE);
        question.setAlignmentX(BOTTOM_ALIGNMENT);
        question.setAlignmentY(BOTTOM_ALIGNMENT);
        question.setText("Welcome to SayIt Assistant");
        add(question);
    }

    public void setQuestion(Question question) {
        this.question.setText(question.toString());
    }
}
