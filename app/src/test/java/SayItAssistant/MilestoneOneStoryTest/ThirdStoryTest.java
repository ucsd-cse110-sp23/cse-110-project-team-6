package SayItAssistant.MilestoneOneStoryTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import SayItAssistant.middleware.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Story test for verficiation that vocalized questions are recorded.
 * 
 * For sake of testing, UI elements will not be factored in. Any UI elements which may have been
 * specified will be ommitted from description of the tests as (...) or brief context description.
 */
public class ThirdStoryTest {
    /**
     * New Question button is pressed to ask a new question
     * 
     * Given: History Helen has pressed on the new question button
     * When: History Helen holds the voice prompt button 
     * And: Helen starts asking the SayIt Assistant the prompt "Summarize the history of inequality of the visually impaired…"
     * Then: SayIt Assistant’s voice prompt button changes color 
     * And: Helen’s speech is recorded
     * When: History Helen continues to expand on her question by asking "...in relation to literacy before Braille."
     * Then: Helen’s speech will still be recordedGiven: History Helen has SayIt Assitant opened
     * And: Helen has not asked any questions
     * When: History Helen looks at the history
     * Then: History Helen sees that there are no questions asked
     */
    @Test
    public void testScenario1() {
        /*
         * This is tested manually, as everything in this scenario is based around the UI
         */
    }

    /*
     * New question button is depressed to stop the question
     * 
     * Given: History Helen has finished asking a question about "Summarizing the history of inequality of the visually impaired in relation to literacy before Braille"
     * When: History Helen releases the voice prompt button
     * Then: SayIt Assistant transcribes her question (User Story 3)
     * And: SayIt Assistant displays the answer to her question.
     */
    @Test
    public void TestScenario2() {
        /*
         * This is also tested manually, as everything in this scenario is also based around the UI.
         */
    }
    
}