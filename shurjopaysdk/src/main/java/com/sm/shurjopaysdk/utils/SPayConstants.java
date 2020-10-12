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
  public static final String BASE_URL_TEST = "https://shurjotest.com/";
  public static final String BASE_URL_IPN = "http://ipn.shurjotest.com/";
  public static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkiOiJzcGF5dGVzdCIsImlhdCI6MTU5ODM2MTI1Nn0.cwkvdTDI6_K430xq7Iqapaknbqjm9J3Th1EiXePIEcY";
  public static final String TEST = "test";
  public static final String LIVE = "live";
  public static final String IPN = "ipn";

  /**
   * Constant field for Exceptions
   */
  public class Exception {
    public static final String USER_INPUT_ERROR = "User input error!";
    public static final String UNKNOWN_ERROR = "Payment declined from gateway!";
    public static final String BANK_TRANSACTION_FAILED = "Bank transaction failed!";
    public static final String NO_INTERNET_PERMISSION = "No internet permission is given!";
    public static final String NO_NETWORK_STATE_PERMISSION = "No network state permission is given!";
    public static final String NO_INTERNET_MESSAGE =
        "No internet available now. Please check your internet connection.";
    public static final String INVALID_AMOUNT = "Invalid amount!";
  }
}
