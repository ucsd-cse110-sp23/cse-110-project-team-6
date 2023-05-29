package middleware;

public interface StartButtonSubject {
    public void registerStartButtonObserver(StartButtonObserver o);
    public void removeHStartButtonObserver(StartButtonObserver o);
    public void notifyStartButtonObservers(IPrompt prompt, IResponse response);
}