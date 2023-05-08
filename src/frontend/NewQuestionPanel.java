package frontend;

import javax.swing.*;

import backend.History;
import backend.Question;
import backend.Answer;
import middleware.MockAPIRequest;
import middleware.SayItAssistant;

import java.util.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NewQuestionPanel extends AppPanels {

    JTextArea display = new JTextArea();
    JButton recordButton;
    MockAPIRequest chatGPT = new MockAPIRequest(new Question("This is the prompt"));
    MockAPIRequest whisper;

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
        newQuestionButton.addActionListener(
            e -> {
                    addRecordButton(newQuestionButton, display);
                    recordButton.setFont(myFont.getFont());
                    revalidate();
                    repaint();
                    newQuestionButton.setEnabled(false);
                
/*
    ***NOTES***: save new question and answer from the API to a new Question and Answser.  Then:
    Replace the new Question() below with the new question asked.  
    Replace the new Answer() with the new answer.

                    history.getHistoryGrabber().addQuestionAndAnswer(new Question("why is this the way it is"), new Answer("who knows"));
                    history.revalidateHistory(display);
                    revalidate();

*/
            }
        );
            
        this.add(newQuestionButton);
    }

    private void addRecordButton(NewQuestionButton newQuestionButton, DisplayPanel display) {
        // Create the record button
        recordButton = new JButton("Record");
        recordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                SayItAssistant assistant = new SayItAssistant(chatGPT, whisper);
                assistant.recordFile();
                String[] response = assistant.respond();
                display.answer.setText("Hi budd");
                display.question.setText("Good bud");
                newQuestionButton.setEnabled(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                removeRecordButton();
                newQuestionButton.setEnabled(true);
            }
        });

        Rectangle answerBounds = display.answer.getBounds();

        // Set position and size of record button
        int buttonWidth = 100;
        int buttonHeight = 30;
        int buttonX = answerBounds.x + (answerBounds.width - buttonWidth) / 2;
        int buttonY = answerBounds.y + (answerBounds.height - buttonHeight) / 2;
        recordButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);

        // Add the record button the the display panel
        this.add(recordButton);
        this.setComponentZOrder(recordButton, 0);
        this.setComponentZOrder(newQuestionButton, 1);
        revalidate();
        repaint();
    }

    private void removeRecordButton() {
        if (recordButton != null) {
            this.remove(recordButton);
            recordButton = null;
            revalidate();
            repaint();
        }
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