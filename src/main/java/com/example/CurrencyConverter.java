package com.example;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyConverter {

    private Currency originCurrency;
    private Currency destinationCurrency;
    private double amount;

    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    // Construtor para inicializar os parâmetros
    public CurrencyConverter(Currency originCurrency, Currency destinationCurrency, double amount) {
        this.originCurrency = originCurrency;
        this.destinationCurrency = destinationCurrency;
        this.amount = amount;
    }

    public void convert() {
        String key = CurrencyUtils.getApiKey();

        String url = BASE_URL + key + "/pair/" + originCurrency.getCode() + "/" + destinationCurrency.getCode() + "/" + amount;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            CurrencyResponse currencyResponse = gson.fromJson(response.body(), CurrencyResponse.class);

            System.out.println("Resultado da conversão: " + currencyResponse.getConversionResult());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}