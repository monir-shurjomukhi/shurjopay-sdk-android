package com.sm.shurjopaysdk.payment;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

//        _                     _        ___  ___        _     _      _       _      _        _
//       | |                   (_)       |  \/  |       | |   | |    (_)     | |    | |      | |
//   ___ | |__   _   _  _ __    _   ___  | .  . | _   _ | | __| |__   _      | |    | |_   __| |
//  / __|| '_ \ | | | || '__|  | | / _ \ | |\/| || | | || |/ /| '_ \ | |     | |    | __| / _` |
//  \__ \| | | || |_| || |     | || (_) || |  | || |_| ||   < | | | || |     | |____| |_ | (_| | _
//  |___/|_| |_| \__,_||_|     | | \___/ \_|  |_/ \__,_||_|\_\|_| |_||_|     \_____/ \__| \__,_|(_)
//                            _/ |
//

public class SPayWebClient extends WebViewClient {
  private ProgressBar bar;

  SPayWebClient(ProgressBar bar) {
    this.bar = bar;
  }

  /**
   * On page start
   *
   * @param view    to change
   * @param url     to access
   * @param favicon bitmap
   */
  @Override
  public void onPageStarted(WebView view, String url, Bitmap favicon) {
    super.onPageStarted(view, url, favicon);
    bar.setVisibility(View.VISIBLE);
    bar.setProgress(0);
  }

  /**
   * On page load finished
   *
   * @param view to change
   * @param url  to access
   */
  @Override
  public void onPageFinished(WebView view, String url) {
    super.onPageFinished(view, url);
    view.clearCache(true);
    bar.setProgress(100);
    bar.setVisibility(View.GONE);
  }

  @Override
  public void onLoadResource(WebView view, String url) {
    super.onLoadResource(view, url);
  }

  @Override
  public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
    return true;
  }
}