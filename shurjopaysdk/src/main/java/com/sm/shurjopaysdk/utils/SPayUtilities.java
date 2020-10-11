package com.sm.shurjopaysdk.utils;

import android.net.Uri;

import java.util.Map;

//        _                     _        ___  ___        _     _      _       _      _        _
//       | |                   (_)       |  \/  |       | |   | |    (_)     | |    | |      | |
//   ___ | |__   _   _  _ __    _   ___  | .  . | _   _ | | __| |__   _      | |    | |_   __| |
//  / __|| '_ \ | | | || '__|  | | / _ \ | |\/| || | | || |/ /| '_ \ | |     | |    | __| / _` |
//  \__ \| | | || |_| || |     | || (_) || |  | || |_| ||   < | | | || |     | |____| |_ | (_| | _
//  |___/|_| |_| \__,_||_|     | | \___/ \_|  |_/ \__,_||_|\_\|_| |_||_|     \_____/ \__| \__,_|(_)
//                            _/ |
//

public class SPayUtilities {
  /**
   * Build URI
   *
   * @param url    main link
   * @param params to add
   * @return main uri with param
   */
  public static String buildURI(String url, Map<String, String> params) {

    // build url with parameters.
    Uri.Builder builder = Uri.parse(url).buildUpon();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      builder.appendQueryParameter(entry.getKey(), entry.getValue());
    }

    return builder.build().toString();
  }
}
