package SayItAssistant.middleware;

public class MockWhisperRequest implements IAPIRequest {

    public String testString = "Question. This is the transcription of the audio file";

    /**
     * Constructor for MockWhisperRequest class
     */
    public MockWhisperRequest() {}

    @Override
    public String callAPI() {
        return testString;
    }
    
}