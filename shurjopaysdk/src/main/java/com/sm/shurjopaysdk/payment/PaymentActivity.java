package com.sm.shurjopaysdk.payment;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.sm.shurjopaysdk.R;
import com.sm.shurjopaysdk.model.RequiredDataModel;

public class PaymentActivity extends AppCompatActivity {
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
  }

  private void getSpData() {

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