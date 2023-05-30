package middlewareTests;

import org.junit.jupiter.api.*;

import middleware.Answer;
import middleware.HistoryManager;
import middleware.MockWhisperRequest;
import middleware.Question;
import middleware.SayItAssistant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing all of the tests for the HistoryManager class
 */
public class HistoryManagerTests {

    private static final String EXPECT_HISTORY_PATH = "history.json";

    private static final Question QUESTION1 = new Question("What is your name?");
    private static final Question QUESTION2 = new Question("What is your quest?");
    private static final Question QUESTION3 = new Question("What is your favorite color?");
    private static final Question QUESTION4 = new Question("What is bubble tea?");
    private static final Question QUESTION5 = new Question("What is the meaning of life?");
    private static final Question QUESTION6 = new Question("What is 9 + 10?");
    private static final Question DUPLICATE_QUESTION = new Question("What is your name?");
    private static final Question MULTILINE_QUESTION = 
        new Question("What is your name?\nWhat is your quest?");
    private static final Question EMPTY_QUESTION = new Question("");
    
    private static final Answer ANSWER1 = new Answer("My name is SayIt.");
    private static final Answer ANSWER2 = new Answer("My quest is to find the Holy Grail.");
    private static final Answer ANSWER3 = new Answer("My favorite color is blue.");
    private static final Answer ANSWER4 = new Answer("Bubble tea is a drink.");
    private static final Answer ANSWER5 = new Answer("The meaning of life is 42.");
    private static final Answer ANSWER6 = new Answer("9 + 10 = 21.");
    private static final Answer DUPLICATE_ANSWER = new Answer("My name is SayIt.");
    private static final Answer MULTILINE_ANSWER = 
        new Answer("My name is SayIt.\nMy quest is to find the Holy Grail.");
    private static final Answer EMPTY_ANSWER = new Answer("");

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
        historyManager = new HistoryManager(assistant,"a","1");
        historyManager.clearAll();
        allQuestions = new ArrayList<Question>();
        allQuestions.add(QUESTION1);
        allQuestions.add(QUESTION2);
        allQuestions.add(QUESTION3);
        allQuestions.add(QUESTION4);
        allQuestions.add(QUESTION5);
        allQuestions.add(QUESTION6);
        
        allAnswers = new ArrayList<Answer>();
        allAnswers.add(ANSWER1);
        allAnswers.add(ANSWER2);
        allAnswers.add(ANSWER3);
        allAnswers.add(ANSWER4);
        allAnswers.add(ANSWER5);
        allAnswers.add(ANSWER6);
    }

    @AfterEach
    public void tearDown() {
        File file = new File(EXPECT_HISTORY_PATH);
        file.delete();
    }

    /**
     * Tests that the history manager and JSON file is created and empty
     */
    @Test
    public void testCreateHistoryManager() throws IOException {
        assertNotNull(historyManager);
        // Verify the JSON file is created
        assertTrue(Files.exists(Path.of(EXPECT_HISTORY_PATH)));
        File jsonFile = new File(EXPECT_HISTORY_PATH);
        String contents = Files.readString(Path.of(EXPECT_HISTORY_PATH));
        assertEquals("{}", contents);
        // Verify that the history manager is empty
        assertEquals(0, historyManager.getQuestions().size());
    }

    /**
     * Tests that the history manager is able to add in questions and answers to its history
     */
    @Test
    public void testAddQuestion() {
        historyManager.add(QUESTION1, ANSWER1);
        assertEquals(1, historyManager.getHistorySize());

        historyManager.add(QUESTION2, ANSWER2);
        assertEquals(2, historyManager.getHistorySize());

        historyManager.add(QUESTION3, ANSWER3);
        assertEquals(3, historyManager.getHistorySize());

        historyManager.add(QUESTION4, ANSWER4);
        assertEquals(4, historyManager.getHistorySize());

        historyManager.add(QUESTION5, ANSWER5);
        assertEquals(5, historyManager.getHistorySize());

        historyManager.add(QUESTION6, ANSWER6);
        assertEquals(6, historyManager.getHistorySize());

        int allQASize = allQuestions.size();
        assertEquals(allQASize, historyManager.getQuestions().size());

        for (int i = 0; i < allQASize; i++) {
            String expectedQuestion = allQuestions.get(i).toString();
            String actualQuestion = historyManager.getQuestions().get(i).toString();
            assertEquals(expectedQuestion, actualQuestion);

            String expectedAnswer = allAnswers.get(i).toString();
            String actualAnswer = historyManager.getAnswer(i).toString();
            assertEquals(expectedAnswer, actualAnswer);
        }
    }

    /**
     * Tests that the history manager is able to add in duplicate questions and answers 
     */
    @Test
    public void testDuplicateQA() {
        addAllQuestions();
        assertEquals(allQuestions.size(), historyManager.getHistorySize());
        assertEquals(QUESTION1.toString(), DUPLICATE_QUESTION.toString());
        assertEquals(ANSWER1.toString(), DUPLICATE_ANSWER.toString());
        historyManager.add(DUPLICATE_QUESTION, DUPLICATE_ANSWER);
        assertEquals(allQuestions.size() + 1, historyManager.getHistorySize());
        assertEquals(DUPLICATE_QUESTION.toString(), 
                     historyManager.getQuestions().get(allQuestions.size()).toString());
        assertEquals(DUPLICATE_ANSWER.toString(),
                        historyManager.getAnswer(allQuestions.size()).toString());
    }

    /**
     * Tests that history manager is capable of storing multiline questions and answers
     */
    @Test
    public void testMultilineQA() {
        historyManager.add(MULTILINE_QUESTION, MULTILINE_ANSWER);
        assertEquals(1, historyManager.getHistorySize());
        assertEquals(MULTILINE_QUESTION.toString(), 
                     historyManager.getQuestions().get(0).toString());
        assertEquals(MULTILINE_ANSWER.toString(), 
                     historyManager.getAnswer(0).toString());
    }

    /**
     * Tests that history manager is capable of storing empty questions and answers
     */
    @Test
    public void testEmptyQA() {
        historyManager.add(EMPTY_QUESTION, EMPTY_ANSWER);
        assertEquals(1, historyManager.getHistorySize());
        assertEquals(EMPTY_QUESTION.toString(), 
                     historyManager.getQuestions().get(0).toString());
        assertEquals(EMPTY_ANSWER.toString(), 
                     historyManager.getAnswer(0).toString());
    }

    /**
     * Tests persistence of history by starting a new instance of history manager
     */
    @Test
    public void testPersistentHistory() {
        int allQASize = allQuestions.size();
        addAllQuestions();

        // "Close" the current history manager
        historyManager = null;
        assertNull(historyManager);

        // Create a new history manager
        HistoryManager newHistoryManager = new HistoryManager(assistant,"a","1");
        assertNotEquals(newHistoryManager, historyManager);
        assertEquals(allQASize, newHistoryManager.getQuestions().size());
        
        for (int i = 0; i < allQASize; i++) {
            String expectedQuestion = allQuestions.get(i).toString();
            String actualQuestion = newHistoryManager.getQuestions().get(i).toString();
            assertEquals(expectedQuestion, actualQuestion);

            String expectedAnswer = allAnswers.get(i).toString();
            String actualAnswer = newHistoryManager.getAnswer(i).toString();
            assertEquals(expectedAnswer, actualAnswer);
        }

        // Add more questions to the new history manager
        newHistoryManager.add(DUPLICATE_QUESTION, DUPLICATE_ANSWER);
        allQuestions.add(DUPLICATE_QUESTION);
        allAnswers.add(DUPLICATE_ANSWER);
        allQASize++;
        // Close the new history manager
        newHistoryManager = null;
        assertNull(newHistoryManager);

        // Create another new history manager
        HistoryManager anotherHistoryManager = new HistoryManager(assistant,"a","1");
        assertNotEquals(anotherHistoryManager, newHistoryManager);
        assertEquals(allQASize, anotherHistoryManager.getQuestions().size());

        for (int i = 0; i < allQASize; i++) {
            String expectedQuestion = allQuestions.get(i).toString();
            String actualQuestion = anotherHistoryManager.getQuestions().get(i).toString();
            assertEquals(expectedQuestion, actualQuestion);

            String expectedAnswer = allAnswers.get(i).toString();
            String actualAnswer = anotherHistoryManager.getAnswer(i).toString();
            assertEquals(expectedAnswer, actualAnswer);
        }
    }

    /**
     * Tests deleting the most recent question and answer
     */
    @Test
    public void testDeleteRecent() {
        addAllQuestions();
        String fullFile = "";
        String changedFile = "";

        // Reads in contents of the JSON file
        try { fullFile = Files.readString(Path.of(EXPECT_HISTORY_PATH)); } 
        catch (Exception e) { assertTrue(false); }

        int origSize = allQuestions.size();
        historyManager.delete(origSize - 1);
        assertEquals(origSize - 1, historyManager.getHistorySize());
        assertEquals(origSize - 1, historyManager.getQuestions().size());

        // Checks that the remaining questions and answers are still in the history
        for (int i = 0; i < origSize - 1; i++) {
            String expectedQuestion = allQuestions.get(i).toString();
            String actualQuestion = historyManager.getQuestions().get(i).toString();
            assertEquals(expectedQuestion, actualQuestion);

            String expectedAnswer = allAnswers.get(i).toString();
            String actualAnswer = historyManager.getAnswer(i).toString();
            assertEquals(expectedAnswer, actualAnswer);
        }

        // Makes sure that that the last question and answer is no longer in the history
        try { 
            historyManager.getAnswer(origSize); 
            assertTrue(false); 
        } catch (Exception e) { assertTrue(true); }

        // Reads in contents of the JSON file
        try { changedFile = Files.readString(Path.of(EXPECT_HISTORY_PATH)); } 
        catch (Exception e) { assertTrue(false); }

        assertNotEquals(changedFile, fullFile);

        // Close the current history manager
        historyManager = null;
        assertNull(historyManager);

        // Open a new history manager and check question no longer exists
        HistoryManager newHistoryManager = new HistoryManager(assistant,"a","1");
        assertEquals(origSize - 1, newHistoryManager.getHistorySize());
        assertEquals(origSize - 1, newHistoryManager.getQuestions().size());

        // Checks that the remaining questions and answers are still in the history
        for (int i = 0; i < origSize - 1; i++) {
            String expectedQuestion = allQuestions.get(i).toString();
            String actualQuestion = newHistoryManager.getQuestions().get(i).toString();
            assertEquals(expectedQuestion, actualQuestion);

            String expectedAnswer = allAnswers.get(i).toString();
            String actualAnswer = newHistoryManager.getAnswer(i).toString();
            assertEquals(expectedAnswer, actualAnswer);
        }

        // Makes sure that that the last question and answer is no longer in the history
        try { 
            newHistoryManager.getAnswer(origSize); 
            assertTrue(false); 
        } catch (Exception e) { assertTrue(true); }
    }

    /**
     * Tests deleting a question and answer at a specific index
     */
    @Test
    public void testDeleteSpecific() {
        addAllQuestions();
        int removedIndex = 3;
        String fullFile = "";
        String changedFile = "";

        // Reads in contents of the JSON file
        try { fullFile = Files.readString(Path.of(EXPECT_HISTORY_PATH)); } 
        catch (Exception e) {
            fail(); }

        int origSize = allQuestions.size();
        historyManager.delete(removedIndex);
        allQuestions.remove(removedIndex);
        allAnswers.remove(removedIndex);
        assertEquals(origSize - 1, historyManager.getHistorySize());
        assertEquals(origSize - 1, historyManager.getQuestions().size());

        // Checks that the remaining questions and answers are still in the history
        for (int i = 0; i < origSize - 1; i++) {
            String expectedQuestion = allQuestions.get(i).toString();
            String actualQuestion = historyManager.getQuestions().get(i).toString();
            assertEquals(expectedQuestion, actualQuestion);

            String expectedAnswer = allAnswers.get(i).toString();
            String actualAnswer = historyManager.getAnswer(i).toString();
            assertEquals(expectedAnswer, actualAnswer);
        }

        // Makes sure that that the last question and answer is no longer in the history
        try { 
            historyManager.getAnswer(origSize);
            fail();
        } catch (Exception e) { assertTrue(true); }

        // Reads in contents of the JSON file
        try { changedFile = Files.readString(Path.of(EXPECT_HISTORY_PATH)); } 
        catch (Exception e) {
            fail(); }

        assertNotEquals(changedFile, fullFile);

        // Close the current history manager
        historyManager = null;
        assertNull(historyManager);

        // Open a new history manager and check question no longer exists
        HistoryManager newHistoryManager = new HistoryManager(assistant,"a","1");
        assertEquals(origSize - 1, newHistoryManager.getHistorySize());
        assertEquals(origSize - 1, newHistoryManager.getQuestions().size());

        // Checks that the remaining questions and answers are still in the history
        for (int i = 0; i < origSize - 1; i++) {
            String expectedQuestion = allQuestions.get(i).toString();
            String actualQuestion = newHistoryManager.getQuestions().get(i).toString();
            assertEquals(expectedQuestion, actualQuestion);

            String expectedAnswer = allAnswers.get(i).toString();
            String actualAnswer = newHistoryManager.getAnswer(i).toString();
            assertEquals(expectedAnswer, actualAnswer);
        }

        // Makes sure that that the last question and answer is no longer in the history
        try { 
            newHistoryManager.getAnswer(origSize);
            fail();
        } catch (Exception e) { assertTrue(true); }
    }

    /**
     * Tests deleting all questions and answers
     */
    @Test
    public void testClearAll() {
        addAllQuestions();
        String fullFile = "";
        String changedFile = "";

        // Reads in contents of the JSON file
        try { fullFile = Files.readString(Path.of(EXPECT_HISTORY_PATH)); } 
        catch (Exception e) { assertTrue(false); }

        historyManager.clearAll();
        assertEquals(0, historyManager.getHistorySize());
        assertEquals(0, historyManager.getQuestions().size());

        // Makes sure that that the last question and answer is no longer in the history
        try { 
            historyManager.getAnswer(0); 
            assertTrue(false); 
        } catch (Exception e) { assertTrue(true); }

        // Reads in contents of the JSON file
        try { changedFile = Files.readString(Path.of(EXPECT_HISTORY_PATH)); } 
        catch (Exception e) { assertTrue(false); }

        assertNotEquals(changedFile, fullFile);

        // Close the current history manager
        historyManager = null;
        assertNull(historyManager);

        // Open a new history manager and check question no longer exists
        HistoryManager newHistoryManager = new HistoryManager(assistant,"a","1");
        assertEquals(0, newHistoryManager.getHistorySize());
        assertEquals(0, newHistoryManager.getQuestions().size());

        // Makes sure that that the last question and answer is no longer in the history
        try { 
            newHistoryManager.getAnswer(0); 
            assertTrue(false); 
        } catch (Exception e) { assertTrue(true); }
    }
}
