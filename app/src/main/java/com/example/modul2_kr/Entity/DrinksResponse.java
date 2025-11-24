package com.example.modul2_kr.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrinksResponse {
    @SerializedName("drinks")
    private List<Drinks> drinks;

    // Геттер для получения списка
    public List<Drinks> getDrinks() {
        return drinks;
    }
}

