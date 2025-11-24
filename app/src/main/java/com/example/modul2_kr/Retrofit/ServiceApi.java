package com.example.modul2_kr.Retrofit;

import com.example.modul2_kr.Entity.Drinks;
import com.example.modul2_kr.Entity.DrinksResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface ServiceApi {
    @GET("drinkins")
    Call<DrinksResponse> getDrinks();;
    @GET("drinkins/{id}")
    Call<Drinks> getDrinksId(@Path("id") int id);
    @POST("drinkins")
    Call<List<Drinks>> addDrinks();
}
