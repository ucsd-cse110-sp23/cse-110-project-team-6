package SayItAssistant.middleware;

import java.lang.ref.Cleaner.Cleanable;

public class PromptFactory {
    
    private final String QUESTION_PROMPT     = "question";
    private final String DELETE_PROMPT        = "delete prompt";
    private final String CLEAR_ALL_PROMPTS   = "clear all";
    private final String SETUP_EMAIL_PROMPT  = "setup email";
    private final String CREATE_EMAIL_PROMPT = "create email";
    private final String SEND_EMAIL_PROMPT   = "send email";
    private final String PUNCTUATION = "[^\\p{L}\\p{Z}]";   //RegEx to cover all punctuation in a String

    public IPrompt createPrompt(String input) {

        IPrompt prompt = null;
        String cleanInput = input.replaceAll(PUNCTUATION, "").toLowerCase();
        System.out.println("CLEAN INPUT: " + cleanInput);
        
        if (cleanInput.startsWith(QUESTION_PROMPT, 0) && !(cleanInput.equals(QUESTION_PROMPT))) {
            System.out.println("Creating question prompt");
            input = input.replaceFirst("(?i)"+QUESTION_PROMPT, ""); // strips input of the command, regardless of case
            input = input.replaceFirst(PUNCTUATION, "");            // removes punctuation after the command
            input = input.replaceFirst(" ", "");                    // removes space after the command
            prompt = new Question(input);                           // creates a Question prompt using the new input
            System.out.println("Input: " + input);
        }

        else if (cleanInput.equals(DELETE_PROMPT)) {
            System.out.println("Creating delete prompt");
            prompt = new DeletePrompt();
        }

        else if (cleanInput.equals(CLEAR_ALL_PROMPTS)) {
            System.out.println("Creating clear-all prompt");
            prompt = new ClearAllPrompt();
        }

        else if (cleanInput.equals(SETUP_EMAIL_PROMPT)) {
            // prompt = new setUpEmailPrompt();
        }

        else if (cleanInput.startsWith(CREATE_EMAIL_PROMPT) && !(cleanInput.equals(CREATE_EMAIL_PROMPT))) {
            // prompt = new createEmailPrompt(input.replaceFIrst(CREATE_EMAIL_PROMPT, "").trim());
        }

        else if (cleanInput.startsWith(SEND_EMAIL_PROMPT, 0) && !(cleanInput.equals(SEND_EMAIL_PROMPT))) {
            // prompt = new sendEmailPrompt();
        }

        return prompt;
    }
}
