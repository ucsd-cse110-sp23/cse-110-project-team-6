package middleware;

import java.io.File;

import backend.Question;

public class MockAPIRequest implements IAPIRequest {

    private boolean isChatGPTRequest;

    /**
     * Constructor which mimics expectation of ChatGPTRequest constructor
     */
    public MockAPIRequest(Question prompt) {
        isChatGPTRequest = true;
    }

    /**
     * Constructor which mimicsc expectation of WhisperRequest constructor
     */
    public MockAPIRequest(File file) {
        isChatGPTRequest = false;
    }

    @Override
    public String callAPI() {
        if (isChatGPTRequest)
            return "This is the response to the prompt";
        else
            return "This is the transcription of the audio file";
    }
    
}
