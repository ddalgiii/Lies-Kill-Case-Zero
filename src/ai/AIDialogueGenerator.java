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

    public static String generateText(String userInput) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

        } catch (Exception ex) {
            return "Exception: " + ex.getMessage();
        }
    }
}
