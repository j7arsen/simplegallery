package com.j7arsen.simple.gallery.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by arsen on 11.01.2016.
 */
public class AppSettings {

    public static void saveIntValue(String key, int value, Context activity){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntValue(String key, Context activity){
        return PreferenceManager.getDefaultSharedPreferences(activity).getInt(key, 1);
    }

}
