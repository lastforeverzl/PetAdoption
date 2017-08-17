package com.zackyzhang.petadoption.api;

import com.zackyzhang.petadoption.ApiUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by lei on 8/4/17.
 */

public class PetFinderApi {
    private static final String TAG = "PetFinderApi";

    private PetFinderService api;
    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;

    private static PetFinderApi sInstance;

    public static PetFinderApi instance() {
        if (sInstance == null) {
            sInstance = new PetFinderApi();
        }
        return sInstance;
    }

    public PetFinderService getApi() {
        if (api == null) createApi();
        return api;
    }

    private void createApi() {
        mOkHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(logInterceptor())
                .addInterceptor(new CleanDataInterceptor())
                .build();
        api = new Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(PetFinderService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PetFinderService.class);
    }

    private static HttpLoggingInterceptor logInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.tag("OkHttp").d(message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return httpLoggingInterceptor;
    }

    private class CleanDataInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            if (response.code() == 200) {
                String rawJson = response.body().string();
                String newJson = ApiUtils.cleanInvalidSymbol(rawJson);
                String breedJson = ApiUtils.fixBreedObject(newJson);
                MediaType contentType = response.body().contentType();
                ResponseBody body = ResponseBody.create(contentType, breedJson);
                return response.newBuilder().body(body).build();
            }
            return response;
        }
    }
}
