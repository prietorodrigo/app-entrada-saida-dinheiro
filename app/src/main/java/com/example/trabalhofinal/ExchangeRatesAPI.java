package com.example.trabalhofinal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExchangeRatesAPI {
    @GET("latest.json")
    Call<ExchangeRatesResponse> getLatestRates(@Query("app_id") String appId, @Query("base") String base);
}
