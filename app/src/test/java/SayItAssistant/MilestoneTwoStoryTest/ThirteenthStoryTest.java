package SayItAssistant.MilestoneTwoStoryTest;

import org.junit.jupiter.api.*;

import com.google.common.math.Quantiles;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import SayItAssistant.middleware.SayItAssistant;
import SayItAssistant.middleware.Answer;
import SayItAssistant.middleware.ClearAllPrompt;
import SayItAssistant.middleware.DeletePrompt;
import SayItAssistant.middleware.HistoryManager;
import SayItAssistant.middleware.IPrompt;
import SayItAssistant.middleware.MockWhisperRequest;
import SayItAssistant.middleware.Question;
import SayItAssistant.middleware.ResponseCoordinator;

/**
 * This class is responsible for testing the thirteenth story of the SayItAssistant
 * 
 * User Story:
 * As a minimalist, I want to be able to clear all of my prompt history by saying 
 * “Clear All” so that I don’t have to individually select and delete all my old questions. 
 */
public class ThirteenthStoryTest {

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
    /*
     * Scenario 1: Clearing a history prompt with questions
     * Given: History Helen had asked “Question. What caused the downfall of the Roman Empire?”, “Create email to Jill let’s meet at Geisel”, and “Send email to Jill B at UCSD dot EDU”
     * When: Helen correctly uses the Start button
     * And: Helen says “Clear all”
     * Then: SayIt assistant will delete all of the questions from the history prompt
     * And: The prompt area will display Helen’s command “Clear All”.
     * And: The response area will display “All prompts successfully deleted”.
     * And: The prompt history contains no prompts.*/
    @Test
    public void testScenario1() {

        // asks multiple questions and puts them in the history
        addAllQuestions();

        // creates a prompt to delete the question from the history
        IPrompt prompt = new ClearAllPrompt();
        ResponseCoordinator rc = new ResponseCoordinator(historyManager, null);

        // the questions are in history
        assertEquals(4, historyManager.getPrompts().size());

        // the questions get deleted from history
        rc.createResponse(prompt);
        assertEquals(0, historyManager.getPrompts().size());
    }
}
