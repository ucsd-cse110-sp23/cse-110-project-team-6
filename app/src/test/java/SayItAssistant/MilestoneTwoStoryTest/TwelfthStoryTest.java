package SayItAssistant.MilestoneTwoStoryTest;

import org.junit.jupiter.api.*;

import com.google.common.math.Quantiles;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import SayItAssistant.middleware.SayItAssistant;
import SayItAssistant.middleware.DeletePrompt;
import SayItAssistant.middleware.HistoryManager;
import SayItAssistant.middleware.IPrompt;
import SayItAssistant.middleware.MockWhisperRequest;
import SayItAssistant.middleware.Question;
import SayItAssistant.middleware.ResponseCoordinator;

/**
 * This class is responsible for testing the twelfth story of the SayItAssistant
 * 
 * User Story:
 * As a minimalist, I want to be able to delete an old prompt by saying 
 * “Delete Prompt” so that I can keep my collection of previously asked 
 * questions tidy.
 */
public class TwelfthStoryTest {

    private static final String EXPECT_HISTORY_PATH =
            System.getProperty("user.dir") + "/history.json";
    private static final String EXPECT_DATA_PATH =
            System.getProperty("user.dir") + "/data.json";

    private static final int MAX_WINDOW_QUESTION_SIZE = 10;


    private static final String TEST_USER = "test";
    private static final String TEST_PASSWORD = "password";
    private static HistoryManager historyManager;
    private static SayItAssistant sayItAssistant;
    private static MockWhisperRequest mockWhisperRequest;

    // Constant strings for testing
    private static final String TEST_QUESTION   = "Question. What caused the Roman Empire to fall?";
    private static final String PARSED_QUESTION = "What caused the Roman Empire to fall?";
    private static final String SHORT_RECORD    = "";
    private static final String RESPONSE        = "This is the response to the prompt";

    /**
     * Opens up SayIt Assistant before each test
     */
    @BeforeEach
    public void setUp() {
        openSayItAssistant();
        historyManager.clearAll();
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
        sayItAssistant.setHistoryManager(historyManager);
        sayItAssistant.respond();
    }

    /*
     * Scenario 1: There is a singular prompt within the Prompt History
     * Given: History Helen had previously asked “Question. What caused the downfall of the Roman Empire?”
     * When: Helen selects the question from the Prompt History.
     * And: Helen correctly uses the Start button.
     * And: Helen says “Delete Prompt”.
     * Then: SayIt Assistant will delete the question from the history.
     * And: The prompt area will display Helen’s command “Delete Prompt”.
     * And: The response area will display “Prior prompt successfully deleted”.
     * And: The current prompt will not be stored within the prompt history.
     */
    @Test
    public void testScenario1() {

        // asks a question and puts it in the history
        askQuestion(TEST_QUESTION);

        // creates a prompt to delete the question from the history
        IPrompt prompt = new DeletePrompt();
        ResponseCoordinator rc = new ResponseCoordinator(historyManager, mockWhisperRequest);

        // the question is in history
        assertEquals(1, historyManager.getPrompts().size());

        // the question gets deleted from history
        rc.createResponse(prompt);
        assertEquals(0, historyManager.getPrompts().size());
    }
}