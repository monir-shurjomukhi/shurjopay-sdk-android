package com.sm.shurjopaysdk.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

//        _                     _        ___  ___        _     _      _       _      _        _
//       | |                   (_)       |  \/  |       | |   | |    (_)     | |    | |      | |
//   ___ | |__   _   _  _ __    _   ___  | .  . | _   _ | | __| |__   _      | |    | |_   __| |
//  / __|| '_ \ | | | || '__|  | | / _ \ | |\/| || | | || |/ /| '_ \ | |     | |    | __| / _` |
//  \__ \| | | || |_| || |     | || (_) || |  | || |_| ||   < | | | || |     | |____| |_ | (_| | _
//  |___/|_| |_| \__,_||_|     | | \___/ \_|  |_/ \__,_||_|\_\|_| |_||_|     \_____/ \__| \__,_|(_)
//                            _/ |
//

public class PermissionsManager {
  /**
   * Does User has internet permission
   *
   * @param context - the context
   * @return true if user has internet permission
   */
  static public boolean doesUserHaveInternetPermission(Context context) {
    return hasPermissions(context, Manifest.permission.INTERNET);
  }

  /**
   * Does User has Network state permission
   *
   * @param context - the context
   * @return true if user has network state permission
   */
  static public boolean doesUserHaveNetworkStatePermission(Context context) {
    return hasPermissions(context, Manifest.permission.ACCESS_NETWORK_STATE);
  }

  /**
   * Determines if the context calling has the required permission
   *
   * @param context    - the IPC context
   * @param permission - The permissions to check
   * @return true if the IPC has the granted permission
   */
  private static boolean hasPermission(Context context, String permission) {
    int res = context.checkCallingOrSelfPermission(permission);
    return res == PackageManager.PERMISSION_GRANTED;
  }

  /**
   * Determines if the context calling has the required permissions
   *
   * @param context     - the IPC context
   * @param permissions - The permissions to check
   * @return true if the IPC has the granted permission
   */
  private static boolean hasPermissions(Context context, String... permissions) {

    boolean hasAllPermissions = true;

    for (String permission : permissions) {
      //return false instead of assigning, but with this you can log all permission values
      if (!hasPermission(context, permission)) {
        hasAllPermissions = false;
      }
    }
    return hasAllPermissions;
  }
}
