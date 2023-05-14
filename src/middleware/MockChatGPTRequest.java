package middleware;

public class MockChatGPTRequest implements IAPIRequest {

    /**
     * Constructor for MockChatGPTRequest class
     */
    public MockChatGPTRequest(Question prompt) {}

    @Override
    public String callAPI() {
        return "This is the response to the prompt";
    }
    
}