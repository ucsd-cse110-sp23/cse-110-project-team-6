package backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.*;

public class HistoryTextIOTests {
    private ArrayList<Question> expectedQuestionsList = new ArrayList<>(Arrays.asList(
            new Question("Question 1"), new Question("Question 2"),
            new Question("Question 3"), new Question("Question 4"),
            new Question("Question 5")));
    private ArrayList<Answer> expectedAnswersList = new ArrayList<>(Arrays.asList(
            new Answer("Answer 1"), new Answer("Answer 2"),
            new Answer("Answer 3"), new Answer("Answer 4"),
            new Answer("Answer 5")));
    private ArrayList<Question> newQList = new ArrayList<>(Arrays.asList(
            new Question("Q1"), new Question("Q2"),
            new Question("Q3"), new Question("Q4"),
            new Question("Q5")));
    private ArrayList<Answer> newAList = new ArrayList<>(Arrays.asList(
            new Answer("A1"), new Answer("A2"),
            new Answer("A3"), new Answer("A4"),
            new Answer("A5")));
    private HashMap<Question, Answer> expectedHistory = createHistory();

    private HashMap<Question, Answer> createHistory() {
        HashMap<Question, Answer> expectedHistory = new HashMap<Question, Answer>();
        for (int i = 0; i < this.expectedQuestionsList.size(); ++i) {
            expectedHistory.put(expectedQuestionsList.get(i), expectedAnswersList.get(i));
        }
        return expectedHistory;
    }

    @Test
    public void testCreateTextIO() {
        HistoryTextIO htIO = new HistoryTextIO("tests/backend/historyTest.txt");
        htIO.write(expectedQuestionsList, expectedAnswersList, "tests/backend/historyTest.txt");
        ArrayList<Question> actualQuestionsList = htIO.getQuestions();
        ArrayList<Answer> actualAnswersList = htIO.getAnswers();
        
        for (int i = 0; i < actualAnswersList.size(); i++) {
            assertEquals(expectedQuestionsList.get(i).getQuestion(), actualQuestionsList.get(i).getQuestion());
            assertEquals(expectedAnswersList.get(i).getAnswer(), actualAnswersList.get(i).getAnswer());
        }
    }

    @Test
    public void testWriteTextIO() {
        HistoryTextIO htIO = new HistoryTextIO("tests/backend/historyTest.txt");
        htIO.write(newQList, newAList, "tests/backend/historyTest.txt");
        ArrayList<Question> actualQuestionsList = htIO.getQuestions();
        ArrayList<Answer> actualAnswersList = htIO.getAnswers();

        for (int i = 0; i < actualAnswersList.size(); i++) {
            assertEquals(expectedAnswersList.get(i).getAnswer(), actualAnswersList.get(i).getAnswer());
            assertEquals(expectedQuestionsList.get(i).getQuestion(), actualQuestionsList.get(i).getQuestion());
        }
    }
}
