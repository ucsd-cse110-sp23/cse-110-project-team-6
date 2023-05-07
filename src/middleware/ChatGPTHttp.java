package middleware;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class ChatGPTHttp implements HttpInterface {

    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "";
    private static final String MODEL = "text-davinci-003";

    private HttpClient client;
    private HttpRequest request;

    public ChatGPTHttp() {
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public void buildRequest(String endpoint, JSONObject body) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buildRequest'");
    }

    @Override
    public HttpResponse<String> sendRequest(HttpRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendRequest'");
    }
    
}
