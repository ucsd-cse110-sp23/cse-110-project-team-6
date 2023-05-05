package middleware;

import javax.sound.sampled.*; // For sound recording and playback
import org.junit.*;
import static org.junit.Assert.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class voiceRecorderTests {
    private VoiceRecorder vr;

    // Sets up the audio format and mock TargetDataLine
    @Before
    public void setUp() {
        TargetDataLine line = new MockTargetDataLine();
        vr = new VoiceRecorder(line);
    }

    @After
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
        assertTrue("Autio file was not created", Files.exists(path));
    }
}
