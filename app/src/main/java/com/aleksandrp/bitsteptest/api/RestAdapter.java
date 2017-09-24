package com.aleksandrp.bitsteptest.api;

import android.util.Log;

import com.aleksandrp.bitsteptest.utils.SettingsApp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public class RestAdapter {

    public static final String API_BASE_URL = "http://164.132.224.6/test/";
    public static final String API_BASE_URL_ICON = "http://164.132.224.6/test/";

    private Retrofit retrofit;

    public RestAdapter() {
    }

    public void init(final boolean token, String method) {
        Log.d("INIT", "method " + method);
        final String authToken = SettingsApp.getInstance().getTokenServer();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)      // for log request
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                                Request request;
                                if (!token) {
                                    request = chain.request().newBuilder()
//                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                                .addHeader("Accept", "application/json")
                                            .build();
                                } else {
                                    request = chain.request().newBuilder()
                                            .addHeader("Authorization", "bearer " + authToken)
//                                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                                    .addHeader("Accept", "application/json")
//                                    .url(url)
                                            .build();
                                }


                                return chain.proceed(request);
                            }
                        })
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();

        HttpUrl httpUrl = HttpUrl.parse(API_BASE_URL);

        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(defaultHttpClient)
                .build();

    }


    public Retrofit getRetrofit() {
        return retrofit;
    }


}
