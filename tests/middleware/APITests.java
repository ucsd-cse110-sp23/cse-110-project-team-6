package middleware;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import backend.Question;


/**
 * This class contains all of the tests for ChatGPT and Whisper API requests
 */
public class APITests {
    
    private static final String EXPECT_CHAT = "This is the response to the prompt";
    private static final String EXPECT_WHISPER = "This is the transcription of the audio file";

    @Test
    public void testMockChat() {
        Question prompt = new Question("What is the meaning of life?");
        
        MockAPIRequest request = new MockAPIRequest(prompt);
        String response = request.callAPI();

        assertNotNull(response);
        assertTrue(response.contains(EXPECT_CHAT));
    }

    @Test 
    public void verifyChatInterface() {
        Question prompt = new Question("What is the meaning of life?");
        ChatGPTRequest request = new ChatGPTRequest(prompt);
        
        assertTrue(request instanceof IAPIRequest);
    }

    @Test
    public void testMockWhisper() {
        File file = new File("test.wav");
        MockAPIRequest request = new MockAPIRequest(file);
        String response = request.callAPI();

        assertNotNull(response);
        assertTrue(response.contains(EXPECT_WHISPER));
    }
}
