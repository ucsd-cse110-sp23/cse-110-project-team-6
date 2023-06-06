package SayItAssistant.middleware;

public class ClearAllPrompt implements IPrompt {
    private int promptNumber;
    private boolean STORABLE = false;
    private String MESSAGE = "Welcome to SayIt Assistant";

    public ClearAllPrompt createPrompt(String prompt) {
        return new ClearAllPrompt();
    }

    public ClearAllPrompt() {

    }

    public String toString() {
        return "";
    }

    public void setPromptNumber(int promptNumber) {
        this.promptNumber = promptNumber;
    }

    public int getPromptNumber() {
        return this.promptNumber;
    }

    public String getMessage() {
        return MESSAGE;
    }

    public boolean isStorable() {
        return STORABLE;
    }
}