package SayItAssistant.MilestoneTwoStoryTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import SayItAssistant.middleware.SayItAssistant;
import SayItAssistant.middleware.HistoryManager;
import SayItAssistant.middleware.MockWhisperRequest;
import SayItAssistant.middleware.PromptResponsePair;

/**
 * This class is responsible for testing the tenth story of the SayItAssistant
 * 
 * User Story:
 * As a vocal person,
 * I want my voice to be recorded with SA2 to start after I press the "Start" button
 * so that my requests are heard as I have them
 */
public class TenthStoryTest {
    
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
    private static final String NO_SPEECH_RECORD = "";
    private static final String NO_COMMAND_WARN = "No command found";
    private static final String RESPONSE        = "This is the response to the prompt";
    private static final String NO_COMMAND_RESPONSE = 
    "Apologies, no valid command was found within your request. I can assist you if you start your requests with Question, Delete Prompt, Clear All, Setup Email, Create Email, or Send Email to <email>.";

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
    private PromptResponsePair askQuestion(String question) {
        mockWhisperRequest.testString = question;
        sayItAssistant = new SayItAssistant(mockWhisperRequest);
        historyManager = new HistoryManager(sayItAssistant, TEST_USER, TEST_PASSWORD);
        sayItAssistant.setHistoryManager(historyManager);
        return sayItAssistant.respond();
    }

    /**
     * Scenario 1: Start Button is Pressed, Held, and Released, and Speech is Heard Properly
     * Given: History Helen is logged into SayItAssistant 2
     * When: She presses and holds the Start button
     * Then: SayItAssistant 2 begins recording her speech.
     * And: The Start button changes its text to “Recording…”
     * When: Helen asks “Question. What caused the Roman Empire to fall?”
     * Then: SayIt Assistant continues to record her voice
     * When: Helen releases the Start button
     * Then: SayIt Assistant stops recording her speech
     * And: The Start button text changes back to “Start” instead of “Recording…”
     * And: Helens command of “Question” and her prompt “What caused the Roman Empire to fall?” are 
     * displayed in the Prompt Area with the command on-top of the prompt
     * And: SayIt Assistant begins to process her speech according to her prompt.
     */
    @Test
    public void testScenario1() {
        // Mock instance of already logging into SayIt Assistant
        openSayItAssistant();

        // Mock instance of pressing and holding the Start button
        //PressStartButton
        
        // Mock instance of asking a question
        PromptResponsePair value = askQuestion(TEST_QUESTION);

        // Mock instance of releasing the Start button
        //ReleaseStartButton

        // Check that the Start button text changes back to "Start"
        //CheckTeehee

        // Check that the command and prompt are displayed in the Prompt Area
        String prompt = value.getPrompt().toString();
        assertEquals(prompt, PARSED_QUESTION);
        assertEquals(prompt, historyManager.getPrompts().get(0).toString());
        assertEquals(historyManager.getResponse(0).toString(), RESPONSE);
        //CommandPanel.setCommand()
    }

    /**
     * Scenario 2: Start button is Pressed Briefly
     * Given: History Helen is logged into SayItAssistant 2
     * When: History Helen taps the Start button by pressing it quickly and releasing it
     * Then: The Start button changes its text to “Recording…”
     * And: The Start button text changes back to “Start” instead of “Recording…”
     * And: An error message appears in the prompt area saying “Apologies, your request could not 
     * be understood. Please try to either hold the button longer to capture your speech or please 
     * try speaking more clearly into the microphone.” 
     * And: The interaction is not saved to the Prompt History.
     */
    @Test
    public void testScenario2() {
        // Mock instance of already logging into SayIt Assistant
        openSayItAssistant();

        // Mock instance of pressing and releasing the Start button
        //PressStartButton
        //ReleaseStartButton

        // Check that the Start button text changes back to "Start"
        //CheckTeehee

        // Check that the error message appears in the prompt area
        PromptResponsePair value = askQuestion(SHORT_RECORD);
        assertEquals(value.getResponse().toString(), NO_COMMAND_RESPONSE);
        assertEquals(value.getPrompt().toString(), NO_COMMAND_WARN);

        // Verify that the interaction is not saved to the Prompt History
        assertEquals(historyManager.getPrompts().size(), 0);
    }

    /**
     * Scenario 3: Start button is Pressed, Held, and Released, but No Speech Detected
     * Given: History Helen is logged into SayItAssistant 2
     * When: History Helen presses and holds the Start button
     * Then: SayItAssistant 2 activates the recording functionality
     * And: The Start button changes its text to "Recording..."
     * When: Helen does not speak
     * Then: SayIt Assistant continues to show the "Recording..." status
     * When: Helen releases the Start button
     * Then: The Start button text changes back to “Start” instead of “Recording…”
     * Then: An error message appears in the prompt area saying “Apologies, your request could not 
     * be understood. Please try to either hold the button longer to capture your speech or make 
     * yourself more audible.” 
     * And: The interaction is not saved to the Prompt History.
     */
    @Test
    public void testScenario3() {
        // Mock instance of already logging into SayIt Assistant
        openSayItAssistant();

        // Mock instance of pressing and holding the Start button
        //PressStartButton

        // Mock instance of releasing the Start button
        //ReleaseStartButton

        // Check that the Start button text changes back to "Start"
        //CheckTeehee

        // Check that the error message appears in the prompt area
        PromptResponsePair value = askQuestion(NO_SPEECH_RECORD);
        assertEquals(value.getResponse().toString(), NO_COMMAND_RESPONSE);
        assertEquals(value.getPrompt().toString(), NO_COMMAND_WARN);

        // Verify that the interaction is not saved to the Prompt History
        assertEquals(historyManager.getPrompts().size(), 0);
    }

    /**
     * Scenario 4: Prompt is recorded, but a valid command is not found within the start of the 
     * prompt
     * 
     * Given: History Helen is logged into SayItAssistant 2
     * When: History Helen presses and holds the Start button
     * Then: SayItAssistant 2 activates the recording functionality
     * And: The Start button changes its text to "Recording..."
     * When: Helen asks “What caused the downfall of the Roman Empire?”
     * Then: SayIt Assistant continues to show the "Recording..." status
     * When: Helen releases the Start button
     * Then: The Start button text changes back to “Start” instead of “Recording…”
     * Then: An error message appears in the prompt area saying “Apologies, no valid command was 
     * found within your request. I can assist you if you start your requests with Question, Delete 
     * Prompt, Clear All, Setup Email, Create Email, or Send Email to <email>.” 
     * And: The interaction is not saved to the Prompt History.
     */
    @Test
    public void testScenario4() {
        // Mock instance of already logging into SayIt Assistant
        openSayItAssistant();

        // Mock instance of pressing and holding the Start button
        //PressStartButton

        // Mock instance of asking a question
        PromptResponsePair value = askQuestion(PARSED_QUESTION);

        // Mock instance of releasing the Start button
        //ReleaseStartButton

        // Check that the Start button text changes back to "Start"
        //CheckTeehee

        // Check that the error message appears in the prompt area
        assertEquals(value.getResponse().toString(), NO_COMMAND_RESPONSE);
        assertEquals(value.getPrompt().toString(), NO_COMMAND_WARN);

        // Verify that the interaction is not saved to the Prompt History
        assertEquals(historyManager.getPrompts().size(), 0);
    }
}
