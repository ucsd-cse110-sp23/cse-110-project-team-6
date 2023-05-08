package middleware;

import java.io.File;

/**
 * This interface is responsible for delegating user inputs to the appropriate API
 */
public interface IAssistant {
    public String[] respond();
}
