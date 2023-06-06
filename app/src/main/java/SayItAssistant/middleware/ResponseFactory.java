package SayItAssistant.middleware;

import SayItAssistant.App;

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
        else if (prompt == null) {

        }
        return response;
    }
}