package com.sm.shurjopaysdk2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    amount.setText(new Random().nextInt(100000) + "");

    Button bt_payment = (Button) findViewById(R.id.bt_payment);
    bt_payment.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    RequiredDataModel requiredDataModel = new RequiredDataModel(
        "spaytest",
        "JehPNXF58rXs",
        "NOK" + new Random().nextInt(1000000),
        Double.parseDouble(amount.getText().toString())
    );

    //requiredDataModel.setMerchantName("spaytest");
    //requiredDataModel.setMerchantPass("JehPNXF58rXs");
    //requiredDataModel.setUniqID("NOK" + new Random().nextInt(1000000));
    //requiredDataModel.setTotalAmount(Double.parseDouble(amount.getText().toString()));

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
