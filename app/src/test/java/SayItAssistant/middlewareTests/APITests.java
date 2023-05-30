package SayItAssistant.middlewareTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import SayItAssistant.middleware.ChatGPTRequest;
import SayItAssistant.middleware.IAPIRequest;
import SayItAssistant.middleware.MockChatGPTRequest;
import SayItAssistant.middleware.MockWhisperRequest;
import SayItAssistant.middleware.Question;


/**
 * This class contains all of the tests for ChatGPT and Whisper API requests
 */
public class APITests {
    
    private static final String EXPECT_CHAT = "This is the response to the prompt";
    private static final String EXPECT_WHISPER = "This is the transcription of the audio file";

    @Test
    public void testMockChatGPT() {
        Question prompt = new Question("What is the meaning of life?");
        
        IAPIRequest chatgpt = new MockChatGPTRequest(prompt);
        String response = chatgpt.callAPI();

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
        IAPIRequest whisperRequest = new MockWhisperRequest();
        String response = whisperRequest.callAPI();

        assertNotNull(response);
        assertTrue(response.contains(EXPECT_WHISPER));
    }
}
