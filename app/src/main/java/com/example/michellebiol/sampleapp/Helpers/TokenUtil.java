package com.example.michellebiol.sampleapp.Helpers;

import android.content.Context;

public class TokenUtil {
    private static final String PREFERENCES_FILE = "tokens";

    //getters
    public static String getTokenType(Context context)
    {
        SharedPreferenceHelper.PREF_FILE = PREFERENCES_FILE;
        return SharedPreferenceHelper.getSharedPreferenceString(context,"token_type",null);
    }

    public static String getToken(Context context)
    {
        SharedPreferenceHelper.PREF_FILE = PREFERENCES_FILE;
        return SharedPreferenceHelper.getSharedPreferenceString(context,"token",null);
    }

    public static String getUserId(Context context)
    {
        SharedPreferenceHelper.PREF_FILE = PREFERENCES_FILE;
        return SharedPreferenceHelper.getSharedPreferenceString(context,"user_id",null);
    }
    //end of getters


    //setters

    //end of setters
}
