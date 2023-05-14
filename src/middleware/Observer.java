package middleware;

/**
 * Interface for the Observer of the Observer Pattern
 */
public interface Observer {
    public void update(Question question, Answer answer);
}