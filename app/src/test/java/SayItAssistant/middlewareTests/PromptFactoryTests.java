package SayItAssistant.middlewareTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

import SayItAssistant.middleware.*;

public class PromptFactoryTests {
    PromptFactory pf = new PromptFactory();
    private String QUESTION_PROMPT = "Question.  What is the capital of France?";
    private String DELETE_PROMPT = "Delete prompt";
    private final String CLEAR_ALL_PROMPT   = "Clear all";
    private final String SETUP_EMAIL_PROMPT  = "Setup email";
    private final String CREATE_EMAIL_PROMPT = "Create email";
    private final String SEND_EMAIL_PROMPT   = "Send email";
    private String INVALID_PROMPT = "What is the capital of France?";

    /*
     * Tests for when a prompt is given with "question" as the command type.
     */
    @Test
    public void testQuestionPrompt() {
        IPrompt p = pf.createPrompt(QUESTION_PROMPT);

        assertTrue (p instanceof Question);
    }

    /*
     * Tests for when a prompt is given with "delete" as the command type.
     */
    @Test
    public void testDeletePrompt() {
        IPrompt p = pf.createPrompt(DELETE_PROMPT);

        assertTrue (p == null);
    }

    /*
     * Tests for when a prompt is given with "clear all" as the command type.
     */
    @Test
    public void testClearAllPrompt() {
        IPrompt p = pf.createPrompt(CLEAR_ALL_PROMPT);

        assertTrue (p == null);
    }

    /*
     * Tests for when a prompt is given with "setup email" as the command type.
     */
    @Test
    public void testSetUpEmailPrompt() {
        IPrompt p = pf.createPrompt(SETUP_EMAIL_PROMPT);

        assertTrue (p == null);
    }

    /*
     * Tests for when a prompt is given with "create email" as the command type.
     */
    @Test
    public void testCreateEmailPrompt() {
        IPrompt p = pf.createPrompt(CREATE_EMAIL_PROMPT);

        assertTrue (p == null);
    }

    /*
     * Tests for when a prompt is given with "send email" as the command type.
     */
    @Test
    public void testSendEmailPrompt() {
        IPrompt p = pf.createPrompt(SEND_EMAIL_PROMPT);

        assertTrue (p == null);
    }
    /*
     * Tests for when a prompt is given, but does not start with a valid command.
     */
    @Test
    public void testInvalidPrompt() {
        IPrompt p = pf.createPrompt(INVALID_PROMPT);

        assertTrue (p == null);
    }
}
