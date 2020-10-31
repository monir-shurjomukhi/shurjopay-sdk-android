package com.sm.shurjopaysdk.payment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
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
  private RequiredDataModel requiredDataModel;
  private String sdkType;
  private String html;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment);

    // add back arrow to toolbar
    if (getSupportActionBar() != null){
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    webView = findViewById(R.id.webView);
    progressBar = findViewById(R.id.progressBar);
    progressDialog = new ProgressDialog(this);
    requiredDataModel = (RequiredDataModel) getIntent().getSerializableExtra(SPayConstants.DATA);
    Log.d(TAG, "onCreate: requiredDataModel = " + requiredDataModel);
    sdkType = getIntent().getStringExtra(SPayConstants.SDK_TYPE);

    getHtmlForm();
  }

  private void getHtmlForm() {
    if (requiredDataModel == null) {
      ShurjoPaySDK.listener.onFailed("User Input Error!");
      finish();
    }

    showProgress("Please Wait...");
    ApiClient.getInstance(sdkType).getApi().getHtmlForm(getSpDataFromModel(requiredDataModel))
        .enqueue(new Callback<String>() {
          @Override
          public void onResponse(Call<String> call, Response<String> response) {
            Log.d(TAG, "onResponse: " + response.body());
            hideProgress();
            try {
              if (response.isSuccessful()) {
                html = response.body();
                showWebsite(html);
              } else {
                ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_DECLINED);
                finish();
              }
            } catch (Exception e) {
              Log.e(TAG, "onResponse: ", e);
              ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_DECLINED);
              finish();
            }
          }

          @Override
          public void onFailure(Call<String> call, Throwable t) {
            hideProgress();
            Log.e(TAG, "onFailure: ", t);
            ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_DECLINED);
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
    final String[] previousUrl = {""};
    final String[] currentUrl = {""};
    webView.getSettings().setLoadsImagesAutomatically(true);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    webView.loadData(html, "text/html; charset=utf-8", "UTF-8");
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d(TAG, "shouldOverrideUrlLoading: url = " + url);

        previousUrl[0] = currentUrl[0];
        currentUrl[0] = url;

        Log.d(TAG, "shouldOverrideUrlLoading: previousUrl[0] = " + previousUrl[0]);
        Log.d(TAG, "shouldOverrideUrlLoading: currentUrl[0] = " + currentUrl[0]);

        if (currentUrl[0].contains("return_url.php")) {
          //ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_CANCELLED_BY_USER);
          //finish();
          if (previousUrl[0].contains("cancel=ok")) {
            ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_CANCELLED);
            finish();
          } else {
            getTransactionInfo();
          }
        } else {
          view.loadUrl(url);
        }
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

  /*@Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    // Check if the key event was the Back button and if there's history
    if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
      webView.clearHistory();
      showWebsite(html);
      return true;
    }
    // If it wasn't the Back key or there's no web page history, bubble up to the default
    // system behavior (probably exit the activity)
    return super.onKeyDown(keyCode, event);
  }*/

  private void getTransactionInfo() {
    showProgress("Please Wait...");
    ApiClient.getInstance(sdkType.equalsIgnoreCase(SPayConstants.SdkType.TEST) ?
        SPayConstants.IPN_TEST : SPayConstants.IPN_LIVE)
        .getApi().getTransactionInfo(requiredDataModel.getToken(), requiredDataModel.getUniqID())
        .enqueue(new Callback<TransactionInfo>() {
          @Override
          public void onResponse(Call<TransactionInfo> call, Response<TransactionInfo> response) {
            Log.d(TAG, "onResponse: response.body() = " + response.body());
            hideProgress();
            try {
              if (response.isSuccessful()) {
                TransactionInfo transactionInfo = response.body();
                if (transactionInfo == null) {
                  ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_DECLINED);
                  return;
                }

                    /*if (transactionInfo.getSpCode() == null) {
                      if (transactionInfo.getMethod() != null) {
                        showProgress("Please Wait...");
                        new Handler().postDelayed(new Runnable() {
                          @Override
                          public void run() {
                            //tryAgain();
                          }
                        }, 60000);
                        return;
                      } else {
                        ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.BANK_TRANSACTION_FAILED);
                      }
                    }*/

                if (TextUtils.isEmpty(transactionInfo.getSpCode())) {
                  ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_DECLINED);
                  return;
                }

                if (transactionInfo.getSpCode().equalsIgnoreCase("000")) {
                  ShurjoPaySDK.listener.onSuccess(response.body());
                } else {
                  ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.BANK_TRANSACTION_FAILED);
                }
              } else {
                ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_DECLINED);
              }
            } catch (Exception e) {
              Log.e(TAG, "onResponse: ", e);
              ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_DECLINED);
            } finally {
              finish();
            }
          }

          @Override
          public void onFailure(Call<TransactionInfo> call, Throwable t) {
            try {
              Log.e(TAG, "onFailure: ", t);
              hideProgress();
            } catch (Exception e) {
              Log.e(TAG, "onFailure: ", e);
            } finally {
              ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_DECLINED);
              finish();
            }
          }
        });
  }

  private void tryAgain() {
    ApiClient.getInstance(sdkType.equalsIgnoreCase(SPayConstants.SdkType.TEST) ?
        SPayConstants.IPN_TEST : SPayConstants.IPN_LIVE)
        .getApi().getTransactionInfo(requiredDataModel.getToken(),
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
                  ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_DECLINED);
                  return;
                }

                if (TextUtils.isEmpty(transactionInfo.getSpCode())) {
                  ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_CANCELLED);
                  return;
                }

                if (transactionInfo.getSpCode().equalsIgnoreCase("000")) {
                  ShurjoPaySDK.listener.onSuccess(response.body());
                } else {
                  ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.BANK_TRANSACTION_FAILED);
                }
              } else {
                ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_DECLINED);
              }
            } catch (Exception e) {
              Log.e(TAG, "onResponse: ", e);
              ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_DECLINED);
            }
            finish();
          }

          @Override
          public void onFailure(Call<TransactionInfo> call, Throwable t) {
            try {
              Log.e(TAG, "onFailure: ", t);
              hideProgress();
            } catch (Exception e) {
              Log.e(TAG, "onFailure: ", e);
            } finally {
              ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_DECLINED);
              finish();
            }
          }
        });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // handle arrow click here
    if (item.getItemId() == android.R.id.home) {
      //ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_CANCELLED);
      finish(); // close this activity and return to preview activity (if there is any)
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    ShurjoPaySDK.listener.onFailed(SPayConstants.Exception.PAYMENT_CANCELLED);
    super.onBackPressed();
  }
}
