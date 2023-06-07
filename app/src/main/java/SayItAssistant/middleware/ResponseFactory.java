package SayItAssistant.middleware;

import SayItAssistant.frontend.EmailSetup;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ResponseFactory {

    IAPIRequest whisperRequest;
    HistoryManager history;

    public ResponseFactory(HistoryManager history, IAPIRequest whisperRequest) {
        this.history = history;
        this.whisperRequest = whisperRequest;
    }

    public IResponse createResponse(IPrompt prompt) {

        IResponse response = null;

        if (prompt instanceof Question) {

            if (prompt.toString().equals("Sending email...")) {
                String[] words = prompt.getMessage().split(" ");
                String dest = "noemail";
                for (String w : words) {
                    if (w.contains("@")) {
                        //get last word with @
                        if (w.charAt(w.length() - 1) == '.') {
                            w = w.substring(0, w.length() - 1); //remove period
                        }
                        dest = w;
                    }
                }
                String body = "";
                String res;
                try {
                    //get current email text
                    List<String> lines = Files.readAllLines(Paths.get("e.txt"));
                    StringBuilder sb = new StringBuilder();
                    lines.forEach(l -> sb.append(l + '\n'));
                    body = sb.toString();
                } catch (IOException e) {
                    System.out.println("Email file not found");
                }
                //send email
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(String.format("https://hlnm.pythonanywhere.com/send?user=%s&pass=%s&destination=%s",
                                history.getUsername(), history.getPassword(), dest)))
                        .POST(BodyPublishers.ofString(body))
                        .build();

                try {
                    res = client.send(request, BodyHandlers.ofString()).body();
                    System.out.println(res);
                    return new Answer(res);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                //regular GPT request
                IAPIRequest chatRequest = (whisperRequest instanceof MockWhisperRequest) ? new MockChatGPTRequest(prompt) : new ChatGPTRequest(prompt);
                String answer = chatRequest.callAPI();
                response = new Answer(answer);
            }
        } else if (prompt instanceof DeletePrompt) {
            int recentPromptNumber = AppManager.getRecentPromptNumber();
            if (recentPromptNumber != -1) {
                history.delete(recentPromptNumber);
            }
            AppManager.setRecentPromptNumber(-1);
        } else if (prompt instanceof ClearAllPrompt) {
            history.clearAll();
            AppManager.setRecentPromptNumber(-1);
        } else if (prompt instanceof SetUpEmailPrompt) {
            new EmailSetup(AppManager.getUsername(), AppManager.getPassword());
        }
        return response;
    }
}