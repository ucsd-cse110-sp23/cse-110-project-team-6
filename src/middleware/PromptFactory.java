package middleware;

public class PromptFactory {
    
    private String QUESTION_PROMPT = "question";
    private String DELTE_PROMPT = "delete prompt";
    private String CLEAR_ALL_PROMPTS = "clear all";
    private String SETUP_EMAIL_PROMPT = "setup email";
    private String CREATE_EMAIL_PROMPT = "create email";
    private String SEND_EMAIL_PROMPT = "send email";

    public Object[] createPrompt(String input) {

        IPrompt prompt = null;
        String input_lc = input.toLowerCase();
        Object[] prompt_command_pair = {null, null};
        System.out.println(input + "\n");
        if (input_lc.startsWith(QUESTION_PROMPT)) {
            System.out.println("This is a question");
            prompt = new Question(input.replaceFirst(QUESTION_PROMPT, "").trim());
            prompt_command_pair = new Object[] {prompt, QUESTION_PROMPT};
        }

        else if (input_lc.startsWith(DELTE_PROMPT, 0)) {
            // prompt = new DeletePrompt();
            // prompt_command_pair = new Object[] {prompt, DELETE_PROMPT};
        }

        else if (input_lc.startsWith(CLEAR_ALL_PROMPTS, 0)) {
            // prompt = new ClearAllPrompt();
        }

        else if (input_lc.startsWith(SETUP_EMAIL_PROMPT, 0)) {
            // prompt = new setUpEmailPrompt();
        }

        else if (input_lc.startsWith(CREATE_EMAIL_PROMPT)) {
            // prompt = new createEmailPrompt(input.replaceFIrst(CREATE_EMAIL_PROMPT, "").trim());
        }

        else if (input_lc.startsWith(SEND_EMAIL_PROMPT, 0)) {
            // prompt = new sendEmailPrompt();
        }

        return prompt_command_pair;
    }
}
