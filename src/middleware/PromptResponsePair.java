package middleware;

public class PromptResponsePair {
    private IPrompt prompt;
    private IResponse response;

    public PromptResponsePair(IPrompt newPrompt, IResponse newResponse) {
        prompt = newPrompt;
        response   = newResponse;
    }

    public IPrompt getPrompt() {
        return prompt;
    }

    public IResponse getResponse() {
        return response;
    }
}
