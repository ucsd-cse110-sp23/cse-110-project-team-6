package middleware;

/**
 * Interface for the Observer of the Observer Pattern
 */
public interface Observer {
    public void update(IPrompt prompt, IResponse response);
}