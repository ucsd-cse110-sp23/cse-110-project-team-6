package middleware;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * class that utilizes the chatGPT API to generate a response.
 * The constructor will take in a string prompt that is generated using 
 * the whisper class to create an instance of this object where
 * an answer can be generared.
 */
public class chatGPT implements iChatGpt{

    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";
    String prompt;

    chatGPT(String prompt){
        this.prompt = prompt; 
    }

    /*
     * method to return an answer from the prompt.  
     */
    public String getAsnwer() throws IOException, InterruptedException {
        int maxTokens = 100; 
        //create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();

        requestBody.put("model", MODEL);
 
        requestBody.put("prompt", prompt);
 
        requestBody.put("max_tokens", maxTokens);
 
        requestBody.put("temperature", 1.0);
 
        //create the HTTP client 
        HttpClient client = HttpClient.newHttpClient();
 
        //create the request object
        HttpRequest request = HttpRequest
 
        .newBuilder()
 
        .uri(URI.create(API_ENDPOINT))
 
        .header("Content-Type", "application/json")
 
        .header("Authorization", String.format("Bearer %s", API_KEY))
 
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
 
        .build();
 
        // Send the request and recieve the response
        HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString()
        );
 
        String responseBody = response.body(); 
 
        JSONObject responseJson = new JSONObject(responseBody);
 
        JSONArray choices = responseJson.getJSONArray("choices");
        String generatedText = choices.getJSONObject(0).getString("text");
 
        return generatedText; 
    }
    
}
