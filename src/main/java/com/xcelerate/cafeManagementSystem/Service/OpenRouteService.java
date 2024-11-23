package com.xcelerate.cafeManagementSystem.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OpenRouteService {
    private final OkHttpClient client = new OkHttpClient();

    @Value("${ORSKey}")
    private String ORSKey;

    public String getEstimatedTime(String dest_lat, String dest_lon) {
        final String origin = "33.656937,73.015123";
        String url = "https://api.openrouteservice.org/v2/directions/driving-car";

        // Prepare JSON body with coordinates and radiuses
        String jsonBody = String.format("""
        {
            "coordinates": [
                [%s, %s],
                [%s, %s]
            ],
            "radiuses": [10000, 10000]
        }
        """, origin.split(",")[1], origin.split(",")[0], dest_lon, dest_lat);

        System.out.println(url);

        // Create the request
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", ORSKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                double durationInSeconds = jsonObject
                        .getAsJsonArray("routes")
                        .get(0)
                        .getAsJsonObject()
                        .get("summary")
                        .getAsJsonObject()
                        .get("duration")
                        .getAsDouble();

                double durationInMinutes = durationInSeconds / 60;
                double durationInHours = durationInMinutes / 60;

                if (durationInHours < 1) {
                    return String.format("%.2f minutes", durationInMinutes);
                } else {
                    int hours = (int) durationInHours;
                    double remainingMinutes = durationInMinutes % 60;
                    return String.format("%d hours %.2f minutes", hours, remainingMinutes);
                }
            } else {
                System.out.println("Request failed: " + response.code());
                if (response.body() != null) {
                    System.out.println("Error response: " + response.body().string());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
