package frontend;

//import javax.print.attribute.standard.JobHoldUntil;
import javax.swing.*;
import java.awt.*;

public class DisplayPanel extends AppPanels {

    JTextPane question = new JTextPane();
    JTextArea answer   = new JTextArea();

    JScrollPane scrollQuestion = new JScrollPane
        (question, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JScrollPane scrollAnswer = new JScrollPane
        (answer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    JButton newQuestionButton;

    public DisplayPanel() {
        this.setLayout(new GridLayout(3,1));
        add(scrollQuestion);
        add(scrollAnswer);
        setBackground(GREY);
        question.setBackground(GREY);
        question.setForeground(WHITE);
        question.setAlignmentX(BOTTOM_ALIGNMENT);
        question.setAlignmentY(BOTTOM_ALIGNMENT);
        question.setText("Welcome to SayIt Assistant");

        answer.setBackground(LIGHT_GREY);
        answer.setForeground(WHITE);
    }

    public void addNewQuestionPanel(NewQuestionPanel newQuestionPanel) {
        add(newQuestionPanel, BorderLayout.SOUTH);
    }
    
}