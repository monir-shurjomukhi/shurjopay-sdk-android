package com.sm.shurjopaysdk2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sm.shurjopaysdk.listener.PaymentResultListener;
import com.sm.shurjopaysdk.model.RequiredDataModel;
import com.sm.shurjopaysdk.model.TransactionInfo;
import com.sm.shurjopaysdk.payment.ShurjoPaySDK;
import com.sm.shurjopaysdk.utils.SPayConstants;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private static final String TAG = MainActivity.class.getSimpleName();
  private EditText amount;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    amount = (EditText) findViewById(R.id.input_amount);
    amount.setText("1");

    Button btnPayment = (Button) findViewById(R.id.btnPayment);
    btnPayment.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    RequiredDataModel requiredDataModel = new RequiredDataModel(
        "allexambd",
        "bnzofEyOCfBW",
        "AEB" + new Random().nextInt(1000000),
        Double.parseDouble(amount.getText().toString()),
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkiOiJhbGxleGFtYmQiLCJpYXQiOjE2MDM5ODg4NDB9.mfHdsOh-RWp10DSdgkF22tx1XJtEye4CHOcYIlC0rKY"
    );

    ShurjoPaySDK.getInstance().makePayment(
        this,
        SPayConstants.SdkType.LIVE,
        requiredDataModel,
        new PaymentResultListener() {
      @Override
      public void onSuccess(TransactionInfo transactionInfo) {
        Log.d(TAG, "onSuccess: transactionInfo = " + transactionInfo);
        Toast.makeText(MainActivity.this, "onSuccess: transactionInfo = " +
            transactionInfo, Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onFailed(String message) {
        Log.d(TAG, "onFailed: message = " + message);
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
      }
    });
  }
}
