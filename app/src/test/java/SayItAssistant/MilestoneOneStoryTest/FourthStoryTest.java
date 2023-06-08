package SayItAssistant.MilestoneOneStoryTest;

import org.junit.jupiter.api.*;

import SayItAssistant.middleware.Answer;
import SayItAssistant.middleware.HistoryManager;
import SayItAssistant.middleware.MockWhisperRequest;
import SayItAssistant.middleware.Question;
import SayItAssistant.middleware.SayItAssistant;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Class containing all of the tests for the HistoryManager class
 */
public class FourthStoryTest {
    private static final int NOT_SELECTED = -1;
    private static final String EXPECT_HISTORY_PATH =
            System.getProperty("user.dir") + "/history.json";

    private static final String EXPECT_DATA_PATH =
            System.getProperty("user.dir") + "/data.json";

    private static final String TEST_USER = "test";
    private static final String TEST_PASSWORD = "password";


    private static final Question QUESTION1 = new Question("What is your name?");
    private static final Question QUESTION2 = new Question("What is your quest?");
    private static final Question QUESTION3 = new Question("What is your favorite color?");
    private static final Question QUESTION4 = new Question("What is bubble tea?");

    private static final Answer ANSWER1 = new Answer("My name is SayIt.");
    private static final Answer ANSWER2 = new Answer("My quest is to find the Holy Grail.");
    private static final Answer ANSWER3 = new Answer("My favorite color is blue.");
    private static final Answer ANSWER4 = new Answer("Bubble tea is a drink.");

    private static SayItAssistant assistant;
    private static HistoryManager historyManager;
    private static ArrayList<Question> allQuestions;
    private static ArrayList<Answer> allAnswers;

    /**
     * Helper Method to quickly add all the questions to the history manager
     */
    private static void addAllQuestions() {
        int allQASize = allQuestions.size();
        for (int i = 0; i < allQASize; i++) {
            historyManager.add(allQuestions.get(i), allAnswers.get(i));
        }
    }

    @BeforeEach
    public void setUp() {
        assistant = new SayItAssistant(new MockWhisperRequest());
        historyManager = new HistoryManager(assistant, TEST_USER, TEST_PASSWORD);
        historyManager.clearAll();
        allQuestions = new ArrayList<Question>();
        allQuestions.add(QUESTION1);
        allQuestions.add(QUESTION2);
        allQuestions.add(QUESTION3);
        allQuestions.add(QUESTION4);

        allAnswers = new ArrayList<Answer>();
        allAnswers.add(ANSWER1);
        allAnswers.add(ANSWER2);
        allAnswers.add(ANSWER3);
        allAnswers.add(ANSWER4);
    }

    @AfterEach
    public void tearDown() {
        File file = new File(EXPECT_HISTORY_PATH);
        file.delete();
        file = new File(EXPECT_DATA_PATH);
        file.delete();
    }

    /**
     * BDD Scenario Test for Scenario 2: Delete the current question asked
     * Given: History Helen has just asked a question
     * And: She gets a response she doesn’t like
     * And: The question gets added to SayIt Assistant’s history
     * When: History Helen clicks on the delete question button
     * Then: The question will be deleted from history.
     */
    @Test

    public void BDD2() {
        historyManager.add(QUESTION1, ANSWER1);
        historyManager.delete(0);
        assertEquals(0, historyManager.getHistorySize(), "Size should be zero");
    }

    /**
     * BDD Scenario Test for Scenario 3: No questions in the SayIt Assistant’s history panel
     * Given: There are no questions in SayIt Assistant’s history
     * When: History Helen click the delete question button
     * Then: Nothing will happen.
     */
    @Test
    public void BDD3() {
        try {
            historyManager.delete(NOT_SELECTED);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        } catch (Exception e) {
            assertFalse(false);
        }
    }

    /**
     * Scenario 4: No questions have been selected nor asked
     * Given: SayIt Assistant has 4 questions within its history.
     * And: None of the questions have been selected.
     * And: History Helen has not asked a question.
     * When: History Helen clicks the delete question button.
     * Then: Nothing will happen.
     */
    @Test
    public void BDD4() {
        addAllQuestions();
        try {
            historyManager.delete(NOT_SELECTED);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        } catch (Exception e) {
            assertFalse(false);
        }
    }

}