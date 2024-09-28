package com.example;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CurrencyManager currencyManager = new CurrencyManager();

        // Adicionando algumas moedas padrão
        currencyManager.addCurrency("USD", "United States Dollar");
        currencyManager.addCurrency("BRL", "Real");
        currencyManager.addCurrency("EUR", "Euro");
        currencyManager.addCurrency("GBP", "British Pound Sterling");
        currencyManager.addCurrency("JPY", "Japanese Yen");
        currencyManager.addCurrency("AUD", "Australian Dollar");

        Currency originCurrency = null; // Inicializa como null

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Escolher moeda de origem");
            System.out.println("2. Escolher moeda de destino");
            System.out.println("3. Definir chave de API alternativa");
            System.out.println("0. Sair");

            System.out.print("Escolha uma opção: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    // Passo 1: Escolha da moeda de origem
                    System.out.println("Escolha a moeda de origem:");
                    originCurrency = chooseCurrency(scanner, currencyManager);
                    System.out.println("Você escolheu a moeda de origem: " + originCurrency);
                    break;

                case 2:
                    // Passo 2: Escolha da moeda de destino
                    if (originCurrency == null) { // Verifica se a origem foi escolhida
                        System.out.println("Por favor, escolha a moeda de origem primeiro.");
                        break;
                    }

                    System.out.println("Escolha a moeda de destino para conversão:");
                    Currency destinationCurrency = chooseCurrency(scanner, currencyManager);
                    if (destinationCurrency == null) {
                        break;
                    }
                    System.out.println("Você escolheu a moeda de destino: " + destinationCurrency);

                    System.out.print("Digite o valor que deseja converter de " + originCurrency + " para " + destinationCurrency + ": ");
                    double amount = scanner.nextDouble();

                    CurrencyConverter converter = new CurrencyConverter(originCurrency, destinationCurrency, amount);
                    converter.convert();
                    break;

                case 3:
                    // Opção para definir chave de API alternativa
                    System.out.print("Digite a nova chave de API: ");
                    String newApiKey = scanner.next();
                    if (CurrencyUtils.testApiKey(newApiKey)) {
                        CurrencyUtils.setAlternativeApiKey(newApiKey);
                        System.out.println("Chave de API alternativa definida com sucesso.");
                    } else {
                        System.out.println("Chave de API inválida. Mantendo a chave padrão.");
                    }
                    break;

                case 0:
                    System.out.println("Saindo...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Escolha inválida.");
                    break;
            }
        }
    }

    private static Currency chooseCurrency(Scanner scanner, CurrencyManager currencyManager) {
        List<Currency> currencies = currencyManager.listCurrencies();
        int index = 1;

        // Exibir as moedas
        for (Currency currency : currencies) {
            System.out.println(index + ". " + currency);
            index++;
        }
        System.out.println(index + ". Outras");

        System.out.print("Digite o número correspondente à sua escolha: ");
        int choice = scanner.nextInt();

        if (choice >= 1 && choice <= currencies.size()) {
            return currencies.get(choice - 1);
        } else if (choice == currencies.size() + 1) {
            return chooseFromApiCurrencies(scanner);
        } else {
            System.out.println("Escolha inválida.");
            return chooseCurrency(scanner, currencyManager);
        }
    }

    private static Currency chooseFromApiCurrencies(Scanner scanner) {
        Map<String, String> currencies = CurrencyUtils.getAllCurrencies();

        if (currencies != null) {
            System.out.println("Lista de todas as moedas disponíveis:");
            int index = 1;
            for (Map.Entry<String, String> entry : currencies.entrySet()) {
                System.out.println(index + ". " + entry.getKey() + " - " + entry.getValue());
                index++;
            }

            System.out.print("Digite o número correspondente à sua escolha: ");
            int choice = scanner.nextInt();

            if (choice >= 1 && choice <= currencies.size()) {
                String selectedCode = (String) currencies.keySet().toArray()[choice - 1];
                Currency currency = new Currency(selectedCode, currencies.get(selectedCode));
                return currency;
            } else {
                System.out.println("Escolha inválida.");
                return chooseFromApiCurrencies(scanner);
            }
        } else {
            System.out.println("Não foi possível obter a lista de moedas.");
            return null;
        }
    }
}