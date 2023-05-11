package middleware;

public class MockWhisperRequest implements IAPIRequest {

    /**
     * Constructor for MockWhisperRequest class
     */
    public MockWhisperRequest() {}

    @Override
    public String callAPI() {
        return "This is the transcription of the audio file";
    }
    
}
