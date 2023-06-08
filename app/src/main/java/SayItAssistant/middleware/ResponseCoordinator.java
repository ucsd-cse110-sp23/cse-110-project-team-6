package SayItAssistant.middleware;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import SayItAssistant.frontend.EmailSetup;

public class ResponseCoordinator {

    // Constants for HTTP requests
    private final static String HOST = "http://127.0.0.1:5000/";
    private final static String ENDPOINT = "send";
    private final static String USER_PARAM = "?user=";
    private final static String PASS_PARAM = "&pass=";
    private final static String DEST_PARAM = "&destination=";

    IAPIRequest whisperRequest;
    HistoryManager history;

    public ResponseCoordinator (HistoryManager history, IAPIRequest whisperRequest) {
        this.history = history;
        this.whisperRequest = whisperRequest;
    }

    public IResponse createResponse(IPrompt prompt) {

        IResponse response = null;

        if (prompt instanceof Question) {

            if(prompt.toString().equals("Sending email...")){
                String[] words = prompt.getMessage().split(" ");
                String dest = "noemail";
                for(String w:words){
                    if(w.contains("@")){
                        if(w.charAt(w.length()-1) == '.'){
                            w = w.substring(0, w.length()-1);
                        }
                        dest = w;
                    } 
                }
                String body = "";
                String res = "no response";
                try {
                    List<String> lines = Files.readAllLines(Paths.get("e.txt"));
                    StringBuilder sb = new StringBuilder();
                    lines.forEach(l -> sb.append(l + '\n'));
                    body = sb.toString();
                } catch (IOException e) {
                    System.out.println("Email file not found");
                }
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(HOST + ENDPOINT + USER_PARAM + history.getUsername() + PASS_PARAM + history.getPassword() + DEST_PARAM + dest))
                    .POST(BodyPublishers.ofString(body))
                    .build();

                try {
                    res = client.send(request, BodyHandlers.ofString()).body();
                    System.out.println(res);
                    return new Answer(res);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                IAPIRequest chatRequest = (whisperRequest instanceof MockWhisperRequest) ? 
                    new MockChatGPTRequest(prompt) : new ChatGPTRequest(prompt);
                String answer = chatRequest.callAPI();
                response = new Answer(answer);
            }
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