package com.sm.shurjopaysdk.payment;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

//        _                     _        ___  ___        _     _      _       _      _        _
//       | |                   (_)       |  \/  |       | |   | |    (_)     | |    | |      | |
//   ___ | |__   _   _  _ __    _   ___  | .  . | _   _ | | __| |__   _      | |    | |_   __| |
//  / __|| '_ \ | | | || '__|  | | / _ \ | |\/| || | | || |/ /| '_ \ | |     | |    | __| / _` |
//  \__ \| | | || |_| || |     | || (_) || |  | || |_| ||   < | | | || |     | |____| |_ | (_| | _
//  |___/|_| |_| \__,_||_|     | | \___/ \_|  |_/ \__,_||_|\_\|_| |_||_|     \_____/ \__| \__,_|(_)
//                            _/ |
//

public class SMWebViewClient extends WebChromeClient {
  private ProgressBar bar;

  SMWebViewClient(ProgressBar bar) {
    this.bar = bar;
  }

  /**
   * On progress changed
   *
   * @param view        to change
   * @param newProgress to show
   */
  @Override
  public void onProgressChanged(WebView view, int newProgress) {
    super.onProgressChanged(view, newProgress);
    bar.setProgress(newProgress);
  }
}
