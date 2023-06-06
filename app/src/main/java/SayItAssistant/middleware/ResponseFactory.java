package SayItAssistant.middleware;

import SayItAssistant.App;
import SayItAssistant.frontend.EmailSetup;

public class ResponseFactory {

    IAPIRequest whisperRequest;
    HistoryManager history;

    public ResponseFactory (HistoryManager history, IAPIRequest whisperRequest) {
        this.history = history;
        this.whisperRequest = whisperRequest;
    }

    public IResponse createResponse(IPrompt prompt) {

        IResponse response = null;

        if (prompt instanceof Question) {
            IAPIRequest chatRequest = (whisperRequest instanceof MockWhisperRequest) ? 
                new MockChatGPTRequest(prompt) : new ChatGPTRequest(prompt);
            String answer = chatRequest.callAPI();
            response = new Answer(answer);
        }

        else if (prompt instanceof DeletePrompt) {
            int recentPromptNumber = AppManager.getRecentPromptNumber();
            if (recentPromptNumber != -1) {
                System.out.println("Deleting prompt " + recentPromptNumber);
                history.delete(recentPromptNumber);
            }
            AppManager.setRecentPromptNumber(-1);
        }

        else if (prompt instanceof ClearAllPrompt) {
            System.out.println("Clearing all...");
            history.clearAll();
            AppManager.setRecentPromptNumber(-1);
        }

        else if (prompt instanceof SetUpEmailPrompt) {
            System.out.println("Setting up email...");
            new EmailSetup(AppManager.getUsername(), AppManager.getPassword());
        }

        else if (prompt == null) {

        }
        return response;
    }
}