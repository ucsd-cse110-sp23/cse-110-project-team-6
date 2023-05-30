package SayItAssistant.frontend;

import java.util.ArrayList;

import javax.sound.sampled.TargetDataLine;
import javax.swing.*;

import SayItAssistant.middleware.IPrompt;
import SayItAssistant.middleware.IResponse;
import SayItAssistant.middleware.Observer;
import SayItAssistant.middleware.PromptResponsePair;
import SayItAssistant.middleware.SayItAssistant;
import SayItAssistant.middleware.Subject;
import SayItAssistant.middleware.VoiceRecorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;;

/*
 * The new question button allows the user to record and submit questions.
 */
public class StartButton extends AppButtons implements Subject {

    // formatting for the new question button
    private final int StartButtonWidth = 1200;
    private final int StartButtonHeight = 50;
    private final static String StartButtonLabel = "Start";
    private final static String RecordingLabel = "Recording...";
    private VoiceRecorder recorder;
    private TargetDataLine targetDataLine;
    private ArrayList<Observer> observers;
    private IPrompt prompt;
    private IResponse response;

    /*
     * Creates and formats the Start button
     */
    public StartButton(SayItAssistant assistant) {
        super(StartButtonLabel);
        // Set the font for the button
        this.setFont(this.myFont.getBoldFont());
        this.recorder = new VoiceRecorder(targetDataLine);
        this.setBackground(CREAM);
        this.setForeground(NIGHT);
        this.observers = new ArrayList<Observer>();
        setHorizontalAlignment(SwingConstants.CENTER);
        setPreferredSize(new Dimension(StartButtonWidth, StartButtonHeight));
        addMouseListener(new MouseAdapter() {

            // Commence voice recording once the button is pressed
            @Override
            public void mousePressed(MouseEvent e) {
                setEnabled(false);
                setText(RecordingLabel);
                setBackground(BEIGE);
                revalidate();
                recorder.startRecording();
            }

            // Terminate voice recording once the button is released
            @Override
            public void mouseReleased(MouseEvent e) {

                recorder.stopRecording();
                setBackground(CREAM);
                setText(StartButtonLabel);

                // Get the prompt and response from the assistant
                PromptResponsePair promptResponse = assistant.respond();

                prompt   = promptResponse.getPrompt();
                response = promptResponse.getResponse();

                notifyObservers();
                setEnabled(true);
                revalidate();
            }
        });
            
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        System.out.println("Start button is notifying observers");
        for (Observer o : observers) {
            o.update(prompt, response);
        }
    }
}