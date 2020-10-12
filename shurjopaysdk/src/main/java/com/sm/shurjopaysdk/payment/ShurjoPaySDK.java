package com.sm.shurjopaysdk.payment;

//        _                     _        ___  ___        _     _      _       _      _        _
//       | |                   (_)       |  \/  |       | |   | |    (_)     | |    | |      | |
//   ___ | |__   _   _  _ __    _   ___  | .  . | _   _ | | __| |__   _      | |    | |_   __| |
//  / __|| '_ \ | | | || '__|  | | / _ \ | |\/| || | | || |/ /| '_ \ | |     | |    | __| / _` |
//  \__ \| | | || |_| || |     | || (_) || |  | || |_| ||   < | | | || |     | |____| |_ | (_| | _
//  |___/|_| |_| \__,_||_|     | | \___/ \_|  |_/ \__,_||_|\_\|_| |_||_|     \_____/ \__| \__,_|(_)
//                            _/ |
//

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import com.sm.shurjopaysdk.listener.PaymentResultListener;
import com.sm.shurjopaysdk.model.RequiredDataModel;
import com.sm.shurjopaysdk.utils.PermissionsManager;
import com.sm.shurjopaysdk.utils.SPayConstants;
import com.sm.shurjopaysdk.utils.SPayNetworkManager;

public class ShurjoPaySDK {
  @SuppressLint("StaticFieldLeak")
  private static ShurjoPaySDK mInstance = new ShurjoPaySDK();
  public static PaymentResultListener listener = null;

  private ShurjoPaySDK() { }

  /**
   * Get a singleton instance for payment
   *
   * @return an singleton instance
   */
  public static synchronized ShurjoPaySDK getInstance() {
    if (mInstance == null) {
      mInstance = new ShurjoPaySDK();
    }
    return mInstance;
  }

  public void makePayment(Activity activity, RequiredDataModel dataModel, PaymentResultListener resultListener) {
    if (activity == null) {
      listener.onFailed(SPayConstants.Exception.USER_INPUT_ERROR);
      return;
    }

    // Check minimum and maximum amount check
    if (dataModel.getTotalAmount() < 0) {
      listener.onFailed(SPayConstants.Exception.INVALID_AMOUNT);
      return;
    }

    // Network state permission check
    if (!PermissionsManager.doesUserHaveNetworkStatePermission(activity)) {
      listener.onFailed(SPayConstants.Exception.NO_NETWORK_STATE_PERMISSION);
      return;
    }

    // Internet permission check
    if (!PermissionsManager.doesUserHaveInternetPermission(activity)) {
      listener.onFailed(SPayConstants.Exception.NO_INTERNET_PERMISSION);
      return;
    }

    // check is internet is available
    if (!SPayNetworkManager.IsInternetAvailable(activity)) {
      listener.onFailed(SPayConstants.Exception.NO_INTERNET_MESSAGE);
      return;
    }

    ShurjoPaySDK.listener = resultListener;

    Intent intent = new Intent(activity, PaymentActivity.class);
    intent.putExtra("data", dataModel);
    activity.startActivity(intent);
  }
}
