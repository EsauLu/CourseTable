package com.fatcat.coursetable.uitls;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference封装
 *
 * @author fatcat
 */
public class PrefUtils {

    public static final String PREF_NAME = "config";

    public static void setCourseInfo(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String  getCourseInfo(Context ctx, String key, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return  sp.getString(key,defaultValue);
    }
//    public static boolean  getCourseBoolean(Context ctx, String key, boolean defaultValue) {
//        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
//                Context.MODE_PRIVATE);
//        return  sp.getBoolean(key,defaultValue);
//    }
//
//    public static void setCourseBoolean(Context ctx, String key, boolean value) {
//        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
//                Context.MODE_PRIVATE);
//        sp.edit().putBoolean(key, value).commit();
//    }

}
