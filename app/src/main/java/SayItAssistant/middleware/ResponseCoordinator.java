package SayItAssistant.middleware;

import SayItAssistant.frontend.EmailSetup;

public class ResponseCoordinator {

    IAPIRequest whisperRequest;
    HistoryManager history;

    /*
     * Constructs a coordinator based on the history and whisper type.
     */
    public ResponseCoordinator (HistoryManager history, IAPIRequest whisperRequest) {
        this.history = history;
        this.whisperRequest = whisperRequest;
    }

    /*
     * Creates a response dependent upon the type of prompt entered.
     */
    public IResponse createResponse(IPrompt prompt) {

        IResponse response = null;

        // if a question is entered, a new answer is created based off of the question
        if (prompt instanceof Question) {
            IAPIRequest chatRequest = (whisperRequest instanceof MockWhisperRequest) ? 
                new MockChatGPTRequest(prompt) : new ChatGPTRequest(prompt);
            String answer = chatRequest.callAPI();
            response = new Answer(answer);
        }

        // if a prompt is requested to be deleted, it is deleted based off of its prompt number
        else if (prompt instanceof DeletePrompt) {
            int recentPromptNumber = 0;
            recentPromptNumber = AppManager.getRecentPromptNumber();
            if (recentPromptNumber != -1) {
                System.out.println("Deleting prompt " + recentPromptNumber);
                history.delete(recentPromptNumber);
            }
            AppManager.setRecentPromptNumber(-1);
        }

        // if all prompts are requested to be cleared, they are.  The prompt number is also reset
        else if (prompt instanceof ClearAllPrompt) {
            System.out.println("Clearing all...");
            history.clearAll();
            AppManager.setRecentPromptNumber(-1);
        }

        // if an email is requested to be setup, the UI pops up for it
        else if (prompt instanceof SetUpEmailPrompt) {
            System.out.println("Setting up email...");
            new EmailSetup(AppManager.getUsername(), AppManager.getPassword());
        }

        else if (prompt == null) {

        }
        return response;
    }
}
