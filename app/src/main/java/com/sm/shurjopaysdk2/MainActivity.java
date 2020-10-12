package com.sm.shurjopaysdk2;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sm.shurjopaysdk.listener.PaymentResultListener;
import com.sm.shurjopaysdk.model.RequiredDataModel;
import com.sm.shurjopaysdk.model.TransactionInfo;
import com.sm.shurjopaysdk.payment.ShurjoPaySDK;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity
    implements PaymentResultListener, View.OnClickListener {
  private static final String TAG = MainActivity.class.getSimpleName();
  private EditText amount;
  private ShurjoPaySDK paySDK;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    amount = (EditText) findViewById(R.id.input_amount);

    Button bt_payment = (Button) findViewById(R.id.bt_payment);
    bt_payment.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
/*    paySDK = ShurjoPaySDK.getInstance(MainActivity.this)
        .setListener(MainActivity.this) // make sure this declared first
        .setAmount(Double.valueOf(amount.getText().toString()))
        .payment();*/

    RequiredDataModel requiredDataModel = new RequiredDataModel();
    requiredDataModel.setMerchantName("spaytest");
    requiredDataModel.setMerchantPass("JehPNXF58rXs");
    requiredDataModel.setUniqID("NOK" + new Random().nextInt(1000000));
    requiredDataModel.setTotalAmount(10);

    ShurjoPaySDK.getInstance(this).payment(this, requiredDataModel, new PaymentResultListener() {
      @Override
      public void onSuccess(TransactionInfo transactionInfo) {
        Log.d(TAG, "onSuccess: transactionInfo = " + transactionInfo);
      }

      @Override
      public void onFailed(String message) {
        Log.d(TAG, "onFailed: message = " + message);
      }
    });

    //post();
  }

  @Override
  public void onSuccess(TransactionInfo responseModel) {
    Log.d(TAG, "onSuccess: " + responseModel);
    new AlertDialog.Builder(MainActivity.this).setTitle("Payment Successful")
        .setMessage(responseModel.toString())
        .setCancelable(false)
        .setPositiveButton("OK", null)
        .show();
  }

  @Override
  public void onFailed(String message) {
    Log.e(TAG, "onFailed: " + message);
    new AlertDialog.Builder(MainActivity.this).setTitle("Payment Failed")
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton("OK", null)
        .show();
  }

  @Override
  protected void onDestroy() {
    paySDK.onDestroy();
    super.onDestroy();
  }

  private void post() {
    try {
      URL url = new URL("https://shurjotest.com/");
      HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("POST");
      conn.setDoInput(true);
      conn.setDoOutput(true);

      List<Pair> params = new ArrayList<>();
      params.add(new Pair("jsdata", "paramValue1"));

      OutputStream os = conn.getOutputStream();
      BufferedWriter writer = new BufferedWriter(
          new OutputStreamWriter(os, "UTF-8"));
      writer.write(getQuery(params));
      writer.flush();
      writer.close();
      os.close();

      conn.connect();
    } catch (Exception e) {

    }
  }

  private String getQuery(List<Pair> params) throws UnsupportedEncodingException {
    StringBuilder result = new StringBuilder();
    boolean first = true;

    for (Pair pair : params) {
      if (first)
        first = false;
      else
        result.append("&");

      result.append(URLEncoder.encode(pair.first.toString(), "UTF-8"));
      result.append("=");
      result.append(URLEncoder.encode(pair.first.toString(), "UTF-8"));
    }

    Log.d(TAG, "getQuery: result = " + result);

    return result.toString();
  }
}
