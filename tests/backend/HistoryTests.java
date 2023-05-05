package backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

public class HistoryTests {
    
    private ArrayList<Question> expectedQuestionsList = new ArrayList<>(Arrays.asList(
        new Question("Who am I?"), new Question("What is 9 + 10?"), 
        new Question("What is the meaning of life?"), new Question("Where am I?"),
        new Question("What is my favorite color?")));
    private ArrayList<Answer> expectedAnswersList = new ArrayList<>(Arrays.asList(
        new Answer("I am SayIt."), new Answer("That would be 21."),
        new Answer("42"), new Answer("I am in the Matrix"),
        new Answer("Yellow.  No, Red.")));
    private HashMap<Question, Answer> expectedHistory = createHistory();

    private HashMap<Question, Answer> createHistory() {
        HashMap<Question, Answer> expectedHistory = new HashMap<Question, Answer>();
        for (int i = 0; i < this.expectedQuestionsList.size(); ++i) {
            expectedHistory.put(expectedQuestionsList.get(i), expectedAnswersList.get(i));
        }
        return expectedHistory;
    }

    @Test
    public void testCreateHistory () {
        History history = new History();
        assertTrue (history instanceof History);
        assertEquals(0, history.numberOfQuestions());
    }

    @Test
    public void testGetAnswer () {
        History history = new History();
        history.setHistory(expectedQuestionsList, expectedAnswersList);

        Answer testAnswer = history.getAnswer(expectedQuestionsList.get(2));
        Answer testAnswer2 = history.getAnswer(expectedQuestionsList.get(0));

        assertEquals(expectedAnswersList.get(2).getAnswer(), testAnswer.getAnswer());
        assertEquals(expectedAnswersList.get(0).getAnswer(), testAnswer2.getAnswer());
    }

    @Test
    public void testGetQuestions() {
        History history = new History();
        history.setHistory(expectedQuestionsList, expectedAnswersList);
        
        ArrayList<Question> actualQuestionsList = history.getQuestions();

        for (int i = 0; i < history.numberOfQuestions(); ++i) {
            assertEquals(expectedQuestionsList.get(i).getQuestion(), actualQuestionsList.get(i).getQuestion());
        }

    }

    @Test
    public void testAddQuestionAndAnswer() {
        History history = new History();

        assertEquals(0, history.numberOfQuestions());

        history.addQuestionAndAnswer(expectedQuestionsList.get(1), expectedAnswersList.get(1));

        assertEquals(1, history.numberOfQuestions());

        history.addQuestionAndAnswer(expectedQuestionsList.get(4), expectedAnswersList.get(4));

        assertEquals(2, history.numberOfQuestions());
        assertEquals(expectedAnswersList.get(1).getAnswer(), history.getAnswer(expectedQuestionsList.get(1)).getAnswer());
        assertEquals(expectedAnswersList.get(4).getAnswer(), history.getAnswer(expectedQuestionsList.get(4)).getAnswer());
    }

    @Test
    public void testDeleteQuestionAndAnswer() {
        History history = new History();

        assertEquals(0, history.numberOfQuestions());
        history.deleteQuestionAndAnswer(expectedQuestionsList.get(1));
        assertEquals(0, history.numberOfQuestions());

        history.addQuestionAndAnswer(expectedQuestionsList.get(2), expectedAnswersList.get(2));
        history.addQuestionAndAnswer(expectedQuestionsList.get(4), expectedAnswersList.get(4));
        history.deleteQuestionAndAnswer(expectedQuestionsList.get(2));
        assertEquals (1, history.numberOfQuestions());
        assertEquals(null, history.getAnswer(expectedQuestionsList.get(2)));
        history.deleteQuestionAndAnswer(expectedQuestionsList.get(4));
        assertEquals(null, history.getAnswer(expectedQuestionsList.get(4)));
        assertEquals (0, history.numberOfQuestions());
    }

    @Test
    public void testClearQuestionsAndAnswers() {
        History history = new History();

        history.setHistory(expectedQuestionsList, expectedAnswersList);

        assertEquals(5, history.numberOfQuestions());
        history.clearAllQuestionsAndAnswers();
        assertEquals(0, history.numberOfQuestions());
        for (int i = 0; i < expectedAnswersList.size(); ++i) {
            assertEquals(null, history.getAnswer(expectedQuestionsList.get(i)));
        }
    }
}