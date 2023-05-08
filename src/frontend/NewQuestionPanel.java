package frontend;

import javax.swing.*;

import backend.Question;
import backend.Answer;

import java.util.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NewQuestionPanel extends AppPanels {

    JTextArea display = new JTextArea();

    public NewQuestionPanel(MyFont myFont) {
        this.setLayout(new GridLayout(0,1));
        this.setBackground(BLACK);
        this.myFont = myFont;
        
    }

    public void addNewQuestionButton(NewQuestionButton newQuestionButton) {
        this.add(newQuestionButton);
    }

    public void populateNewQuestionPanel(DisplayPanel display, HistoryPanel history) {
        NewQuestionButton newQuestionButton = new NewQuestionButton();
        newQuestionButton.setFont(this.myFont.getFont());
        newQuestionButton.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
/*
    ***NOTES***: save new question and answer from the API to a new Question and Answser.  Then:
    Replace the new Question() below with the new question asked.  
    Replace the new Answer() with the new answer.

                    history.getHistoryGrabber().addQuestionAndAnswer(new Question("why is this the way it is"), new Answer("who knows"));
                    history.revalidateHistory(display);
                    revalidate();

*/
                }
            }
        );
            
        this.add(newQuestionButton);
    }

}

class NewQuestionButton extends AppButtons {

    private final int newQuestionButtonWidth = 1200;
    private final int newQuestionButtonHeight = 50;
    private final static String newQuestionLabel = "New Question";

    public NewQuestionButton() {
        super(newQuestionLabel);
        this.setBackground(GREEN);
        this.setForeground(WHITE);
        setHorizontalAlignment(SwingConstants.LEFT);

        setAlignmentX(Component.LEFT_ALIGNMENT);
        setPreferredSize(new Dimension(newQuestionButtonWidth, newQuestionButtonHeight));
    }

}