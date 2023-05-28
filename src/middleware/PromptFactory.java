package middleware;

public class PromptFactory {
    
    private String QUESTION_PROMPT = "question";
    private String DELTE_PROMPT = "delete prompt";
    private String CLEAR_ALL_PROMPTS = "clear all";
    private String SETUP_EMAIL_PROMPT = "setup email";
    private String CREATE_EMAIL_PROMPT = "create email";
    private String SEND_EMAIL_PROMPT = "send email";

    public Prompt createPrompt(String input) {

        Prompt prompt = null;
        String input_lc = input.toLowerCase();

        if (input_lc.startsWith(QUESTION_PROMPT, 0)) {
            //prompt = new Question();
        }

        else if (input_lc.startsWith(DELTE_PROMPT, 0)) {
            // prompt = new DeletePrompt();
        }

        else if (input_lc.startsWith(CLEAR_ALL_PROMPTS, 0)) {
            // prompt = new ClearAllPrompt();
        }

        else if (input_lc.startsWith(SETUP_EMAIL_PROMPT, 0)) {
            // prompt = new setUpEmailPrompt();
        }

        else if (input_lc.startsWith(CREATE_EMAIL_PROMPT)) {
            // prompt = new createEmailPrompt();
        }

        else if (input_lc.startsWith(SEND_EMAIL_PROMPT, 0)) {
            // prompt = new sendEmailPrompt();
        }

        return prompt;
    }
}
