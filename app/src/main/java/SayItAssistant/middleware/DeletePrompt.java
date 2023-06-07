package SayItAssistant.middleware;

public class DeletePrompt implements IPrompt{
    
    private int promptNumber;
    private boolean STORABLE = false;
    private boolean UPDATES_DISPLAY = true;
    private String MESSAGE = "Welcome to SayIt Assistant";

    public DeletePrompt createPrompt(String prompt) {
        return new DeletePrompt();
    }

    public DeletePrompt() {

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

    public boolean updatesDisplay() {
        return UPDATES_DISPLAY;
    }
}
