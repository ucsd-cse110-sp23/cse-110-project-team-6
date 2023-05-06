package middleware;

import java.io.*;
import java.net.*;
import org.json.*;
import org.junit.runner.Request;

/*
 * Class that takes in a filepath string in the constructor
 * to create an instance of this object. The pupose of this class
 * is to utilize the Whisper API to convert an audio file 
 * to a String to be used in the construction of a 
 * chatGPT object. 
 */
public class Whisper implements iWhisper {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";
    File file; 

    Whisper(String filePath){
        file = new File(filePath);
    }

    /*
     * writing a parameter to the output stream in multipart form data format
     * that is specific for the Whisper API. 
     * 
     * Returns: nothing
     */
    public static void writeParameterToOutputStream(
        OutputStream outputStream,
        String parameterName,
        String parameterValue,
        String boundary
    ) throws IOException{

        outputStream.write(("--" + boundary + "\r\n").getBytes());

        outputStream.write(
            (
                "Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n"   
            ).getBytes()
        );

        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    /*
     * Helper method to write the audio file to the output stream in multipart form
     * data format for the Whisper API
     * 
     * Returns: String Prompt: 
     */
    public static void writeFileToOutputStream(
        OutputStream outputStream,
        File file, 
        String boundary
    ) throws IOException{
        outputStream.write(("--" + boundary + "\r\n").getBytes());

        outputStream.write(
            (
                "Content-Disposition: form-data; name=\"file\"; filename=\"" +
                file.getName() + 
                "\"\r\n"
            ).getBytes()
        );

        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

        //adding the audio to the parameter to send to the Whisper API
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead; 
        while ((bytesRead = fileInputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }

    /*
     * Helper method that verifies the HTTP reponse is successful
     * it will build the string that contains the transcribed data.
     */
    public static String handleSuccessResponse(HttpURLConnection connection) throws IOException, JSONException{
        //creating buffered reader that reads the data from the http connection then
        //uses the string builder to createa a response which is processed to a JSON and printed
        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
        );

        String inputLine; 
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }
        in.close();

        JSONObject reponseJson = new JSONObject(response.toString());

        String generatedText = reponseJson.getString("text");

        return generatedText; 
    }

    /*
     * Helper method to create error message when the response code is not successful
     */
    private static String handleErrorResponse(HttpURLConnection connection)
    throws IOException, JSONException{
        BufferedReader errorReader = new BufferedReader(
            new InputStreamReader(connection.getErrorStream())
        );
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null){
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        return "Error Result: " + errorResult;
    }
    

    // method to get the converted audio file to text
    public String getPrompt() throws IOException{

        // set up HTTP connection

        URL url = new URL(API_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        //setting up the request header
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty(
            "Content-Type", 
            "multipart/form-data; boundary =" + boundary
        );

        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

        //set up outpur stream ot write request body
        OutputStream outputStream = connection.getOutputStream();

        //write model paramer to request body
        writeParameterToOutputStream(outputStream, "model", MODEL, boundary);

        writeFileToOutputStream(outputStream, file, boundary);

        //write closing boundary to Request body
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

        //close output stream
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK){
            return handleSuccessResponse(connection);
        }   

        else{
            return handleErrorResponse(connection);
        }

        
    }


    
}
