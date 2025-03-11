package ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class AIDialogueGenerator {
    // Hugging Face Inference API endpoint for GPT-2 (you can change the model if desired)
    private static final String HF_API_URL = "https://api-inference.huggingface.co/models/gpt2";
    // Replace with your Hugging Face API token (free tier available)
    private static final String HF_API_TOKEN = "hugging face API token";

    /**
     * Generates text based on the given prompt using the Hugging Face Inference API.
     *
     * @param prompt the prompt to send to the model
     * @return the generated text or an error message
     */
    public static String generateText(String prompt) {
        try {
            // Build the JSON payload
            JsonObject jsonBody = new JsonObject();
            jsonBody.addProperty("inputs", prompt);
            // Optionally, you can add parameters such as max_length, temperature, etc.
            String requestBody = new Gson().toJson(jsonBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(HF_API_URL))
                    .header("Authorization", "Bearer " + HF_API_TOKEN)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Debug: print raw response (optional)
            System.out.println("Raw API Response: " + response.body());
            
            // The response is typically a JSON array; parse it:
            JsonArray jsonArray = new Gson().fromJson(response.body(), JsonArray.class);
            if (jsonArray.size() > 0) {
                JsonObject firstResult = jsonArray.get(0).getAsJsonObject();
                // For GPT-2 on Hugging Face, the key is usually "generated_text"
                String generatedText = firstResult.get("generated_text").getAsString();
                return generatedText;
            } else {
                return "API Error: No generated text returned.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating text: " + e.getMessage();
        }
    }
}
