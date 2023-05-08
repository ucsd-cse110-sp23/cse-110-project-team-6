package middleware;

import java.io.File;

/**
 * This mocks the SayItAssistant class for testing purposes
 */
public class MockAssistant extends SayItAssistant {
    
    /**
     * Constructor for MockAssistant class
     * 
     * @param historyGrabber HistoryGrabber object which contains the history of
     *                       questions and answers
     */
    public MockAssistant(IAPIRequest whisperRequest) {
        super(whisperRequest);
    }

    @Override
    public String[] respond() {

        String[] mockResponse = new String[2];
        mockResponse[0] = "This is the question";
        mockResponse[1] = "This is the response";

        return mockResponse;
    }
}
