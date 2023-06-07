package SayItAssistant.middleware;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PromptFactory {

    private final String QUESTION_PROMPT = "question";
    private final String DELETE_PROMPT = "delete prompt";
    private final String CLEAR_ALL_PROMPTS = "clear all";
    private final String SETUP_EMAIL_PROMPT = "set up email";
    private final String SETUP_EMAIL_PROMPT_ALT = "setup email";
    private final String CREATE_EMAIL_PROMPT = "create email";
    private final String SEND_EMAIL_PROMPT = "send email";
    private final String PUNCTUATION = "[^\\p{L}\\p{Z}]";   //RegEx to cover all punctuation in a String

    public IPrompt createPrompt(String input) {

        IPrompt prompt = null;
        String cleanInput = input.replaceAll(PUNCTUATION, "").toLowerCase();
        System.out.printf("Input: %s\n", input);
        if (cleanInput.startsWith(QUESTION_PROMPT) && !(cleanInput.equals(QUESTION_PROMPT))) {
            prompt = makeQuestion(input);
        } else if (cleanInput.equals(DELETE_PROMPT)) {
            prompt = new DeletePrompt();
        } else if (cleanInput.equals(CLEAR_ALL_PROMPTS)) {
            prompt = new ClearAllPrompt();
        } else if (cleanInput.equals(SETUP_EMAIL_PROMPT) || cleanInput.equals(SETUP_EMAIL_PROMPT_ALT)) {
            prompt = new SetUpEmailPrompt();
        } else if (cleanInput.startsWith(CREATE_EMAIL_PROMPT)) {
            try {
                List<String> lines = Files.readAllLines(Paths.get("name.txt"));
                Question q;
                try {
                    String name = lines.get(0);
                    q = new Question(String.format("%s Add %s as the sender's name after the closing phrase", input, name));
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("no display name set");
                    q = new Question(input);
                }
                q.setMESSAGE("Create Email");
                prompt = q;
            } catch (IOException e) {
                System.out.println("lol");
            }
        } else if (cleanInput.startsWith(SEND_EMAIL_PROMPT) && !cleanInput.equals(SEND_EMAIL_PROMPT)) {
            Question q = new Question("Sending email...");
            //make sure the email is in the correct format
            q.setMESSAGE(input.replace(" at ", "@"));
            return q;
        }

        return prompt;
    }

    //parses the input to create a Question prompt
    private Question makeQuestion(String input) {
        Question prompt;
        input = input.replaceFirst("(?i)" + QUESTION_PROMPT, ""); // strips input of the command, regardless of case
        input = input.replaceFirst(PUNCTUATION, "");            // removes punctuation after the command
        input = input.replaceFirst(" ", "");                    // removes space after the command
        prompt = new Question(input);                           // creates a Question prompt using the new input

        return prompt;
    }
}
