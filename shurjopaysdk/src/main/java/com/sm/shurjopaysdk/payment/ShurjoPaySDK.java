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

import com.sm.shurjopaysdk.listener.PaymentResultListener;
import com.sm.shurjopaysdk.model.RequiredDataModel;
import com.sm.shurjopaysdk.utils.SPayAppConfig;
import com.sm.shurjopaysdk.utils.SPayConstants;
import com.sm.shurjopaysdk.utils.SPayNetworkManager;
import com.sm.shurjopaysdk.utils.PermissionsManager;
import com.sm.shurjopaysdk.utils.SPayUtilities;
import com.sm.shurjopaysdk.utils.SPayValidator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ShurjoPaySDK {
  @SuppressLint("StaticFieldLeak")
  private static ShurjoPaySDK mInstance = null;

  private Activity activity = null;
  public static PaymentResultListener listener = null;
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

  public static String postDataFromMap(RequiredDataModel requiredDataModel) {
    return "spdata=<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + " <shurjoPay>\n"
        + " <merchantName>"
        + requiredDataModel.getMerchantName()
        + "</merchantName>\n"
        + " <merchantPass>"
        + requiredDataModel.getMerchantPass()
        + "</merchantPass>\n"
        + " <userIP>"
        + requiredDataModel.getUserIP()
        + "</userIP>\n"
        + " <uniqID>"
        + requiredDataModel.getUniqID()
        + "</uniqID>\n"
        + " <totalAmount>"
        + requiredDataModel.getTotalAmount()
        + "</totalAmount>\n"
        + " <paymentOption>"
        + requiredDataModel.getPaymentOption()
        + "</paymentOption>\n"
        + " <returnURL>"
        + requiredDataModel.getReturnURL()
        + "</returnURL>\n"
        + " </shurjoPay>";
  }

  /**
   * Set listener for Success or Failed reason
   *
   * @param listener to check
   * @return an single tone instance
   */
  public ShurjoPaySDK setListener(PaymentResultListener listener) {
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
    if (!SPayValidator.isValidAmount(amount)) {
      if (listener != null) {
        listener.onFailed(SPayConstants.Exception.INVALID_AMOUNT);
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
      listener.onFailed(SPayConstants.Exception.INVALID_AMOUNT);
      hideProgress();
      return mInstance;
    }

    // Network state permission check
    if (!PermissionsManager.doesUserHaveNetworkStatePermission(activity)) {
      listener.onFailed(SPayConstants.Exception.NO_NETWORK_STATE_PERMISSION);
      hideProgress();
      return mInstance;
    }

    // Internet permission check
    if (!PermissionsManager.doesUserHaveInternetPermission(activity)) {
      listener.onFailed(SPayConstants.Exception.NO_INTERNET_PERMISSION);
      hideProgress();
      return mInstance;
    }

    // check is internet is available
    if (!SPayNetworkManager.IsInternetAvailable(activity)) {
      listener.onFailed(SPayConstants.Exception.NO_INTERNET_MESSAGE);
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
    builder.setTitle(SPayConstants.PAYMENT_TITLE);
    builder.setPositiveButton(SPayConstants.OK, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        // Going to Bank Page
        bankTransactionView();
      }
    });
    builder.setNegativeButton(SPayConstants.CANCEL, null);
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
    webView.addJavascriptInterface(new SPayWebAppInterface(dialog, listener), "Android");
    webView.clearCache(true);
    settings.setSaveFormData(false);
    settings.setAllowFileAccess(true);
    settings.setLoadWithOverviewMode(true);
    settings.setBuiltInZoomControls(false);
    settings.getJavaScriptCanOpenWindowsAutomatically();
    webView.setWebViewClient(new SPayWebClient(bar));
    webView.loadUrl(loadBankPageUrl());

    RequiredDataModel requiredDataModel = new RequiredDataModel();
    requiredDataModel.setMerchantName("spaytest");
    requiredDataModel.setMerchantPass("pass1234");
    requiredDataModel.setPaymentOption("bkash");
    requiredDataModel.setReturnURL("www.google.com");
    requiredDataModel.setUniqID("NOK" + getCurrentTimeStamp());
    requiredDataModel.setUserIP("192.168.11.22");
    requiredDataModel.setTotalAmount(String.valueOf(amount));

    webView.postUrl(SPayAppConfig.BANK_PAGE_LINK, postDataFromMap(requiredDataModel).getBytes());

    webView.setWebChromeClient(new SPayWebViewClient(bar));
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

    return SPayUtilities.buildURI(SPayAppConfig.BANK_PAGE_LINK, param_map);
  }

  /**
   * Cancel Payment dialog
   *
   * @param d to show
   */
  private void cancelDialog(final Dialog d) {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setTitle(SPayConstants.EXIT);
    builder.setMessage(SPayConstants.CANCEL_PAYMENT);
    builder.setCancelable(false);
    builder.setPositiveButton(SPayConstants.CONTINUE, null);
    builder.setNegativeButton(SPayConstants.EXIT, null);

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
