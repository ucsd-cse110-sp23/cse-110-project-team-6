package middleware;

public interface HistoryButtonSubject {
    public void registerHistoryButtonObserver(HistoryButtonObserver o);
    public void removeHistoryButtonObserver(HistoryButtonObserver o);
    public void notifyHistoryButtonObservers(IPrompt prompt, IResponse response);
}
