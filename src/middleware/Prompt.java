package middleware;

public interface Prompt {
    public Prompt createPrompt(String prompt);
    public String toString();
}