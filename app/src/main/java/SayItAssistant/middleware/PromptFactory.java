package SayItAssistant.middleware;

import SayItAssistant.frontend.CommandPanel;

//import java.lang.ref.Cleaner.Cleanable;

public class PromptFactory {
    
    private final String QUESTION_PROMPT        = "question";
    private final String DELETE_PROMPT          = "delete prompt";
    private final String CLEAR_ALL_PROMPTS      = "clear all";
    private final String SETUP_EMAIL_PROMPT     = "set up email";
    private final String SETUP_EMAIL_PROMPT_ALT = "setup email";
    private final String CREATE_EMAIL_PROMPT    = "create email";
    private final String SEND_EMAIL_PROMPT      = "send email";
    private final String PUNCTUATION = "[^\\p{L}\\p{Z}]";   //RegEx to cover all punctuation in a String

    public IPrompt createPrompt(String input) {

        IPrompt prompt = null;
        String inputLC = input.toLowerCase();
        System.out.println(input + "\n");

        if (inputLC.startsWith(QUESTION_PROMPT, 0)) {
            System.out.println("Creating question prompt");
            prompt = new Question(input);
        }

        else if (inputLC.startsWith(DELETE_PROMPT, 0)) {
            // prompt = new DeletePrompt();
            // prompt_command_pair = new Object[] {prompt, DELETE_PROMPT};
        }

        else if (inputLC.startsWith(CLEAR_ALL_PROMPTS, 0)) {
            // prompt = new ClearAllPrompt();
        }

        else if (inputLC.startsWith(SETUP_EMAIL_PROMPT, 0)) {
            // prompt = new setUpEmailPrompt();
        }

        else if (inputLC.startsWith(CREATE_EMAIL_PROMPT)) {
            // prompt = new createEmailPrompt(input.replaceFIrst(CREATE_EMAIL_PROMPT, "").trim());
        }

        else if (inputLC.startsWith(SEND_EMAIL_PROMPT, 0)) {
            // prompt = new sendEmailPrompt();
        }

        return prompt;
    }
}
