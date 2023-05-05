package frontend;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import middleware.VoiceRecorder;

public class questionButton extends JButton {
    private Boolean isRecording; 
    private VoiceRecorder voiceRecorder = new VoiceRecorder();

    public questionButton(){
        this.setText("New Question");
        this.setBackground(Color.green);
        isRecording = false; 
    }

    // Optional functionality for button that can be deleted or used if desired.
    public void record(){
        this.voiceRecorder.startRecording();
    }

    public void stopRecording(){
        this.voiceRecorder.stopRecording();
    }

    public Boolean getStatus(){
        return isRecording; 
    }

}
