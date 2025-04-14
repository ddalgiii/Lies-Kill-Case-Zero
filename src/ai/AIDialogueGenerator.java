package ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;package ai;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.json.JSONObject;

public class AIDialogueGenerator {
    private static final String API_URL = " your API URL "; // Ensure correct API URL
    private static final int CONNECT_TIMEOUT = 10000; //10 seconds
    private static final int READ_TIMEOUT = 10000;

    public static String generateText(String userInput) {
        HttpURLConnection conn = null; //intilize
        try {
            URL url = new URL(API_URL);
            conn = (HttpURLConnection) url.openConnection();

            //timeouts
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Create JSON payload
            JSONObject json = new JSONObject();
            json.put("model", "model you are using"); // Ensure correct model name
            json.put("prompt", userInput);
            json.put("stream", false);

            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8)) {
                    String fullResponse = scanner.useDelimiter("\\A").next();
                    JSONObject responseJson = new JSONObject(fullResponse);

                    // Only return the actual AI message
                    return responseJson.getString("response").trim();
                }
            } else {
                return "Error: Received response code " + responseCode;
            }

        } catch (java.net.SocketTimeoutException e) {
            return "Error: Connection timed out.";
        } catch (Exception ex) {
            return "Exception: " + ex.getMessage();
        } finally {
            // Disconnect the connection to release resources
            // It's important to do this in a finally block to ensure it happens
            // even if exceptions occur.
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
