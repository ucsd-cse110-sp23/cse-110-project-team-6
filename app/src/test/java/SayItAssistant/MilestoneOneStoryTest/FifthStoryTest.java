package SayItAssistant.MilestoneOneStoryTest;

import SayItAssistant.middleware.HistoryManager;
import SayItAssistant.middleware.SayItAssistant;
import SayItAssistant.middleware.WhisperRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class FifthStoryTest {
    @Test
    public void testClearAllEmpty() {
       /* Scenario 2: There are no questions in SayIt Assistant’s history
        Given: there are no questions SayIt Assistant’s history
        When: History Helen wants to click the clear all button
        Then: Nothing will happen.*/
        HistoryManager newHistoryManager = new HistoryManager(new SayItAssistant(new WhisperRequest()));
        Assertions.assertEquals(newHistoryManager.getPrompts(), new ArrayList<>());
        newHistoryManager.clearAll();
        Assertions.assertEquals(newHistoryManager.getPrompts(), new ArrayList<>());
        Assertions.assertEquals(newHistoryManager.getHistorySize(), 0);
    }
}
