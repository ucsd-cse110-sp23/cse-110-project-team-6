package middleware;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public interface HttpInterface {
    public void buildRequest(String endpoint, JSONObject body);
    public HttpResponse<String> sendRequest(HttpRequest request);
}
