package com.example.trabalhofinal;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class ExchangeRatesResponse {
    @SerializedName("rates")
    private Map<String, Double> rates;

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}
