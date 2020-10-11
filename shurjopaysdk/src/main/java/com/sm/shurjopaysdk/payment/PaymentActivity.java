package com.sm.shurjopaysdk.payment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.sm.shurjopaysdk.R;
import com.sm.shurjopaysdk.model.RequiredDataModel;
import com.sm.shurjopaysdk.model.SPDataModel;
import com.sm.shurjopaysdk.networking.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
  private static final String TAG = "PaymentActivity";
  private WebView webView;
  private ProgressBar progressBar;
  private ProgressDialog progressDialog;
  private RequiredDataModel requiredDataModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment);

    webView = findViewById(R.id.webView);
    progressBar = findViewById(R.id.progressBar);
    progressDialog = new ProgressDialog(this);
    requiredDataModel = (RequiredDataModel) getIntent().getSerializableExtra("data");

    showProgress("Please Wait...");
    getHtmlForm();
  }

  private void getHtmlForm() {
    if (requiredDataModel == null) {
      ShurjoPaySDK.listener.onFailed("User Input Error!");
      finish();
    }
    ApiClient.getInstance().getApi().getHtmlForm(
        new SPDataModel(getSpDataFromModel(requiredDataModel)))
        .enqueue(new Callback<String>() {
          @Override
          public void onResponse(Call<String> call, Response<String> response) {
            Log.d(TAG, "onResponse: " + response.body());
            hideProgress();
            try {
              if (response.isSuccessful()) {
                showWebsite(response.body());
              } else {
                ShurjoPaySDK.listener.onFailed("Payment Failed!");
                finish();
              }
            } catch (Exception e) {
              Log.e(TAG, "onResponse: ", e);
              ShurjoPaySDK.listener.onFailed("Payment Failed!");
              finish();
            }
          }

          @Override
          public void onFailure(Call<String> call, Throwable t) {
            hideProgress();
            Log.e(TAG, "onFailure: ", t);
            ShurjoPaySDK.listener.onFailed("Payment Failed!");
            finish();
          }
        });
  }

  public static String getSpDataFromModel(RequiredDataModel requiredDataModel) {
    return "spdata=<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + " <shurjoPay>\n"
        + " <merchantName>"
        + requiredDataModel.getMerchantName()
        + "</merchantName>\n"
        + " <merchantPass>"
        + requiredDataModel.getMerchantPass()
        + "</merchantPass>\n"
        + " <userIP>"
        + requiredDataModel.getUserIP()
        + "</userIP>\n"
        + " <uniqID>"
        + requiredDataModel.getUniqID()
        + "</uniqID>\n"
        + " <totalAmount>"
        + requiredDataModel.getTotalAmount()
        + "</totalAmount>\n"
        + " <paymentOption>"
        + requiredDataModel.getPaymentOption()
        + "</paymentOption>\n"
        + " <returnURL>"
        + requiredDataModel.getReturnURL()
        + "</returnURL>\n"
        + " </shurjoPay>";
  }

  private void showWebsite(String html) {

  }

  private void showProgress(String message) {
    progressDialog.setMessage(message);
    progressDialog.setCancelable(false);
    progressDialog.show();
  }

  private void hideProgress() {
    progressDialog.dismiss();
  }
}