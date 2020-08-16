package com.sm.shurjopaysdk2;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sm.shurjopaysdk.listener.SMAppServiceListener;
import com.sm.shurjopaysdk.model.SMResponseModel;
import com.sm.shurjopaysdk.payment.ShurjoPaySDK;

public class MainActivity extends AppCompatActivity
    implements SMAppServiceListener, View.OnClickListener {
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
    paySDK = ShurjoPaySDK.getInstance(MainActivity.this)
        .setListener(MainActivity.this) // make sure this declared first
        .setAmount(Double.valueOf(amount.getText().toString()))
        .payment();
  }

  @Override
  public void onSuccess(SMResponseModel responseModel) {
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
}
