package com.sm.shurjopaysdk.networking;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("sp-data.php")
    Call<String> getHtmlForm(@Field("spdata") String jsdata);
}
