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
public class WhisperRequest implements APIRequest {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String API_KEY = "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";
    private static final String MODEL = "whisper-1";
    private File file;

    WhisperRequest(String filePath) {
        file = new File(filePath);
    }

    /**
     * Helper method to write a parameter to output stream in multipart form data format
     *
     * Identifies the parameters of the additonal fields of data to be sent to the server
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
     * Formats audio file to be sent to server in multipart form data format within output stream
     * 
     * @param outputStream Stream to write file to
     * @param file File to be sent to server
     * @param boundary Indication of start of new parameter
     * @throws IOException
     */
    private static void writeFileToOutputStream(
        OutputStream outputStream,
        File file,
        String boundary
    ) throws IOException {
        
        // Mark the start of the file
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        
        // Provide information about the file
        outputStream.write(
            (
                "Content-Disposition: form-data; name=\"file\"; filename=\"" + 
                file.getName() + 
                "\"\r\n"
            ).getBytes()
        );
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

        // Write the file's contents to the output stream   
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }

    /**
     * Sends the request to the server and returns the response as an HttpResponse object
     * 
     * @return HttpResponse object containing the response from the server
     */
    @SuppressWarnings("deprecation")
    @Override
    public String sendRequest()  {

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

            // Write model parameter to request body
            writeParameterToOutputStream(outputStream, "model", MODEL, boundary);

            // Write file to request body
            writeFileToOutputStream(outputStream, file, boundary);

            // Write closing boundary to request body
            outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

            // Flush and close output stream
            outputStream.flush();
            outputStream.close();

            // Get response code
            int responseCode = connection.getResponseCode();

            

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }
    
    /**
     * Sends the request to the server and returns the response as a String
     * 
     * @return String containing the response from the server
     */
    public String getResponse() {
        // TODO Auto-generated method stub
        return null;
    }
}
