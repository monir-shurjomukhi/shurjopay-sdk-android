package com.sm.shurjopaysdk.payment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.sm.shurjopaysdk.R;
import com.sm.shurjopaysdk.model.RequiredDataModel;
import com.sm.shurjopaysdk.model.TransactionInfo;
import com.sm.shurjopaysdk.networking.ApiClient;
import com.sm.shurjopaysdk.utils.SPayConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
  private static final String TAG = "PaymentActivity";
  private WebView webView;
  private ProgressBar progressBar;
  private ProgressDialog progressDialog;
  private FloatingActionButton fab;
  private RequiredDataModel requiredDataModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment);

    webView = findViewById(R.id.webView);
    progressBar = findViewById(R.id.progressBar);
    progressDialog = new ProgressDialog(this);
    requiredDataModel = (RequiredDataModel) getIntent().getSerializableExtra("data");
    Log.d(TAG, "onCreate: requiredDataModel = " + requiredDataModel);
    fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showProgress("Please Wait...");
        ApiClient.getInstance("ipn").getApi().getTransactionInfo(SPayConstants.TOKEN,
            requiredDataModel.getUniqID())
            .enqueue(new Callback<TransactionInfo>() {
              @Override
              public void onResponse(Call<TransactionInfo> call, Response<TransactionInfo> response) {
                Log.d(TAG, "onResponse: response.body() = " + response.body());
                hideProgress();
                try {
                  if (response.isSuccessful()) {
                    TransactionInfo transactionInfo = response.body();
                    if (transactionInfo == null) {
                      ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.UNKNOWN_ERROR);
                      return;
                    }

                    if (transactionInfo.getSpCode().equalsIgnoreCase("000")) {
                      ShurjoPaySDK.listener.onSuccess(response.body());
                    } else {
                      ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.BANK_TRANSACTION_FAILED);
                    }
                  } else {
                    ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.UNKNOWN_ERROR);
                  }
                } catch (Exception e) {
                  Log.e(TAG, "onResponse: ", e);
                  ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.UNKNOWN_ERROR);
                }
                finish();
              }

              @Override
              public void onFailure(Call<TransactionInfo> call, Throwable t) {
                try {
                  Log.e(TAG, "onFailure: ", t);
                  hideProgress();
                  ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.UNKNOWN_ERROR);
                  finish();
                } catch (Exception e) {
                  Log.e(TAG, "onFailure: ", e);
                  ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.UNKNOWN_ERROR);
                  finish();
                }
              }
            });
      }
    });

    getHtmlForm();
  }

  private void getHtmlForm() {
    if (requiredDataModel == null) {
      ShurjoPaySDK.listener.onFailed("User Input Error!");
      finish();
    }

    showProgress("Please Wait...");
    ApiClient.getInstance("test").getApi().getHtmlForm(getSpDataFromModel(requiredDataModel))
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
    Log.d(TAG, "getSpDataFromModel: ========= " + "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
        + "<shurjoPay>"
        + "<merchantName>"
        + requiredDataModel.getMerchantName()
        + "</merchantName>"
        + "<merchantPass>"
        + requiredDataModel.getMerchantPass()
        + "</merchantPass>"
        + "<userIP>"
        + "127.0.0.1"
        + "</userIP>"
        + "<uniqID>"
        + requiredDataModel.getUniqID()
        + "</uniqID>"
        + "<totalAmount>"
        + requiredDataModel.getTotalAmount()
        + "</totalAmount>"
        + "<paymentOption>"
        + "shurjopay"
        + "</paymentOption>"
        + "<returnURL>"
        + "https://sdk.com"
        + "</returnURL>"
        + "</shurjoPay>");

    return "<shurjoPay>"
        + "<merchantName>"
        + requiredDataModel.getMerchantName()
        + "</merchantName>"
        + "<merchantPass>"
        + requiredDataModel.getMerchantPass()
        + "</merchantPass>"
        + "<userIP>"
        + "127.0.0.1"
        + "</userIP>"
        + "<uniqID>"
        + requiredDataModel.getUniqID()
        + "</uniqID>"
        + "<totalAmount>"
        + requiredDataModel.getTotalAmount()
        + "</totalAmount>"
        + "<paymentOption>"
        + "shurjopay"
        + "</paymentOption>"
        + "<returnURL>"
        + "https://sdk.com"
        + "</returnURL>"
        + "</shurjoPay>";
  }


  private void showWebsite(String html) {
    webView.getSettings().setLoadsImagesAutomatically(true);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    webView.loadData(html, "text/html; charset=utf-8", "UTF-8");
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return false;
      }
    });
    webView.setWebChromeClient(new WebChromeClient() {
      @Override
      public void onProgressChanged(WebView view, int newProgress) {
        progressBar.setProgress(newProgress);
      }
    });
  }

  private void showProgress(String message) {
    progressDialog.setMessage(message);
    progressDialog.setCancelable(false);
    progressDialog.show();
  }

  private void hideProgress() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }
}
