package com.xcelerate.cafeManagementSystem.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmotionAnalysisService {
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson(); // Reuse a single Gson instance

    @Value("${chatApiURL}")
    private String chatApiURL;

    public String getEmotion(String text) {
        // Create the JSON payload
        Map<String, String> json = new HashMap<>();
        json.put("text", text);

        String jsonStr = gson.toJson(json);
        RequestBody body = RequestBody.create(jsonStr, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(chatApiURL + "/emotion/calculate")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Request successful: " + response.code());


                JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
                System.out.println(jsonObject);

                return jsonObject.get("emotion").getAsString();
            } else {
                System.out.println("Request failed: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
