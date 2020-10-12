package com.sm.shurjopaysdk.networking;

import com.sm.shurjopaysdk.model.TransactionInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

  @FormUrlEncoded
  @POST("sp-data.php")
  Call<String> getHtmlForm(@Field("spdata") String jsdata);

  @GET("v1/search")
  Call<TransactionInfo> getTransactionInfo(
      @Header("Authorization") String token,
      @Query("txid") String txId
  );
}
