package com.sm.shurjopaysdk.utils;

//        _                     _        ___  ___        _     _      _       _      _        _
//       | |                   (_)       |  \/  |       | |   | |    (_)     | |    | |      | |
//   ___ | |__   _   _  _ __    _   ___  | .  . | _   _ | | __| |__   _      | |    | |_   __| |
//  / __|| '_ \ | | | || '__|  | | / _ \ | |\/| || | | || |/ /| '_ \ | |     | |    | __| / _` |
//  \__ \| | | || |_| || |     | || (_) || |  | || |_| ||   < | | | || |     | |____| |_ | (_| | _
//  |___/|_| |_| \__,_||_|     | | \___/ \_|  |_/ \__,_||_|\_\|_| |_||_|     \_____/ \__| \__,_|(_)
//                            _/ |
//

public class SPayConstants {
  /**
   * Constants fields for Common
   */
  public static final CharSequence PAYMENT_TITLE = "Payment";
  public static final String EXIT = "Exit";
  public static final String OK = "Ok";
  public static final String CANCEL = "Cancel";
  public static final String CANCEL_PAYMENT = "Do u want to exit cancel Payment";
  public static final String CONTINUE = "Continue";

  /**
   * Constant field for Exceptions
   */
  public class Exception {
    public static final String NO_INTERNET_PERMISSION = "No internet permission is given";
    public static final String NO_NETWORK_STATE_PERMISSION = "No network state permission is given";
    public static final String NO_INTERNET_MESSAGE =
        "No internet available now. Please check your internet connection.";
    public static final String INVALID_AMOUNT = "Invalid amount";
  }
}
