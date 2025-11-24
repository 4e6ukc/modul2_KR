package com.example.modul2_kr;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modul2_kr.Entity.Drinks;
import com.example.modul2_kr.Entity.DrinksResponse;
import com.example.modul2_kr.Retrofit.ServiceApi;
import com.example.modul2_kr.Retrofit.retrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Находим наш TextView по ID
        resultTextView = findViewById(R.id.EditText);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        service = retrofitClient.getRetrofit().create(ServiceApi.class);

        getAllDrinks();
    }

    /**
     * 1. Запрос GET для получения ВСЕХ напитков
     */
    private void getAllDrinks() {
        Log.d(TAG, "--- 1. Запрос на получение всех напитков (GET /drinks) ---");
        resultTextView.setText("1. Получение списка всех напитков...");

        // Тип вызова теперь <DrinksResponse>
        Call<DrinksResponse> call = service.getDrinks();

        call.enqueue(new Callback<DrinksResponse>() {
            @Override
            public void onResponse(Call<DrinksResponse> call, Response<DrinksResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Получаем список из объекта-обертки
                    List<Drinks> drinksList = response.body().getDrinks();

                    StringBuilder sb = new StringBuilder("СПИСОК НАПИТКОВ:\n");
                    for (Drinks drink : drinksList) {
                        Log.i(TAG, "Напиток: " + drink.getName());
                        sb.append("- ").append(drink.getName()).append("\n");
                    }
                    resultTextView.setText(sb.toString());

                    // После успешного получения списка, запрашиваем один напиток по ID
                    if (!drinksList.isEmpty()) {
                        getSingleDrinkById(drinksList.get(2).getId()); // Возьмем ID третьего напитка для примера
                    }

                } else {
                    String errorMsg = "Ответ не удался. Код: " + response.code();
                    Log.w(TAG, errorMsg);
                    resultTextView.setText(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<DrinksResponse> call, Throwable throwable) {
                Log.e(TAG, "ОШИБКА (GET /drinks): ", throwable);
                resultTextView.setText("Ошибка сети: " + throwable.getMessage());
            }
        });
    }

    /**
     * 2. Запрос GET для получения ОДНОГО напитка по ID
     */
    private void getSingleDrinkById(int id) {
        Log.d(TAG, "\n--- 2. Запрос на получение напитка по ID=" + id + " (GET /drinks/{id}) ---");
        // Добавляем информацию в TextView, не стирая старую
        resultTextView.append("\n\n2. Получение напитка с ID=" + id + "...");

        Call<Drinks> call = service.getDrinksId(id);
        call.enqueue(new Callback<Drinks>() {
            @Override
            public void onResponse(Call<Drinks> call, Response<Drinks> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Drinks drink = response.body();
                    String drinkInfo = "Получен напиток: " + drink.getName() + "\nОписание: " + drink.getDescription();
                    Log.i(TAG, drinkInfo);
                    resultTextView.append("\n" + drinkInfo);

                    // После успешного получения, делаем POST запрос
                    postNewDrink();

                } else {
                    String errorMsg = "Ответ не удался. Код: " + response.code();
                    Log.w(TAG, errorMsg);
                    resultTextView.append("\n" + errorMsg);
                }
            }

            @Override
            public void onFailure(Call<Drinks> call, Throwable throwable) {
                Log.e(TAG, "ОШИБКА (GET /drinks/{id}): ", throwable);
                resultTextView.append("\nОшибка сети: " + throwable.getMessage());
            }
        });
    }

    /**
     * 3. Запрос POST для "добавления" нового напитка
     *
     */
    private void postNewDrink() {
        Log.d(TAG, "\n--- 3. Запрос на добавление напитка (POST /drinks) ---");
        resultTextView.append("\n\n3. Отправка нового напитка...");

        Call<List<Drinks>> call = service.addDrinks(); // В вашем API он возвращает List<Drinks>
        call.enqueue(new Callback<List<Drinks>>() {
            @Override
            public void onResponse(Call<List<Drinks>> call, Response<List<Drinks>> response) {
                // Для POST-запроса часто важен сам факт успеха (код 200, 201)
                if (response.isSuccessful()) {
                    String successMsg = "POST-запрос выполнен успешно! Код: " + response.code();
                    Log.i(TAG, successMsg);
                    resultTextView.append("\n" + successMsg);
                } else {
                    String errorMsg = "Ответ на POST не удался. Код: " + response.code();
                    Log.w(TAG, errorMsg);
                    resultTextView.append("\n" + errorMsg);
                }
            }

            @Override
            public void onFailure(Call<List<Drinks>> call, Throwable throwable) {
                Log.e(TAG, "ОШИБКА (POST /drinks): ", throwable);
                resultTextView.append("\nОшибка сети: " + throwable.getMessage());
            }
        });
    }
}