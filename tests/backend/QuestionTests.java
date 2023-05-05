package backend;

import org.junit.Test;
import static org.junit.Assert.*;

public class QuestionTests {
    
    /*
     * Tests that a Question object holds the question it is intended to hold.
     */
    @Test
    public void testQuestion () {
        Question question = new Question("Who am I?");
        assertEquals("Who am I?", question.getQuestion());
    }

    /*
     * Tests that an exception is thrown when a null question is asked.
     */
    @Test
    public void testNullQuestion () {

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            ()->{
                Question nullQuestion = new Question(null);
            });

        assertTrue(thrown.getMessage().contentEquals("Entered question was null"));
    }
}