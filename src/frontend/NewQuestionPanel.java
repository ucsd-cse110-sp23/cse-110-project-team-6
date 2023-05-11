package frontend;

import javax.sound.sampled.TargetDataLine;
import javax.swing.*;

import middleware.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class which contains the Buttons for asking and deleting questions
 */
public class NewQuestionPanel extends AppPanels {

    //JTextArea display = new JTextArea();
    private JButton recordButton;
    private TargetDataLine targetDataLine;
    private VoiceRecorder recorder;
    private SayItAssistant assistant;

    /**
     * Constructor for NewQuestionPanel class
     * @param myFont
     */
    public NewQuestionPanel(MyFont myFont, SayItAssistant assistant) {
        this.setLayout(new GridLayout(0,1));
        this.setBackground(BLACK);
        this.myFont = myFont;
        this.assistant = assistant;
        this.recorder = new VoiceRecorder(targetDataLine);
    }

    /**
     * Adds a new question button to the new question panel
     * @param newQuestionButton NewQuestionButton object which contains the new question button
     */
    public void addNewQuestionButton(NewQuestionButton newQuestionButton) {
        this.add(newQuestionButton);
    }

    /**
     * Populates the new question panel
     * @param display
     * @param history
     */
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

    /**
     * Adds a record button to the new question panel
     * @param newQuestionButton
     * @param display
     * @param history
     */
    private void addRecordButton(NewQuestionButton newQuestionButton, DisplayPanel display, HistoryPanel history) {
        // Create the record button
        recordButton = new JButton("Record");
        recordButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                recorder.startRecording();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                recorder.stopRecording();

                String[] response = assistant.respond();

                display.answer.setText(response[1]);
                display.question.setText(response[0]);
                
                history.revalidateHistory(display);

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

    /**
     * Removes the record button from the new question panel
     */
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