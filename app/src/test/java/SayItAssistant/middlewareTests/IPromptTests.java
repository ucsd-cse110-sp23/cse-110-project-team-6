package SayItAssistant.middlewareTests;

import SayItAssistant.middleware.IPrompt;
import SayItAssistant.middleware.Question;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains all of the tests for IPrompt.java and Question.java
 */

public class IPromptTests {

    @Test
    public void testCreatePrompt() {
        IPrompt prompt = new Question("Test prompt");
        
        assertNotNull(prompt);
        assertTrue(prompt instanceof Question);
    }

    @Test
    public void testToString() {
        IPrompt prompt = new Question("Test question");

        assertNotNull(prompt);
        assertEquals("Test question", prompt.toString());
    }

    @Test
    public void PromptConstructor() {
        Question question = new Question("Test question");
        
        assertEquals("Test question", question.toString());
    }

    @Test
    public void testSetPromptNumber() {
        Question question = new Question("Test question");

        question.setPromptNumber(10);
        assertEquals(10, question.getPromptNumber());
    }

    @Test
    public void testGetPromptNumber() {
        Question question = new Question("Test question");

        question.setPromptNumber(5);
        assertEquals(5, question.getPromptNumber());
    }
}
