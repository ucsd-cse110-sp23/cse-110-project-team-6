package middleware;

public interface IPrompt {
    public IPrompt createPrompt(String prompt);
    public String toString();
}