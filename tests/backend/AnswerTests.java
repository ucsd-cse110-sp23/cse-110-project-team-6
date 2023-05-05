package backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnswerTests {
    
    /*
     * Tests that a Answer object holds the answer it is intended to hold.
     */
    @Test
    public void testAnswer () {
        Answer answer = new Answer ("I am Giovanni");
        assertEquals("I am Giovanni", answer.getAnswer());
    }

    /*
     * Tests that an exception is thrown when a null answer is given.
     */
    @Test
    public void testNullAnswer () {

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            ()->{
                Answer nullAnswer = new Answer(null);
            });

        assertTrue(thrown.getMessage().contentEquals("Entered answer was null"));
    }
}