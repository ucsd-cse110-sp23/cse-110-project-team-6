package middleware;

import java.util.ArrayList;

import javax.sound.sampled.TargetDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;;

/*
 * The new question button allows the user to record and submit questions.
 */
public class StartButton extends frontend.AppButtons implements StartButtonSubject {

    // formatting for the new question button
    private final int StartButtonWidth = 1200;
    private final int StartButtonHeight = 50;
    private final static String StartButtonLabel = "Start";
    private VoiceRecorder recorder;
    private TargetDataLine targetDataLine;
    private ArrayList<StartButtonObserver> observers;

    /*
     * Creates and formats the new question button.
     */
    public StartButton(SayItAssistant assistant) {
        super(StartButtonLabel);
        this.recorder = new VoiceRecorder(targetDataLine);
        this.setBackground(GREEN);
        this.setForeground(BLACK);
        this.observers = new ArrayList<StartButtonObserver>();
        setHorizontalAlignment(SwingConstants.CENTER);
        setPreferredSize(new Dimension(StartButtonWidth, StartButtonHeight));
        addMouseListener(new MouseAdapter() {

            // when pressed, voice recording will commence
            @Override
            public void mousePressed(MouseEvent e) {
                setEnabled(false);
                revalidate();
                recorder.startRecording();
            }

            // once released, voice recording will stop and the question / answer will be displayed
            @Override
            public void mouseReleased(MouseEvent e) {

                recorder.stopRecording();

                // displays the question and answer
                String[] response = assistant.respond();

                // **************************TODO: CHANGE THIS TO WORK FOR ALL PROMPT / RESPONSE TYPES**********************************
                notifyStartButtonObservers(new Question(response[0]), new Answer(response[1]));
                setEnabled(true);
                revalidate();
            }
        });
            
    }

    @Override
    public void registerStartButtonObserver(StartButtonObserver o) {
        observers.add(o);
    }

    @Override
    public void removeHStartButtonObserver(StartButtonObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyStartButtonObservers(IPrompt prompt, IResponse response) {
        System.out.println("Start button is notifying observers");
        for (StartButtonObserver o : observers) {
            o.update(prompt, response);
        }
    }
}