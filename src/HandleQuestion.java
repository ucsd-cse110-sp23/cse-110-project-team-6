import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;


public class HandleQuestion implements HttpHandler {

    private static final String DATA = "data.json";
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "Request Received";
        String method = exchange.getRequestMethod();
        if(method.equals("PUT")) {
            response = handlePut(exchange);
        }
        else if(method.equals("GET")) {
            response = handleGet(exchange);
        }
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static String IStoStr(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null)
            sb.append(line);
        return sb.toString();
    }
    private String handlePut(HttpExchange exchange) throws IOException {
        String question = exchange.getRequestURI().getQuery();
        String body = IStoStr(exchange.getRequestBody());
        JSONObject history = new JSONObject(body);
        Map<String,String> params = parseQueryParams(question);
        String content = new String(Files.readAllBytes(Paths.get(DATA)));
        JSONObject json = new JSONObject(content);
        String password = json.getJSONObject(params.get("u")).getString("password");
        if(password.equals(params.get("p"))){
            json.getJSONObject(params.get("u")).put("history", history);
        FileWriter file = new FileWriter(DATA);
        file.write(json.toString());
        file.close();
        return "posted";
    }
        return "Incorrect";    
    }

    private String handleGet(HttpExchange exchange) throws IOException {
        String question = exchange.getRequestURI().getQuery();
        Map<String,String> params = parseQueryParams(question);
        String content = new String(Files.readAllBytes(Paths.get(DATA)));
        JSONObject json = new JSONObject(content);
        if(params.containsKey("new")){
            if(!json.has(params.get("u"))){
                JSONObject userData = new JSONObject();
                userData.put("password", params.get("p"));
                userData.put("history",new JSONObject());
                json.put(params.get("u"), userData);
                FileWriter file = new FileWriter(DATA);
                file.write(json.toString());
                file.close();
                return "True";
            }else{
                return "Incorrect";
            }
        }
        if(json.getJSONObject(params.get("u")) != null){
            String password = json.getJSONObject(params.get("u")).getString("password");
            if(password.equals(params.get("p"))){
                return json.getJSONObject(params.get("u")).getJSONObject("history").toString();
            }
        }   
        return "Incorrect";
    }

    private static Map<String, String> parseQueryParams(String query) throws UnsupportedEncodingException {
        Map<String, String> queryParams = new HashMap<>();

        if (query != null) {
            String[] pairs = query.split("&");

            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");

                queryParams.put(key, value);
            }
        }

        return queryParams;
    }
}
