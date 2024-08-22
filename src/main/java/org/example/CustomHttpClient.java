package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomHttpClient {

    private ObjectMapper objectMapper = new ObjectMapper();

    public String executeGet(String resourceUrl) {
        System.out.println("Executing GET request to " + resourceUrl);
        try {
            URL url = new URL(resourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");


            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Successfully retrieved response: " + response);
                return response.toString();
            } else {
                throw new RuntimeException("API call failed, response code: " + responseCode);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unexpected error: " + ex.getMessage());
        }
    }

    public void executePost(String resourceUrl, Object postData) {
        System.out.println("Executing POST request to " + resourceUrl);
        try {
            URL url = new URL(resourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = objectMapper.writeValueAsString(postData).getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Successfully executed, response: " + (response.toString().isEmpty() ? "NO DATA" : response));
            } else {
                throw new RuntimeException("API call failed, response code: " + responseCode);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unexpected error: " + ex.getMessage());
        }
    }
}
