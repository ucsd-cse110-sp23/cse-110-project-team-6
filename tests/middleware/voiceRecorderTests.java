package middleware;

import middleware.sayit.components.VoiceRecorder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
