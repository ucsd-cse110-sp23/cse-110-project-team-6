package SayItAssistant.middleware;

public class SetUpEmailPrompt implements IPrompt{
    private int promptNumber;
    private boolean STORABLE = false;
    private boolean UPDATES_DISPLAY = false;
    private String MESSAGE = "";

    public SetUpEmailPrompt createPrompt(String prompt) {
        return new SetUpEmailPrompt();
    }

    public SetUpEmailPrompt() {

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
