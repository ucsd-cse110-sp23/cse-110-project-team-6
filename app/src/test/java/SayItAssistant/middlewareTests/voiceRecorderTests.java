package SayItAssistant.middlewareTests;

import javax.sound.sampled.*; // For sound recording and playback
import org.junit.jupiter.api.*;

import SayItAssistant.middleware.MockTargetDataLine;
import SayItAssistant.middleware.VoiceRecorder;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class voiceRecorderTests {
    private VoiceRecorder vr;

    // Sets up the audio format and mock TargetDataLine
    @BeforeEach
    public void setUp() {
        TargetDataLine line = new MockTargetDataLine();
        vr = new VoiceRecorder(line);
    }

    @AfterEach
    public void tearDown() {
        vr = null;
    }

    @Test
    public void getAudioFormat() {
        AudioFormat format = vr.getAudioFormat();
        assertNotNull(format);
        assertEquals(44100, format.getSampleRate(), 0.1);
        assertEquals(16, format.getSampleSizeInBits());
        assertEquals(1, format.getChannels());
        assertEquals(true, format.isBigEndian());
    }

    @Test
    public void testRecording() {
        vr.startRecording();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vr.stopRecording();

        Path path = Path.of("prompt.wav");
        assertTrue(Files.exists(path));
    }

}
