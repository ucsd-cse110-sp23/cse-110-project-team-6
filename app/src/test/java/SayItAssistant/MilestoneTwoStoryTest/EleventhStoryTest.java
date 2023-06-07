package SayItAssistant.MilestoneTwoStoryTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import SayItAssistant.frontend.PromptPanel;
import SayItAssistant.middleware.IPrompt;
import SayItAssistant.middleware.PromptFactory;
import SayItAssistant.middleware.Question;

/*
 * Class containing all of the tests for the new question command
 */
public class EleventhStoryTest {

    private static final String PROMPT1 = "Question. What caused the Roman Empire to fall?";
    private static final String INVALID_QUESTION_COMMAND = "Question.";

    /*  BDD Scenario Test for Scenario 1: Prompt starts with the word "Question"
     *  Given: History Helen presses the Start button
     *  And: She states "Question. What caused the Roman Empire to fall"
     *  When: Helen releases the Start button
     *  Then: Her command of "Question" and her prompt "What caused the Roman Empire to fall?" are displayed in the Prompt Area with the command on-top of the prompt
     */
    @Test
    public void BDD1() {
        PromptFactory pf = new PromptFactory();
        PromptPanel pp = new PromptPanel();
        // creates a question prompt and displays it in the prompt panel
        IPrompt prompt1 = pf.createPrompt(PROMPT1);
        pp.setPrompt(prompt1);

        assert (prompt1 instanceof Question);                // prompt1 should have created a question

        assertEquals(prompt1.toString(), pp.getPrompt());   // the display should match the prompt
    }

    /*  BDD Scenario Test for Scenario 2: Prompt starts with the word "Question," but the question was not picked up
     *  Given: History Helen presses and holds the start button
     *  When: She states "Question."
     *  And: Helen Releases the Start button
     *  Then: The command "Question" will be displayed with the message "Apologies, I am unable to fulfill your request for technical reasons on my part. Please try again."
     *  And: The prompt "Question" will be displayed in the history panel.
     */
    @Test
    public void BDD2() {
        PromptFactory pf = new PromptFactory();

        IPrompt prompt2 = pf.createPrompt(INVALID_QUESTION_COMMAND);

        assert (prompt2 == null);    // inputting just the command should not create a prompt; the prompt should be null
    }
}
