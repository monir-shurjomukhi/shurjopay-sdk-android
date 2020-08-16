package com.sm.shurjopaysdk.payment;

import android.app.Dialog;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import com.sm.shurjopaysdk.listener.SMAppServiceListener;
import com.sm.shurjopaysdk.model.SMResponseModel;
import java.util.HashMap;
import java.util.Map;

//        _                     _        ___  ___        _     _      _       _      _        _
//       | |                   (_)       |  \/  |       | |   | |    (_)     | |    | |      | |
//   ___ | |__   _   _  _ __    _   ___  | .  . | _   _ | | __| |__   _      | |    | |_   __| |
//  / __|| '_ \ | | | || '__|  | | / _ \ | |\/| || | | || |/ /| '_ \ | |     | |    | __| / _` |
//  \__ \| | | || |_| || |     | || (_) || |  | || |_| ||   < | | | || |     | |____| |_ | (_| | _
//  |___/|_| |_| \__,_||_|     | | \___/ \_|  |_/ \__,_||_|\_\|_| |_||_|     \_____/ \__| \__,_|(_)
//                            _/ |
//

public class SMWebAppInterface {
  private Dialog dialog;
  private SMAppServiceListener listener;

  /**
   * Instantiate the interface and set the context
   */
  SMWebAppInterface(Dialog dialog, SMAppServiceListener listener) {
    this.dialog = dialog;
    this.listener = listener;
  }

  /**
   * Show a toast from the web page when Success
   */
  @JavascriptInterface public void onSuccess(String response) {
    if (!TextUtils.isEmpty(response)) {
      if (dialog != null) {
        dialog.dismiss();
      }
      listener.onSuccess(decodeResponse(response));
    }
  }

  /**
   * Show a toast from the web page when Failed
   */
  @JavascriptInterface public void onFailed(String failed_message) {
    if (!TextUtils.isEmpty(failed_message)) {
      if (dialog != null) {
        dialog.dismiss();
      }
      listener.onFailed(failed_message);
    }
  }

  /**
   * Response from server is decoded for merchant to show
   *
   * @param response to decode
   * @return decoded response
   */
  private SMResponseModel decodeResponse(String response) {
    SMResponseModel responseModel = new SMResponseModel();

    Map<String, String> responseMap = new HashMap<>();
    String[] parts = response.split("&");
    for (String part : parts) {
      String[] c = part.split(":");
      for (int j = 0; j < c.length - 1; j++) {
        try {
          responseMap.put(c[0], c[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
          responseMap.put(c[0], "");
        }
      }
    }

    responseModel.setSuccess(responseMap.get("success").equalsIgnoreCase("TRUE"));
    responseModel.setBank_tx_id(responseMap.get("bank_tx_id"));
    responseModel.setBank_status(responseMap.get("bank_status"));
    responseModel.setAmount(responseMap.get("amount"));
    responseModel.setMethod(responseMap.get("method"));
    responseModel.setCard_holder(responseMap.get("card_holder"));
    responseModel.setCard_number(responseMap.get("card_number"));
    responseModel.setSpay_id(responseMap.get("spay_id"));
    responseModel.setTc_status(responseMap.get("tc_status"));
    responseModel.setErr_msg(responseMap.get("err_msg"));
    responseModel.setTc_amount(responseMap.get("tc_amount"));
    return responseModel;
  }
}
