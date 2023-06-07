package SayItAssistant.middleware;

public interface IPrompt {
    public IPrompt createPrompt(String prompt);
    public String toString();
    public void setPromptNumber(int promptNumber);
    public int getPromptNumber();
    public String getMessage();
    public boolean isStorable();
    public boolean updatesDisplay();
}