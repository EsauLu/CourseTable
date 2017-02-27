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

    public static void setXndInfo(Context ctx, String value){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString("courseXnds", value).commit();
    }

//    public static void setCourseInfo(Context ctx, String value) {
//        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
//                Context.MODE_PRIVATE);
//        sp.edit().putString("courseTable", value).commit();
//    }

    public static void setCourseInfo(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getXndInfo(Context ctx, String defaultValue){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return  sp.getString("courseXnds",defaultValue);
    }

//    public static String  getCourseInfo(Context ctx, String defaultValue) {
//        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
//                Context.MODE_PRIVATE);
//        return  sp.getString("courseTable",defaultValue);
//    }

    public static String  getCourseInfo(Context ctx, String key, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return  sp.getString(key,defaultValue);
    }

    public static  final void setBeginTime(Context ctx, long value){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putLong("begintime", value).commit();
    }

    public static final long getBeginTime(Context ctx, long defaultValue){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return  sp.getLong("begintime",defaultValue);
    }


    public static  final void setJwUrl(Context ctx, String value){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString("jwurl", value).commit();
    }

    public static final String getJwUrl(Context ctx, String defaultValue){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return  sp.getString("jwurl",defaultValue);
    }

    public static final void setCurrXnd(Context ctx, String value){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString("currXnd", value).commit();
    }

    public static final String getCurrXnd(Context ctx, String defaultValue){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return  sp.getString("currXnd",defaultValue);
    }

    public static final void setCurrXqd(Context ctx, String value){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString("currXqd", value).commit();
    }

    public static final String getCurrXqd(Context ctx, String defaultValue){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return  sp.getString("currXqd",defaultValue);
    }

}
