package com.example.baking.model.api;

import android.support.annotation.VisibleForTesting;

import com.example.baking.model.Measure;
import com.example.baking.model.RecipeWithSubobjects;
import com.example.baking.utils.Log;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private static Api sDefaultApi;

    public synchronized static Api getDefault() {
        if (sDefaultApi == null) {
            sDefaultApi = getApi(null);
        }
        return sDefaultApi;
    }

    @VisibleForTesting
    public synchronized static void setDefault(Api api) {
        sDefaultApi = api;
    }

    @VisibleForTesting
    public static Api getApi(Interceptor interceptor) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (interceptor != null) {
            clientBuilder.addInterceptor(interceptor);
        }
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor((message) -> {
            Log.d("Network", message);
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        clientBuilder.addInterceptor(logging);
        OkHttpClient client = clientBuilder.build();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Measure.class, new MeasureDeserializer());
        gsonBuilder.registerTypeAdapter(RecipeWithSubobjects.class, new RecipeWithSubobjectsDeserializer());

        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                .build()
                .create(Api.class);
    }

}
