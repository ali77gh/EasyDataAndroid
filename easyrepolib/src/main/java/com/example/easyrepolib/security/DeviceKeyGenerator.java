package com.example.easyrepolib.security;

import android.content.Context;
import android.provider.Settings.Secure;

/**
 * Created by ali on 8/30/18.
 */

public class DeviceKeyGenerator {

    public static String Generate(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        //todo get device information and generate a 16 char key
    }

    //recommended
    public static String Generate(Context context, String secret) {

        String id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        return id.substring(secret.length()) + secret;
        //todo get device information and generate a 16 char key
    }
}
