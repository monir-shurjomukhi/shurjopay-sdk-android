package com.sm.shurjopaysdk.utils;

import android.text.TextUtils;

//        _                     _        ___  ___        _     _      _       _      _        _
//       | |                   (_)       |  \/  |       | |   | |    (_)     | |    | |      | |
//   ___ | |__   _   _  _ __    _   ___  | .  . | _   _ | | __| |__   _      | |    | |_   __| |
//  / __|| '_ \ | | | || '__|  | | / _ \ | |\/| || | | || |/ /| '_ \ | |     | |    | __| / _` |
//  \__ \| | | || |_| || |     | || (_) || |  | || |_| ||   < | | | || |     | |____| |_ | (_| | _
//  |___/|_| |_| \__,_||_|     | | \___/ \_|  |_/ \__,_||_|\_\|_| |_||_|     \_____/ \__| \__,_|(_)
//                            _/ |
//

public class SMValidator {

  /**
   * Check if the given amount is valid
   *
   * @param amounts to check
   * @return is valid
   */
  public static boolean isValidAmount(double... amounts) {
    boolean isValid = false;
    for (double amount : amounts) {
      isValid = amount > 0;
      // if one is invalid then break with return false
      if (!isValid) {
        break;
      }
    }
    return isValid;
  }

  public static boolean isValid(String... text) {
    boolean isValid = false;
    for (String texts : text) {
      isValid = !isEmpty(texts);
      if (isValid) {
        break;
      }
    }
    return isValid;
  }

  private static boolean isEmpty(String text) {
    return TextUtils.isEmpty(text);
  }
}
