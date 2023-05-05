package middleware;

import java.io.*;
import javax.sound.sampled.*; // For sound recording and playback

public class VoiceRecorder {

    float sampleRate = 44100; // CD-quality audio
    int sampleSizeInBits = 16; // higher-fidelity audio
    int channels = 1; // Mono audio channel recording (less data to process)

    // Signed audio enables wider dynamic range of audio signal
    boolean signed = true;
    boolean bigEndian = true; // most common format for audio

    private AudioFormat format = 
        new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

    private TargetDataLine line;

    /**
     * Default constructor which initializes the audio format and obtains the system microphone
     */
    public VoiceRecorder() {
        // Specifies the type of audio line to obtain
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        // Obtains an audio line which can utilize system microphone to record audio
        try { this.line = (TargetDataLine) AudioSystem.getLine(info); } 
        catch (LineUnavailableException e) { e.printStackTrace(); }
    }

    /** 
     * Overloaded constructor which allows for a mock TargetDataLine to be passed in
     */
    public VoiceRecorder(TargetDataLine line) {
        this.line = new MockTargetDataLine();
    }

    /**
     * Getter method which returns the audio format of the recording
     */
    public AudioFormat getAudioFormat() {
        return format;
    }

    /**
     * Method which records audio from the microphone and saves it to a file
     * @param audioFile File to save the audio to
     */
    public void startRecording() {
        // Thread to record audio allowing other concurrent tasks (e.g., running the GUI)
        Thread t = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    try {
                        // Open the TargetDataLine for recording
                        line.open(format);

                        // Start the TargetDataLine
                        line.start();

                        // Create a new audio input stream from the TargetDataLine
                        AudioInputStream ais = new AudioInputStream(line);

                        // Write the audio data to the file
                        File audioFile = new File("prompt.wav");
                        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, audioFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        );
        t.start();
    }

    /**
     * Method to stop recording audio
     */
    public void stopRecording() {
        line.stop();
        line.close();
    }

    public static void main(String[] args) {
        VoiceRecorder vr = new VoiceRecorder();
        vr.startRecording();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vr.stopRecording();
    }
}