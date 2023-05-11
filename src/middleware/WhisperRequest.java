package middleware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONException;


/*
 * Class that takes in a filepath string in the constructor
 * to create an instance of this object. The pupose of this class
 * is to utilize the Whisper API to convert an audio file 
 * to a String to be used in the construction of a 
 * chatGPT object. 
 */
public class WhisperRequest implements IAPIRequest {
    private static final File AUDIO_FILE = new File("prompt.wav");
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String API_KEY = "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";
    private static final String MODEL = "whisper-1";

    /**
     * Constructor for WhisperRequest object
     * 
     * @require AUDIO_FILE == ".mp3" || ".wav" || ".flac" || other form of audio file
     */
    public WhisperRequest() {}

    /**
     * Helper method to write a parameter to output stream in multipart form data format
     *
     * Essentially writes the metadata for the audio file to be sent to the server.
     * 
     * @param outputStream Stream to write parameter to
     * @param parameterName Name of related data to be sent to server
     * @param parameterValue Value of related data to be sent to server
     * @param boundary Indication of start of new parameter
     * @throws IOException
     */
    private static void writeParameterToOutputStream(
        OutputStream outputStream,
        String parameterName,
        String parameterValue,
        String boundary
    ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
            (
                "Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n"
            ).getBytes()
        );
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    /**
     * Helper method to write a file to output stream in multipart form data format
     * 
     * Multipart form data format is used to send files to the server. It formats data into
     * multiple sections identified by a unique boundary. Each section contains a header
     * followed by actual data payload where the header provides metadata such as 
     * content type, content length, and other relevant information.
     * 
     * @param outputStream Stream to write file to
     * @param file File to be sent to server
     * @param boundary Indication of start of new parameter
     * @throws IOException
     */
    private static void writeFileToOutputStream(
        OutputStream outputStream,
        String boundary
    ) throws IOException {
        
        // Mark the start of the file
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        
        // Provide information about the file
        outputStream.write(
            (
                "Content-Disposition: form-data; name=\"file\"; filename=\"" + 
                AUDIO_FILE.getName() + 
                "\"\r\n"
            ).getBytes()
        );
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

        // Write the file's contents to the output stream   
        FileInputStream fileInputStream = new FileInputStream(AUDIO_FILE);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }

    /**
     * Helper method to handle response from server
     * 
     * @param connection Connection to server
     * @return String containing response from server
     * @throws IOException
     * @throws JSONException
     */
    private static String handleSuccessResponse(HttpURLConnection connection) 
    throws IOException, JSONException{
        // Create reader to read response from server
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
        );

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();

        // Convert response to JSON object
        JSONObject result = new JSONObject(response.toString());
        return result.getString("text");
    }

    /**
     * Helper method to handle error response from server
     * 
     * @param connection Connection to server
     * @return String containing error response from server
     * @throws IOException
     * @throws JSONException
     */
    private static String handleErrorResponse(HttpURLConnection connection) 
    throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(
            new InputStreamReader(connection.getErrorStream())
        );
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        return "Error Result: " + errorResult;
    }

    /**
     * Sends the request to the server and returns the response as a string
     * 
     * @return HttpResponse object containing the response from the server
     */
    @SuppressWarnings("deprecation")
    @Override
    public String callAPI() {
        try {
            // Set up connection
            URL url = new URL(API_ENDPOINT); // Identifies resource on the web
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set up request body
            String boundary = "Boundary-" + System.currentTimeMillis(); // Identifier for parts
            connection.setRequestProperty(
                "Content-Type",
                "multipart/form-data; boundary=" + boundary
            );
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

            // Setup output stream to write request body
            OutputStream outputStream = connection.getOutputStream();

            // Write the main header of the multipart form data
            writeParameterToOutputStream(outputStream, "model", MODEL, boundary);

            // Write file to request body of the multipart form data
            writeFileToOutputStream(outputStream, boundary);

            // Write closing boundary to request body
            outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

            // Flush and close output stream
            outputStream.flush();
            outputStream.close();

            // Get response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return handleSuccessResponse(connection);
            } else {
                return handleErrorResponse(connection);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }
}
