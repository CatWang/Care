package com.orangex.care.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.orangex.care.common.PreferenceKey.PREF_NAME;

/**
 * Created by orangex on 2017/2/22.
 */

public class PreferencesManager {


    private static volatile PreferencesManager sInstance;
    private final SharedPreferences mPref;


    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    public static PreferencesManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PreferencesManager.class) {
                if (sInstance == null) {
                    sInstance = new PreferencesManager(context);
                }
            }
        }
        return sInstance;
    }

    public void setBoolean(String key, boolean value) {

        mPref.edit().putBoolean(key, value).apply();

    }

    public boolean getBoolean(String key,boolean def) {
        return mPref.getBoolean(key, def);
    }

}
