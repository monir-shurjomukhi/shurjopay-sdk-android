package com.sm.shurjopaysdk.networking;

import com.sm.shurjopaysdk.model.SPDataModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("sp-data.php")
    Call<String> getHtmlForm(@Body SPDataModel spData);
}
