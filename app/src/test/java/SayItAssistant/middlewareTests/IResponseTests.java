package SayItAssistant.middlewareTests;

import SayItAssistant.middleware.IResponse;
import SayItAssistant.middleware.Answer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains all of the tests for IResponse.java and Answer.java
 */
public class IResponseTests {

    @Test
    public void testCreateResponse() {
        IResponse response = new Answer("Test answer");

        assertNotNull(response);
        assertTrue(response instanceof Answer);
    }

    @Test
    public void testToString() {
        IResponse response = new Answer("Test answer");

        assertNotNull(response);
        assertEquals("Test answer", response.toString());
    }

    @Test
    public void testAnswerConstructor() {
        Answer answer = new Answer("Test answer");
        
        assertEquals("Test answer", answer.toString());
    }

}

