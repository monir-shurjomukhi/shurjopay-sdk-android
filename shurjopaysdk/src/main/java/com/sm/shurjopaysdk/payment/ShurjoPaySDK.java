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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sm.shurjopaysdk.listener.SMAppServiceListener;
import com.sm.shurjopaysdk.model.SMRequestModel;
import com.sm.shurjopaysdk.utils.SMAppConfig;
import com.sm.shurjopaysdk.utils.SMConstants;
import com.sm.shurjopaysdk.utils.SMNetworkManager;
import com.sm.shurjopaysdk.utils.SMPermissionCheck;
import com.sm.shurjopaysdk.utils.SMUtilities;
import com.sm.shurjopaysdk.utils.SMValidator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ShurjoPaySDK {
  @SuppressLint("StaticFieldLeak")
  private static ShurjoPaySDK mInstance = null;

  private Activity activity = null;
  private SMAppServiceListener listener = null;
  private AlertDialog alert;
  private ProgressDialog progressDialog;

  // necessary field need for transaction
  private double amount = 0;

  /**
   * Constructor of Shurjo Pay SDK
   *
   * @param activity to set
   */
  private ShurjoPaySDK(Activity activity) {
    this.activity = activity;
  }

  private ShurjoPaySDK() {
  }

  /**
   * Single Tone instance for payment
   *
   * @param activity for using on which class
   * @return an single tone instance
   */
  public static synchronized ShurjoPaySDK getInstance(Activity activity) {
    try {
      if (activity != null && mInstance == null) {
        mInstance = new ShurjoPaySDK(activity);
      }
    } catch (Exception e) {
      mInstance = new ShurjoPaySDK();
    }
    return mInstance;
  }

  /**
   * @return yyyy-MM-dd HH:mm:ss formate date as string
   */
  private static String getCurrentTimeStamp() {
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
      return dateFormat.format(new Date());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String postDataFromMap(SMRequestModel smRequestModel) {
    return "spdata=<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + " <shurjoPay>\n"
        + " <merchantName>"
        + smRequestModel.getMerchantName()
        + "</merchantName>\n"
        + " <merchantPass>"
        + smRequestModel.getMerchantPass()
        + "</merchantPass>\n"
        + " <userIP>"
        + smRequestModel.getUserIP()
        + "</userIP>\n"
        + " <uniqID>"
        + smRequestModel.getUniqID()
        + "</uniqID>\n"
        + " <totalAmount>"
        + smRequestModel.getTotalAmount()
        + "</totalAmount>\n"
        + " <paymentOption>"
        + smRequestModel.getPaymentOption()
        + "</paymentOption>\n"
        + " <returnURL>"
        + smRequestModel.getReturnURL()
        + "</returnURL>\n"
        + " </shurjoPay>";
  }

  /**
   * Set listener for Success or Failed reason
   *
   * @param listener to check
   * @return an single tone instance
   */
  public ShurjoPaySDK setListener(SMAppServiceListener listener) {
    if (listener != null) {
      this.listener = listener;
    }
    if (mInstance == null) {
      return null;
    }
    return mInstance;
  }

  /**
   * Set amount to transaction
   *
   * @param amount to set
   * @return an single tone instance
   */
  public ShurjoPaySDK setAmount(double amount) {
    this.amount = amount;
    if (!SMValidator.isValidAmount(amount)) {
      if (listener != null) {
        listener.onFailed(SMConstants.Exception.INVALID_AMOUNT);
      }
    }
    return mInstance;
  }

  /**
   * Payment method start if all required fields is given
   *
   * @return payment start
   */
  public ShurjoPaySDK payment() {
    // start progress dialog
    if (activity != null) {
      progressDialog = new ProgressDialog(activity);
      progressDialog.setCancelable(false);
      progressDialog.show();
    }

    // Check for listener is set
    if (listener == null) {
      hideProgress();
      return mInstance;
    }

    // Check minimum and maximum amount check
    if (amount < 0) {
      listener.onFailed(SMConstants.Exception.INVALID_AMOUNT);
      hideProgress();
      return mInstance;
    }

    // Network state permission check
    if (!SMPermissionCheck.doesUserHaveNetworkStatePermission(activity)) {
      listener.onFailed(SMConstants.Exception.NO_NETWORK_STATE_PERMISSION);
      hideProgress();
      return mInstance;
    }

    // Internet permission check
    if (!SMPermissionCheck.doesUserHaveInternetPermission(activity)) {
      listener.onFailed(SMConstants.Exception.NO_INTERNET_PERMISSION);
      hideProgress();
      return mInstance;
    }

    // check is internet is available
    if (!SMNetworkManager.IsInternetAvailable(activity)) {
      listener.onFailed(SMConstants.Exception.NO_INTERNET_MESSAGE);
      hideProgress();
      return mInstance;
    }

    // After all checked properly, finally going to Confirmation Dialog
    showConfirmationDialog();

    return mInstance;
  }

  /**
   * Bank List Request
   */
  private void showConfirmationDialog() {
    if (alert != null) {
      alert.dismiss();
    }
    AlertDialog.Builder builder;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      builder =
          new AlertDialog.Builder(activity, android.R.style.Theme_Material_Light_Dialog_Alert);
    } else {
      builder = new AlertDialog.Builder(activity);
    }

    builder.setView(confirmationDialogLayout());
    builder.setTitle(SMConstants.PAYMENT_TITLE);
    builder.setPositiveButton(SMConstants.OK, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        // Going to Bank Page
        bankTransactionView();
      }
    });
    builder.setNegativeButton(SMConstants.CANCEL, null);
    builder.setCancelable(false);
    builder.create();
    alert = builder.show();
    hideProgress();
  }

  /**
   * Hide progress dialog
   */
  private void hideProgress() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.hide();
    }
  }

  /**
   * Set Bank List layout
   *
   * @return linear layout
   */
  private LinearLayout confirmationDialogLayout() {
    //  Main Layout
    LinearLayout layout = new LinearLayout(activity);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setPadding(16, 16, 16, 16);

    LinearLayout.LayoutParams textInfoParams =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    TextView textInfo = new TextView(activity);
    textInfo.setPadding(16, 16, 16, 16);
    textInfo.setTextSize(16);
    textInfo.setLayoutParams(textInfoParams);
    String showText = "Amount: " + amount;
    textInfo.setText(showText);

    layout.addView(textInfo);
    return layout;
  }

  /**
   * Bank Transaction View
   */
  private void bankTransactionView() {
    final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    dialog.setContentView(bankPageViewDialog(dialog));
    dialog.setCancelable(false);
    dialog.show();
    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
      @Override
      public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
          cancelDialog(dialog);
        }
        return false;
      }
    });
  }

  /**
   * Bank Page view dialog layout
   *
   * @return linear layout
   */
  private LinearLayout bankPageViewDialog(Dialog dialog) {
    LinearLayout layout = new LinearLayout(activity);
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
    layout.setLayoutParams(layoutParams);
    ProgressBar progressBar =
        new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
    progressBar.setVisibility(View.VISIBLE);
    LinearLayout.LayoutParams barParams =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    progressBar.setLayoutParams(barParams);
    WebView webView = new WebView(activity);
    LinearLayout.LayoutParams webParams =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
    layout.setBackgroundColor(Color.WHITE);
    webView.setLayoutParams(webParams);
    layout.addView(progressBar);
    layout.addView(webView);
    supportView(webView, progressBar, dialog);

    return layout;
  }

  /**
   * Support view for web view
   *
   * @param webView to set
   * @param bar     for loading
   */
  @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
  private void supportView(
      WebView webView, final ProgressBar bar, Dialog dialog) {
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setAppCacheEnabled(true);
    webView.addJavascriptInterface(new SMWebAppInterface(dialog, listener), "Android");
    webView.clearCache(true);
    settings.setSaveFormData(false);
    settings.setAllowFileAccess(true);
    settings.setLoadWithOverviewMode(true);
    settings.setBuiltInZoomControls(false);
    settings.getJavaScriptCanOpenWindowsAutomatically();
    webView.setWebViewClient(new SMWebClient(bar));
    webView.loadUrl(loadBankPageUrl());

    SMRequestModel smRequestModel = new SMRequestModel();
    smRequestModel.setMerchantName("spaytest");
    smRequestModel.setMerchantPass("pass1234");
    smRequestModel.setPaymentOption("bkash");
    smRequestModel.setReturnURL("www.google.com");
    smRequestModel.setUniqID("NOK" + getCurrentTimeStamp());
    smRequestModel.setUserIP("192.168.11.22");
    smRequestModel.setTotalAmount(String.valueOf(amount));

    webView.postUrl(SMAppConfig.BANK_PAGE_LINK, postDataFromMap(smRequestModel).getBytes());

    webView.setWebChromeClient(new SMWebViewClient(bar));
  }

  /**
   * Get Bank Page Url
   *
   * @return bank page url
   */
  private String loadBankPageUrl() {
    Map<String, String> param_map = new HashMap<>();
    //param_map.put("amount", String.format(Locale.getDefault(), "%.2f", amount));

    param_map.put("merchantName", "test");
    param_map.put("merchantPass", "pass1234");
    param_map.put("userIP", "192.168.11.22");
    param_map.put("uniqID", "NOK" + getCurrentTimeStamp());
    param_map.put("totalAmount", String.format(Locale.getDefault(), "%.2f", amount));
    param_map.put("paymentOption", "");
    param_map.put("returnURL", "https://www.google.com/");

    return SMUtilities.buildURI(SMAppConfig.BANK_PAGE_LINK, param_map);
  }

  /**
   * Cancel Payment dialog
   *
   * @param d to show
   */
  private void cancelDialog(final Dialog d) {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setTitle(SMConstants.EXIT);
    builder.setMessage(SMConstants.CANCEL_PAYMENT);
    builder.setCancelable(false);
    builder.setPositiveButton(SMConstants.CONTINUE, null);
    builder.setNegativeButton(SMConstants.EXIT, null);

    final AlertDialog dialog1 = builder.create();
    dialog1.show();
    dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog1.dismiss();
      }
    });
    dialog1.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        d.dismiss();
        dialog1.dismiss();
      }
    });
  }

  /**
   * on Destroy called
   */
  public void onDestroy() {
    mInstance = null;
  }
}
