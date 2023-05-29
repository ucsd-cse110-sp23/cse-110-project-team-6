package frontend;

import middleware.*;

import javax.swing.*;
import javax.sound.sampled.TargetDataLine;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Deprecated
/**
 * Class which contains the button for asking questions.
 */
public class NewQuestionPanel extends AppPanels {

    private TargetDataLine targetDataLine;
    private VoiceRecorder recorder;
    private SayItAssistant assistant;

    /**
     * Constructs the panel for new questions.
     * 
     * @param assistant:    assists with API calls
     * @param qna:          panel for questions and answers
     * @param history:      panel for the history
     */
    public NewQuestionPanel(SayItAssistant assistant, QnAPanel qna, HistoryPanel history) {
        this.setLayout(new GridLayout(0,1));
        this.assistant = assistant;
        this.recorder = new VoiceRecorder(targetDataLine);
        populateNewQuestionPanel(qna, history);
        
    }

    /**
     * Adds a new question button to the new question panel.
     * 
     * @param newQuestionButton: NewQuestionButton object which contains the new question button
     */
    public void addNewQuestionButton(NewQuestionButton newQuestionButton) {
        this.add(newQuestionButton);
    }

    /**
     * Populates the new question panel with a clickable button.
     * 
     * @param qna:      panel for questions and answers
     * @param history:  panel for the history
     */
    public void populateNewQuestionPanel(QnAPanel qna, HistoryPanel history) {

        NewQuestionButton newQuestionButton = new NewQuestionButton();    
        
        newQuestionButton.addMouseListener(new MouseAdapter() {

            // when pressed, voice recording will commence
            @Override
            public void mousePressed(MouseEvent e) {
                newQuestionButton.setEnabled(false);
                revalidate();
                recorder.startRecording();
            }

            // once released, voice recording will stop and the question / answer will be displayed
            @Override
            public void mouseReleased(MouseEvent e) {

                recorder.stopRecording();

                // displays the question and answer
                String[] response = assistant.respond();
                qna.setQuestion(new Question(response[0]));
                qna.setAnswer(new Answer(response[1]));
                history.revalidateHistory(qna);
                newQuestionButton.setEnabled(true);
                revalidate();
            }
        });
            
        add(newQuestionButton); // adds the new question button to the question panel
    }

}

/*
 * The new question button allows the user to record and submit questions.
 */
class NewQuestionButton extends AppButtons {

    // formatting for the new question button
    private final int newQuestionButtonWidth = 1200;
    private final int newQuestionButtonHeight = 50;
    private final static String newQuestionLabel = "New Question";

    /*
     * Creates and formats the new question button.
     */
    public NewQuestionButton() {
        super(newQuestionLabel);
        this.setBackground(GREEN);
        this.setForeground(BLACK);
        setHorizontalAlignment(SwingConstants.CENTER);
        setPreferredSize(new Dimension(newQuestionButtonWidth, newQuestionButtonHeight));
    }

}