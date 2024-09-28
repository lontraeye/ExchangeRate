package com.example;

public class Currency {
    private final String code;
    private final String fullName;

    public Currency(String code, String fullName) {
        this.code = code;
        this.fullName = fullName;
    }

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return code + " - " + fullName;
    }
}