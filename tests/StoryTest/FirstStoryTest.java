package StoryTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import middleware.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Story test for verficiation that asked questions are stored within the history
 * 
 * For sake of testing, UI elements will not be factored in. Any UI elements which may have been
 * specified will be ommitted from description of the tests as (...) or brief context description.
 */
public class FirstStoryTest {
    
    private static final String EXPECT_HISTORY_PATH = 
        System.getProperty("user.dir") + "/bin/backend/history.json";
    private static final int MAX_WINDOW_QUESTION_SIZE = 10;
    
    private static HistoryManager historyManager;
    private static SayItAssistant sayItAssistant;
    private static MockWhisperRequest mockWhisperRequest;

    /**
     * Opens up SayIt Assistant before each test
     */
    @BeforeEach
    public void setUp() {
        openSayItAssistant();
    }

    /**
     * Erases the history file after each test
     */
    @AfterEach
    public void tearDown() {
        sayItAssistant = null;
        historyManager = null;
        File file = new File(EXPECT_HISTORY_PATH);
        file.delete();
    }

    /**
     * Function which mimics opening SayIt Assistant
     */
    private void openSayItAssistant() {
        mockWhisperRequest = new MockWhisperRequest();
        sayItAssistant = new SayItAssistant(mockWhisperRequest);
        historyManager = new HistoryManager(sayItAssistant);
    }

    /**
     * Function which mimics asking a question to SayIt Assistant
     */
    private void askQuestion(String question) {
        mockWhisperRequest.testString = question;
        sayItAssistant = new SayItAssistant(mockWhisperRequest);
        historyManager = new HistoryManager(sayItAssistant);
        sayItAssistant.respond();
    }

    /**
     * SayIt Assistant has no questions asked.
     * 
     * Given: History Helen has SayIt Assitant opened
     * And: Helen has not asked any questions
     * When: History Helen looks at the history
     * Then: History Helen sees that there are no questions asked
     */
    @Test
    public void testScenario1() {
        // Mock Instance of opening SayIt Assistant for the first time
        openSayItAssistant();

        // Check that history is empty (mimics looking at an empty history)
        assertTrue(historyManager.getHistorySize() == 0);

        // Verify that looking questions returns nothing
        assertEquals(historyManager.getQuestions(), new ArrayList<Question>());

        // Verify that looking for an answer to a question that has not been asked does nothing
        try {
            historyManager.getAnswer(0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    /**
     * New Question has been asked to SayIt Assistant.
     * 
     * Given: History Helen has finished asking the question "Who was Louis Braille?"
     * When: History Helen looks at the history
     * Then: The question prompt "Who was Louis Braille?" appears at the top of the History (Panel)
     * When: History Helen clicks on the question prompt "Who was Louis Braille?"
     * Then: SayIt Assistant will display the full response to the question "Who was Louis Braille?"
     */
    @Test 
    public void testScenario2() {
        String question = "Who was Louis Braille?";
        askQuestion(question);

        // Verify that the question was added to the history
        assertTrue(historyManager.getHistorySize() == 1);

        
        // Verify that the question was added to the history
        String receivedQuestion = historyManager.getQuestions().get(0).toString();
        assertEquals(receivedQuestion, question);

        // Verify that the answer to the question is correct
        assertEquals(historyManager.getAnswer(0).toString(), "This is the response to the prompt");
    }

    /**
     * Multiple Questions have been asked to SayIt Assistant (exceeding Display Window)
     * 
     * Given: History Helen finishes asking the question "What was utilized before Braille?"
     * And: There have been 10 questions asked (which exceed the display window)
     * When: History Helen looks at the history
     * Then: The question prompt "What was utilized before Braille?" is added to the History (Panel)
     * And: The rest of the questions are preserved within order of being asked
     * And: (The rest of the questions move down the History Panel) [Manual test]
     * And: (A scrolling bar appears on the History Panel) [Manual test]
     * When: History Helen (scrolls the bar to) the first question asked, "Who was Louis Braille?"
     * Then: SayIt Assistant will display the full response to the first question asked
     */
    @Test
    public void testScenario3() {
        String firstQuestion = "Who was Louis Braille?";
        String genericQuestion = "Why is Louis Braille?";
        String lastQuestion = "What was utilized before Braille?";

        // Ask first question
        askQuestion(firstQuestion);

        // Ask generic questions
        for (int i = 1; i < MAX_WINDOW_QUESTION_SIZE; i++) {
            askQuestion(genericQuestion);
        }

        // Ask last question
        askQuestion(lastQuestion);

        // Verify that the questions were added to the history
        assertTrue(historyManager.getHistorySize() == MAX_WINDOW_QUESTION_SIZE + 1);

        // Verify that the questions were added to the history
        String receivedFirstQuestion = historyManager.getQuestions().get(0).toString();
        assertEquals(receivedFirstQuestion, firstQuestion);

        String receivedGenericQuestion; 
        for (int i = 1; i < MAX_WINDOW_QUESTION_SIZE; i++) {
            receivedGenericQuestion = historyManager.getQuestions().get(i).toString();
            assertEquals(receivedGenericQuestion, genericQuestion);
        }

        String receivedLastQuestion = historyManager.getQuestions().get(10).toString();
        assertEquals(receivedLastQuestion, lastQuestion);
    }

    /**
     * Question within history has been deleted.
     * 
     * Given: The question "Who was Louis Braille?" has been asked
     * And: History Helen has selected the question "Who was Louis Braille?"
     * When: History Helen clicks on the delete button
     * Then: The question "Who was Louis Braille?" is removed from the History (Panel)
     */
    @Test
    public void testScenario4() {
        String question = "Who was Louis Braille?";
        askQuestion(question);

        // Verify that the question was added to the history
        assertTrue(historyManager.getHistorySize() == 1);

        // Verify that the question was added to the history
        String receivedQuestion = historyManager.getQuestions().get(0).toString();
        assertEquals(receivedQuestion, question);

        // Delete the question
        historyManager.delete(0);

        // Verify that the question was deleted from the history
        assertTrue(historyManager.getHistorySize() == 0);

        // Verify that looking questions returns nothing
        assertEquals(historyManager.getQuestions(), new ArrayList<Question>());

        // Verify that looking for an answer to a question that has not been asked does nothing
        try {
            historyManager.getAnswer(0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    /**
     * Question within history has been deleted (while current history exceeds display window).
     * 
     * Given: The History Panel is full by 10 questions
     * And: The question "Who was Louis Braille?" was the 3rd question asked
     * And: The question "What was used before Braille?" was the last question asked [11th]
     * And: History Helen has selected the question "Who was Louis Braille?"
     * When: History Helen clicks on the delete button
     * Then: The question "Who was Louis Braille?" is removed from the History (Panel)
     * And: The question "What was used before Braille?" is moved up to the 10th question
     * And: (The rest of the questions move up the History Panel) [Manual test]
     * And: (The scrolling bar remains at the bottom of the History Panel) [Manual test]
     */
    @Test
    public void testScenario5() {
        String thirdQuestion = "Who was Louis Braille?";
        String genericQuestion = "Why is Louis Braille?";
        String lastQuestion = "What was utilized before Braille?";

        // Ask first through second question
        for (int i = 0; i < 2; i++) {
            askQuestion(genericQuestion);
        }

        // Ask third question
        askQuestion(thirdQuestion);

        // Ask the fourth through ninth question
        for (int i = 3; i < MAX_WINDOW_QUESTION_SIZE; i++) {
            askQuestion(genericQuestion);
        }

        // Ask last question
        askQuestion(lastQuestion);

        // Verify that the questions were added to the history
        assertTrue(historyManager.getHistorySize() == MAX_WINDOW_QUESTION_SIZE + 1);

        // Delete the third question
        historyManager.delete(2);

        // Verify that the question was deleted from the history
        assertTrue(historyManager.getHistorySize() == MAX_WINDOW_QUESTION_SIZE);

        // Verify that the thirdQuestion was deleted from the history
        for (int i = 0; i < MAX_WINDOW_QUESTION_SIZE; i++) {
            assertNotEquals(historyManager.getQuestions().get(i).toString(), thirdQuestion);
        }

        // Verify that the last question was moved up to the 9th question
        String receivedLastQuestion = historyManager.getQuestions().get(9).toString();
        assertEquals(receivedLastQuestion, lastQuestion);
    }

    /**
     * Reopening SayIt Assistant after using it to ask questions.
     * 
     * Given: History Helen has finished asking the question "Who was Louis Braille?"
     * And: History Helen has finished asking the question "What was utilized before Braille?"
     * When: History Helen closes SayIt Assistant
     * And: History Helen reopens SayIt Assistant
     * Then: The question "Who was Louis Braille?" appears at the top of the History (Panel)
     * And: The question "What was utilized before Braille?" is at bottom of the History (Panel)
     */
    @Test
    public void testScenario6() {
        String firstQuestion = "Who was Louis Braille?";
        String lastQuestion = "What was utilized before Braille?";

        // Ask first question
        askQuestion(firstQuestion);

        // Ask last question
        askQuestion(lastQuestion);

        // Verify that the questions were added to the history
        assertTrue(historyManager.getHistorySize() == 2);

        // Verify that the questions were added to the history
        String receivedFirstQuestion = historyManager.getQuestions().get(0).toString();
        assertEquals(receivedFirstQuestion, firstQuestion);

        String receivedLastQuestion = historyManager.getQuestions().get(1).toString();
        assertEquals(receivedLastQuestion, lastQuestion);

        // Close SayIt Assistant
        sayItAssistant = null;
        historyManager = null;

        // Reopen SayIt Assistant
        openSayItAssistant();

        // Verify that the questions were added to the history
        assertTrue(historyManager.getHistorySize() == 2);

        // Verify that the questions were added to the history
        receivedFirstQuestion = historyManager.getQuestions().get(0).toString();
        assertEquals(receivedFirstQuestion, firstQuestion);

        receivedLastQuestion = historyManager.getQuestions().get(1).toString();
        assertEquals(receivedLastQuestion, lastQuestion);
    }
}
