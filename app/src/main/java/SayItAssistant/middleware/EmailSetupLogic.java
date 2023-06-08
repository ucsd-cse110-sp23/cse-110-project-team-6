package SayItAssistant.middleware;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * This class handles the logic of parsing through email information
 */
public class EmailSetupLogic {

    // Constants for network requests
    //private final String HOST = "https://hlnm.pythonanywhere.com/";
    private final String HOST = "http://127.0.0.1:5000/";
    private final String EMAIL_ENDPOINT = "emails";
    private final String USER_PARAM = "?user=";
    private final String PASS_PARAM = "&pass=";

    private String username;
    private String password;

    public EmailSetupLogic(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns a JSON Object which is the response of email information from Server
     * @return JSONObject
     */
    public JSONObject getEmailInfo() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST + EMAIL_ENDPOINT + USER_PARAM + username + PASS_PARAM + password))
                .build();
        
        // Send the HTTP request and get the response body as a JSONObject
        JSONObject jsonResponse = 
            client.sendAsync(request, BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(JSONObject::new)
                    .join();

        return jsonResponse;
    }

    /**
     * Sends a request to the server to add new emai information
     */
    public void updateEmailInfo
        (String lastName, 
        String firstName, 
        String displayName, 
        String emailAddress, 
        String smtpHost, 
        String tlsPort, 
        String emailPassword) 
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("last_name", lastName);
        jsonObject.put("first_name", firstName);
        jsonObject.put("display_name", displayName);
        jsonObject.put("email_address", emailAddress);
        jsonObject.put("smtp_host", smtpHost);
        jsonObject.put("tls_port", tlsPort);
        jsonObject.put("email_password", emailPassword);

        // Convert the JSON object to a string
        String jsonString = jsonObject.toString();

        // Send off to the server
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(String.format(HOST + EMAIL_ENDPOINT + USER_PARAM + username + PASS_PARAM + password)))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(jsonString))
                .build();

        client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
    }

    /**
     * Updates the properties of the user's data to allow email
     */
    public void updateName(String username, String password) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(String.format(HOST + EMAIL_ENDPOINT + USER_PARAM + username + PASS_PARAM + password)))
            .build();

        client.sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(responseBody -> {
                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(responseBody);
                String name = jsonResponse.getString("display_name");
                try {
                    FileWriter fw = new FileWriter("name.txt");
                    fw.write(name);
                    fw.close();
                } catch (IOException e) {
                    System.out.println("writing name error");
                }
            })
            .join();   
    }
}
