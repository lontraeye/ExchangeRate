package com.example;

import java.util.ArrayList;
import java.util.List;

public class CurrencyManager {
    private final List<Currency> currencies;

    public CurrencyManager() {
        this.currencies = new ArrayList<>();
    }

    // Método para adicionar uma moeda
    public void addCurrency(String code, String fullName) {
        currencies.add(new Currency(code, fullName));
    }

    // Método para listar todas as moedas
    public List<Currency> listCurrencies() {
        return currencies;
    }

    // Método para obter uma moeda por código
    public Currency getCurrencyByCode(String code) {
        for (Currency currency : currencies) {
            if (currency.getCode().equalsIgnoreCase(code)) {
                return currency;
            }
        }
        return null;
    }
}