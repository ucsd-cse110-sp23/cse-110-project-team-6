package SayItAssistant.MilestoneOneStoryTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import SayItAssistant.Server;
import SayItAssistant.middleware.*;

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
        System.getProperty("user.dir") + "/history.json";
    private static final String EXPECT_DATA_PATH =
        System.getProperty("user.dir") + "/data.json";
    
    private static final int MAX_WINDOW_QUESTION_SIZE = 10;
    

    private static final String TEST_USER     = "test";
    private static final String TEST_PASSWORD = "password";
    private static HistoryManager historyManager;
    private static SayItAssistant sayItAssistant;
    private static MockWhisperRequest mockWhisperRequest;

    /**
     * Opens up SayIt Assistant before each test
     */
    @BeforeEach
    public void setUp() {
        try {
            Server.startServer();
        } catch (Exception e) {
            assertTrue(false);
        }
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
        file = new File(EXPECT_DATA_PATH);
        file.delete();
        Server.stopServer();
    }

    /**
     * Function which mimics opening SayIt Assistant
     */
    private void openSayItAssistant() {
        mockWhisperRequest = new MockWhisperRequest();
        sayItAssistant = new SayItAssistant(mockWhisperRequest);
        historyManager = new HistoryManager(sayItAssistant, TEST_USER, TEST_PASSWORD);
    }

    /**
     * Function which mimics asking a question to SayIt Assistant
     */
    private void askQuestion(String question) {
        mockWhisperRequest.testString = question;
        sayItAssistant = new SayItAssistant(mockWhisperRequest);
        historyManager = new HistoryManager(sayItAssistant, TEST_USER, TEST_PASSWORD);
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
        assertEquals(historyManager.getPrompts(), new ArrayList<Question>());

        // Verify that looking for an answer to a question that has not been asked does nothing
        try {
            historyManager.getResponse(0);
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
        // Get the name of the folder where this is ran within
        String currentDirectory = System.getProperty("user.dir");
        System.out.println(currentDirectory);
        String command = "Question. Who was Louis Braille?";
        String question = "Who was Louis Braille?";
        askQuestion(command);

        // Verify that the question was added to the history
        assertTrue(historyManager.getHistorySize() == 1);

        
        // Verify that the question was added to the history
        String receivedQuestion = historyManager.getPrompts().get(0).toString();

        assertEquals(question, receivedQuestion);

        // Verify that the answer to the question is correct
        assertEquals(historyManager.getResponse(0).toString(), "This is the response to the prompt");
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
        String firstCommand = "Question. Who was Louis Braille?";
        String firstQuestion = "Who was Louis Braille?";
        String genericCommand = "Question. Why is Louis Braille?";
        String genericQuestion = "Why is Louis Braille?";
        String lastCommand = "Question. What was utilized before Braille?";
        String lastQuestion = "What was utilized before Braille?";

        // Ask first question
        askQuestion(firstCommand);

        // Ask generic questions
        for (int i = 1; i < MAX_WINDOW_QUESTION_SIZE; i++) {
            askQuestion(genericCommand);
        }

        // Ask last question
        askQuestion(lastCommand);

        // Verify that the questions were added to the history
        assertTrue(historyManager.getHistorySize() == MAX_WINDOW_QUESTION_SIZE + 1);

        // Verify that the questions were added to the history
        String receivedFirstQuestion = historyManager.getPrompts().get(0).toString();
        assertEquals(firstQuestion, receivedFirstQuestion);

        String receivedGenericQuestion; 
        for (int i = 1; i < MAX_WINDOW_QUESTION_SIZE; i++) {
            receivedGenericQuestion = historyManager.getPrompts().get(i).toString();
            assertEquals(genericQuestion, receivedGenericQuestion);
        }

        String receivedLastQuestion = historyManager.getPrompts().get(10).toString();
        assertEquals(lastQuestion, receivedLastQuestion);
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
        String command = "Question. Who was Louis Braille?";
        String question = "Who was Louis Braille?";
        askQuestion(command);

        // Verify that the question was added to the history
        assertTrue(historyManager.getHistorySize() == 1);

        // Verify that the question was added to the history
        String receivedQuestion = historyManager.getPrompts().get(0).toString();
        assertEquals(question, receivedQuestion);

        // Delete the question
        historyManager.delete(0);

        // Verify that the question was deleted from the history
        assertTrue(historyManager.getHistorySize() == 0);

        // Verify that looking questions returns nothing
        assertEquals(historyManager.getPrompts(), new ArrayList<Question>());

        // Verify that looking for an answer to a question that has not been asked does nothing
        try {
            historyManager.getResponse(0);
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
        String thirdCommand = "Question. Who was Louis Braille?";
        String thirdQuestion = "Who was Louis Braille?";
        String genericCommand = "Question. Why is Louis Braille?";
        String lastCommand = "Question. What was utilized before Braille?";
        String lastQuestion = "What was utilized before Braille?";

        // Ask first through second question
        for (int i = 0; i < 2; i++) {
            askQuestion(genericCommand);
        }

        // Ask third question
        askQuestion(thirdCommand);

        // Ask the fourth through ninth question
        for (int i = 3; i < MAX_WINDOW_QUESTION_SIZE; i++) {
            askQuestion(genericCommand);
        }

        // Ask last question
        askQuestion(lastCommand);

        // Verify that the questions were added to the history
        assertTrue(historyManager.getHistorySize() == MAX_WINDOW_QUESTION_SIZE + 1);

        // Delete the third question
        historyManager.delete(2);

        // Verify that the question was deleted from the history
        assertTrue(historyManager.getHistorySize() == MAX_WINDOW_QUESTION_SIZE);

        // Verify that the thirdQuestion was deleted from the history
        for (int i = 0; i < MAX_WINDOW_QUESTION_SIZE; i++) {
            assertNotEquals(historyManager.getPrompts().get(i).toString(), thirdQuestion);
        }

        // Verify that the last question was moved up to the 9th question
        String receivedLastQuestion = historyManager.getPrompts().get(9).toString();
        assertEquals(lastQuestion, receivedLastQuestion);
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
        String firstCommand = "Question. Who was Louis Braille?";
        String firstQuestion = "Who was Louis Braille?";
        String lastCommand = "Question. What was utilized before Braille?";
        String lastQuestion = "What was utilized before Braille?";

        // Ask first question
        askQuestion(firstCommand);

        // Ask last question
        askQuestion(lastCommand);

        // Verify that the questions were added to the history
        assertTrue(historyManager.getHistorySize() == 2);

        // Verify that the questions were added to the history
        String receivedFirstQuestion = historyManager.getPrompts().get(0).toString();
        assertEquals(firstQuestion, receivedFirstQuestion);

        String receivedLastQuestion = historyManager.getPrompts().get(1).toString();
        assertEquals(lastQuestion, receivedLastQuestion);

        // Close SayIt Assistant
        sayItAssistant = null;
        historyManager = null;

        // Reopen SayIt Assistant
        openSayItAssistant();

        // Verify that the questions were added to the history
        assertTrue(historyManager.getHistorySize() == 2);

        // Verify that the questions were added to the history
        receivedFirstQuestion = historyManager.getPrompts().get(0).toString();
        assertEquals(firstQuestion, receivedFirstQuestion);

        receivedLastQuestion = historyManager.getPrompts().get(1).toString();
        assertEquals(lastQuestion, receivedLastQuestion);
    }
}
