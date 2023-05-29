package frontend;

import middleware.*;

import javax.swing.*;
import javax.sound.sampled.TargetDataLine;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class which contains the button for asking prompts.
 */
public class StartPanel extends AppPanels {

    private TargetDataLine targetDataLine;
    private VoiceRecorder recorder;
    private SayItAssistant assistant;
    private HistoryManager historyManager;
    /**
     * Constructs the panel for Start button.
     * 
     * @param assistant:    assists with API calls
     * @param qna:          panel for questions and answers
     * @param history:      panel for the history
     */
    public StartPanel(SayItAssistant assistant, PromptAndResponsePanel qna, HistoryPanel history, HistoryManager historyManager) {
        this.setLayout(new GridLayout(0,1));
        this.assistant = assistant;
        this.recorder = new VoiceRecorder(targetDataLine);
        this.historyManager = historyManager;
        populateStartPanel(qna, history);
        
    }

    /**
     * Adds a Start button to the Start panel.
     * 
     * @param StartButton: StartButton object which contains the Start button
     */
    public void addStartButton(StartButton StartButton) {
        this.add(StartButton);
    }

    /**
     * Populates the Start button panel with a clickable button.
     * 
     * @param qna:      panel for questions and answers
     * @param history:  panel for the history
     */
    public void populateStartPanel(PromptAndResponsePanel qna, HistoryPanel history) {

        StartButton StartButton = new StartButton();    
        
        StartButton.addMouseListener(new MouseAdapter() {

            // when pressed, voice recording will commence
            @Override
            public void mousePressed(MouseEvent e) {
                StartButton.setEnabled(false);
                revalidate();
                recorder.startRecording();
            }

            // once released, voice recording will stop and the question / answer will be displayed
            @Override
            public void mouseReleased(MouseEvent e) {

                recorder.stopRecording();

                // displays the question and answer
                String[] response = assistant.respond();
                qna.setPrompt(new Question(response[0]));
                qna.setResponse(new Answer(response[1]));
                history.revalidateHistory(qna);
                StartButton.setEnabled(true);
                revalidate();
            }
        });
            
        add(StartButton); // adds the new question button to the question panel
    }

}

/*
 * The new question button allows the user to record and submit questions.
 */
class StartButton extends AppButtons {

    // formatting for the new question button
    private final int StartButtonWidth = 1200;
    private final int StartButtonHeight = 50;
    private final static String StartButtonLabel = "Start";

    /*
     * Creates and formats the new question button.
     */
    public StartButton() {
        super(StartButtonLabel);
        this.setBackground(GREEN);
        this.setForeground(BLACK);
        setHorizontalAlignment(SwingConstants.CENTER);
        setPreferredSize(new Dimension(StartButtonWidth, StartButtonHeight));
    }

}