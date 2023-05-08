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
import java.io.File;

public class NewQuestionPanel extends AppPanels {

    JTextArea display = new JTextArea();
    JButton recordButton;
    MockAPIRequest chatGPT = new MockAPIRequest(new Question("This is the prompt"));
    MockAPIRequest whisper = new MockAPIRequest(new File("test.wav"));

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
                    addRecordButton(newQuestionButton, display, history);
                    recordButton.setFont(myFont.getFont());
                   
                    repaint();
                    newQuestionButton.setEnabled(false);
                    revalidate();
/*
    ***NOTES***: save new question and answer from the API to a new Question and Answser.  Then:
    Replace the new Question() below with the new question asked.  
    Replace the new Answer() with the new answer.

                    
                    revalidate();

*/
            }
        );
            
        this.add(newQuestionButton);
    }

    private void addRecordButton(NewQuestionButton newQuestionButton, DisplayPanel display, HistoryPanel history) {
        // Create the record button
        recordButton = new JButton("Record");
        recordButton.addMouseListener(new MouseAdapter() {
            SayItAssistant assistant = new SayItAssistant(chatGPT, whisper);
            @Override
            public void mousePressed(MouseEvent e) {
                assistant.startRecording();
                String[] response = assistant.respond();
                display.answer.setText("Hi budd");
                display.question.setText("Good bud");
                newQuestionButton.setEnabled(true);
                history.getHistoryGrabber().addQuestionAndAnswer(new Question(response[0]), new Answer(response[1]));
                history.revalidateHistory(display);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                assistant.stopRecording();
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