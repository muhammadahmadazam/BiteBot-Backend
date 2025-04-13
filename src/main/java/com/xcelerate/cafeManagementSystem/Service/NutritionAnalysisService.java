package com.xcelerate.cafeManagementSystem.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class NutritionAnalysisService{
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // Timeout for establishing connection
            .readTimeout(30, TimeUnit.SECONDS)     // Timeout for reading response
            .writeTimeout(30, TimeUnit.SECONDS)    // Timeout for writing request
            .build();
    private final Gson gson = new Gson(); // Reuse a single Gson instance

    @Value("${chatApiURL}")
    private String chatApiURL;

    public List<Long> analyzeNutrition(String items, String prompt) {
        Map<String, String> body = new HashMap<>();
        body.put("query", prompt);

        String jsonStr = gson.toJson(body);
        RequestBody requestBody = RequestBody.create(jsonStr, okhttp3.MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(chatApiURL + "/items/search")
                .post(requestBody)
                .build();

        try (okhttp3.Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Request successful: " + response.code());
                JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
                Type listType = new TypeToken<List<Long>>() {}.getType();
                return gson.fromJson(jsonObject.get("data"), listType);
            } else {
                System.out.println("Request failed: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}