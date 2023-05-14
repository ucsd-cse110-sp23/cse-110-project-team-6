package middleware;

/**
 * Interface for the Subject of the Observer Pattern
 */
public interface Subject {
    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObservers();
}