package com.fatcat.coursetable.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;


/**
 * @author Administrator
 * @version $Rev: 8 $
 * @time 2015-7-15 上午10:50:49
 * @des 定义一个全局的盒子.里面放置的对象, 属性, 方法都是全局可以调用
 * @updateAuthor $Author: admin $
 * @updateDate $Date: 2015-07-15 17:06:45 +0800 (星期三, 15 七月 2015) $
 * @updateDes TODO
 */
public class BaseApplication extends Application {

    private static Context mContext;
    private static Thread mMainThread;
    private static long mMainTreadId;
    private static Looper mMainLooper;
    private static Handler mHandler;




    public static Handler getHandler() {
        return mHandler;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static long getMainTreadId() {
        return mMainTreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }


    @Override
    public void onCreate() {// 程序的入口
        // 初始化化一些.常用属性.然后放到盒子里面来
        // 上下文
        mContext = getApplicationContext();

        // 主线程
        mMainThread = Thread.currentThread();

        // 主线程Id
        mMainTreadId = android.os.Process.myTid();

        // tid thread
        // uid user
        // pid process
        // 主线程Looper对象
        mMainLooper = getMainLooper();

        // 定义一个handler

        mHandler = new Handler();


        super.onCreate();
    }

}
