package com.sm.shurjopaysdk.networking;


import android.util.Log;

import com.sm.shurjopaysdk.utils.SPayConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
  private static final String TAG = "ApiClient";

  private Retrofit retrofit;
  private static ApiClient apiClient;

  private ApiClient() {
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor())
        .build();

    retrofit = new Retrofit.Builder()
        .baseUrl(SPayConstants.BASE_URL_TEST)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public static synchronized ApiClient getInstance() {
    if (apiClient == null) {
      apiClient = new ApiClient();
    }
    return apiClient;
  }

  public ApiInterface getApi() {
    return retrofit.create(ApiInterface.class);
  }

  private static HttpLoggingInterceptor httpLoggingInterceptor() {
    HttpLoggingInterceptor httpLoggingInterceptor =
        new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
          @Override
          public void log(String message) {
            Log.d(TAG, "httpLoggingInterceptor: message= " + message);
          }
        });
    httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
    return httpLoggingInterceptor;
  }
}
