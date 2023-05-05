package middleware;

import java.io.*;
import javax.sound.sampled.*; // For sound recording and playback

public class VoiceRecorder {
    
    private AudioFormat format;
    private TargetDataLine line;

    /**
     * Getter method which returns the audio format of the recording
     * @return AudioFormat of the recording
     */
    private AudioFormat getAudioFormat() {
        // Samples of audio per second
        float sampleRate = 44100; // CD-quality audio
        
        // Bits within each sample which are digitally encoded
        int sampleSizeInBits = 16; // higher-fidelity audio

        // Number of audio channels in the recording
        int channels = 1; // Mono audio channel recording (less data to process)

        // Signed audio enables wider dynamic range of audio signal
        boolean signed = true;

        // Big endian is the most common format for audio data
        boolean bigEndian = true; // Big endian

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
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
                        // Get the audio format
                        format = getAudioFormat();

                        // Specifies the type of audio line to obtain
                        DataLine.Info  info = new DataLine.Info(TargetDataLine.class, format);

                        // Obtains an audio line which can utilize system microphone to record audio
                        line = (TargetDataLine) AudioSystem.getLine(info);

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
}
