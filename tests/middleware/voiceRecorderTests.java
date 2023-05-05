package middleware;

import org.junit.Test;
import static org.junit.Assert.*;

public class voiceRecorderTests {
    
    @Test
    public void testRecording() {
        VoiceRecorder vr = new VoiceRecorder();
        vr.startRecording();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vr.stopRecording();
    }
}
