package com.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyUtils {

    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static String getApiKey() {
        // Retorne sua chave da API aqui
        return "1d90dd497ba50270620d595b";
    }

    // Método para obter todos os códigos de moeda
    public static Map<String, String> getAllCurrencies() {
        String key = getApiKey();
        String url = BASE_URL + key + "/codes";
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
            
            // Verifica se a resposta contém o array esperado
            if (jsonObject.has("supported_codes")) {
                List<List<String>> currencyCodes = gson.fromJson(jsonObject.get("supported_codes"), new TypeToken<List<List<String>>>() {}.getType());
                
                Map<String, String> result = new HashMap<>();
                for (List<String> entry : currencyCodes) {
                    String code = entry.get(0);
                    String description = entry.get(1);
                    result.put(code, description);
                }
                return result;
            } else {
                System.out.println("A resposta da API não contém o array 'supported_codes'.");
                return null;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
