package SayItAssistant.middleware;

public class MockChatGPTRequest implements IAPIRequest {

    /**
     * Constructor for MockChatGPTRequest class
     */
    public MockChatGPTRequest(IPrompt prompt) {}

    @Override
    public String callAPI() {
        return "This is the response to the prompt";
    }
    
}