package frontend;

import javax.sound.sampled.TargetDataLine;
import javax.swing.*;

import frontend.*;
import middleware.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class which contains the Buttons for asking and deleting questions
 */
public class NewQuestionPanel extends AppPanels {

     private TargetDataLine targetDataLine;
    private VoiceRecorder recorder;
    private SayItAssistant assistant;

    /**
     * Constructor for NewQuestionPanel class
     * @param myFont
     */
    public NewQuestionPanel(MyFont myFont, SayItAssistant assistant, QnAPanel qna, HistoryPanel history) {
        this.setLayout(new GridLayout(0,1));
        this.myFont = myFont;
        this.assistant = assistant;
        this.recorder = new VoiceRecorder(targetDataLine);
        populateNewQuestionPanel(qna, history);
        
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
    public void populateNewQuestionPanel(QnAPanel qna, HistoryPanel history) {
        NewQuestionButton newQuestionButton = new NewQuestionButton();
        //newQuestionButton.setFont(this.myFont.getFont());
       
        newQuestionButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                newQuestionButton.setEnabled(false);
                revalidate();
                recorder.startRecording();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                recorder.stopRecording();

                String[] response = assistant.respond();

                qna.setQuestion(new Question(response[0]));
                qna.setAnswer(new Answer(response[1]));
                history.revalidateHistory(qna);
                newQuestionButton.setEnabled(true);
                revalidate();
            }
        });
            
        add(newQuestionButton);
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
        setHorizontalAlignment(SwingConstants.CENTER);
        setPreferredSize(new Dimension(newQuestionButtonWidth, newQuestionButtonHeight));
    }

}