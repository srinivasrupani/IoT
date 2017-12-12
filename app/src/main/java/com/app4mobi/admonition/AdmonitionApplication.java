package com.app4mobi.admonition;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Srinivas Rupani on 3/3/2017.
 */

public class AdmonitionApplication extends Application {
    private static String TAG = "App Related";
    private static AdmonitionApplication instance;

    public AdmonitionApplication() {
        instance = this;
    }

    public static AdmonitionApplication getInstance() {
        if (instance == null) {
            instance = new AdmonitionApplication();
        }
        return instance;
    }

    public static boolean isManifestPermissionGranted(Context context, String PERMISSION, int PERMISSION_CODE) {
        boolean isPermission = true;
        if (ContextCompat.checkSelfPermission(context, PERMISSION)
            == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission is granted");
            isPermission = true;
        } else {
            Log.v(TAG, "Permission is revoked");
            ActivityCompat.requestPermissions(((Activity) context), new String[]{PERMISSION}, PERMISSION_CODE);
            isPermission = false;
        }
        return isPermission;
    }
}
