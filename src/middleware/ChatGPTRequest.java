package middleware;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGPTRequest implements IAPIRequest {

    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";
    private static final int    MAX_TOKENS = 100;
    private static final double TEMPERATURE = 1.0; // Percentage of randomness in response

    private HttpClient client;      // Program that sends requests to server and receives responses
    private HttpRequest request;    // Request for specific action to be performed on server
    private JSONObject requestBody; // Data containing the parameters for the request

    /**
     * Constructor for ChatGPTRequest class 
     * Initializes the client, request, and requestBody fields
     * 
     * @param prompt Question object containing the prompt for the chatbot
     */
    public ChatGPTRequest(Question prompt) {
        this.client = HttpClient.newHttpClient();

        this.requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("max_tokens", MAX_TOKENS);
        requestBody.put("temperature", TEMPERATURE);
        requestBody.put("prompt", prompt.toString());

        this.request = HttpRequest.newBuilder()
            .uri(URI.create(API_ENDPOINT)) // Identifies resource on the web similar to URL
            .header("Content-Type", "application/json") // Specifies format of data in request body
            .header("Authorization", String.format("Bearer %s", API_KEY)) 
            .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
            .build();
    }

    /**
     * Sends the request to the server and returns the response as a String
     * 
     * @return String containing the response from the server
     */
    @Override
    public String callAPI() {
        HttpResponse<String> response = null;

        try {
            response = client.send(this.request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.err.println("Error sending request to server");
            if (e instanceof IOException)
                System.err.println("IOException: " + e.getMessage());
            else if (e instanceof InterruptedException)
                System.err.println("InterruptedException: " + e.getMessage());
            e.printStackTrace();
        }

        JSONObject json    = new JSONObject(response.body());
        JSONArray  choices = json.getJSONArray("choices");
        String     answer  = choices.getJSONObject(0).getString("text");

        return answer;
    }
    
}
