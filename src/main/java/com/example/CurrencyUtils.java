package com.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CurrencyUtils {

    public static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private static String apiKey;

    static {
        loadApiKey();
    }

    private static void loadApiKey() {
        try (InputStream input = CurrencyUtils.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Desculpe, não foi possível encontrar o arquivo config.properties.");
                return;
            }
            prop.load(input);
            apiKey = prop.getProperty("apiKey");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getApiKey() {
        return apiKey;
    }

    // Método para obter todos os códigos de moeda
    public static Map<String, String> getAllCurrencies() {
        String url = BASE_URL + getApiKey() + "/codes";
        
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

    // Método para testar a chave da API
    // Escolhi adicionar um metodo para usar uma chave de API diferente sem sobrescrever a original
    // para que eu possa usar uma chave padrao facilitando o uso no periodo em que esta chave e valida
    // mas que o projeto nao necessite de alteracoes futuramente, caso a chave venha a vencer.
    public static boolean testApiKey(String apiKey) {
        String url = BASE_URL + apiKey + "/codes";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);

            return jsonObject.has("supported_codes");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setAlternativeApiKey(String newApiKey) {
        apiKey = newApiKey;
    }
}