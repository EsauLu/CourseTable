package com.fatcat.coursetable.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.fatcat.coursetable.R;
import com.fatcat.coursetable.jw.bean.Course;
import com.fatcat.coursetable.jw.bean.CourseTable;
import com.fatcat.coursetable.uitls.DateUtils;
import com.fatcat.coursetable.uitls.PrefUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * 窗口小部件
 *
 * Created by EsauLu on 2016-10-09.
 */

public class CourseWidgetProvider extends AppWidgetProvider {

    public static final String UPDTE_WIDGET_PRE_WEEK_DAY="com.jluzh.jw.updatepreweekday";
    public static final String UPDTE_WIDGET_NEXT_WEEK_DAY="com.jluzh.jw.updatenextweekday";

    private CourseTable mCourseTable;

    private int mCurrWeek;

    private static int mWeekDay=-1;

    public static ArrayList<Course> mDayCourse=new ArrayList<>();

    public CourseWidgetProvider() {
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /**
     * 更新课表
     */
    private void updateCourse(Context context){

        String courseString = PrefUtils.getCourseInfo(context, "");
        if (courseString != null && !courseString.equals("")) {
            Gson gson = new Gson();
            mCourseTable = gson.fromJson(courseString, CourseTable.class);//获取课表对象
        }

    }

    /**
     * 更新星期
     */
    private void updateWeekDay(Context context){
        long currTime=new Date().getTime();
        long beginTime=PrefUtils.getBeginTime(context,currTime);
        mCurrWeek = DateUtils.countCurrWeek(beginTime,currTime);//周数
        if(mWeekDay==-1){
            mWeekDay=DateUtils.countCurrWeekDay(beginTime,currTime);//星期几，0代表星期日
        }
        Log.i("********>>>>********","week day : "+mWeekDay);
    }

    private String getWeekDayStr(int day){
        String str="";
        switch (day){
            case 0:
                str="星期日";
            break;
            case 1:
                str="星期一";
                break;
            case 2:
                str="星期二";
                break;
            case 3:
                str="星期三";
                break;
            case 4:
                str="星期四";
                break;
            case 5:
                str="星期五";
                break;
            case 6:
                str="星期六";
                break;
        }
        Log.i("++++++++++++++","++++++++++++++++"+str+"+++++++++++++");
        return str;
    }

    private void changeDay(Context context, Intent intent){
        updateCourseList(context);

//        int id = intent.getIntExtra("id", AppWidgetManager.INVALID_APPWIDGET_ID);
        AppWidgetManager manager=AppWidgetManager.getInstance(context);
        int[] ids=manager.getAppWidgetIds(new ComponentName(context.getPackageName(),CourseWidgetProvider.class.getName()));
        for(int id:ids){
            RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.widget_list_layout);
            remoteViews.setTextViewText(R.id.tv_widget_week_day,getWeekDayStr(mWeekDay));
            remoteViews.setTextViewText(R.id.tv_widget_week_num,"第"+mCurrWeek+"周");
            manager.updateAppWidget(id,remoteViews);
            manager.notifyAppWidgetViewDataChanged(id,R.id.lv_widget_course_list);
            Log.i("++++idididid++++++","******************"+id+"+++++++++++++");
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("++++++++++++++","+++++++---------广播----------++++++");
        Log.i("++++++++++++++","+++++++---------广播----------++++++");
        Log.i("++++++++++++++","+++++++---------广播----------++++++");
        switch (intent.getAction()){
            case UPDTE_WIDGET_PRE_WEEK_DAY:{
                Log.i("++++++++++++++",mWeekDay+"+++++++++++++");
                Log.i("++++++++++++++",mWeekDay+"+++++++++++++");
                mWeekDay=(mWeekDay+7-1)%7;
                changeDay(context,intent);
                break;
            }
            case UPDTE_WIDGET_NEXT_WEEK_DAY:{
                Log.i("++++++++++++++",mWeekDay+"+++++++++++++");
                Log.i("++++++++++++++",mWeekDay+"+++++++++++++");
                mWeekDay=(mWeekDay+1)%7;
                changeDay(context,intent);
                break;
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        updateCourseList(context);
        Log.i("####onUpdate#####","#######################################################################################################"+appWidgetIds.length+"#######################");
        for(int id:appWidgetIds){
            updateWidgetView(context,appWidgetManager,id);
            Log.i("####onUpdate#####","#######################"+id+"#######################");
        }
        Log.i("####onUpdate#####","#######################"+mWeekDay+"#######################");

    }

    private void updateWidgetView(Context context, AppWidgetManager appWidgetManager, int widgetId){

        RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget_list_layout);

        remoteViews.setTextViewText(R.id.tv_widget_week_num,"第"+mCurrWeek+"周");

        remoteViews.setTextViewText(R.id.tv_widget_week_day,getWeekDayStr(mWeekDay));

        Intent preBtnItent=new Intent();
        preBtnItent.setAction(UPDTE_WIDGET_PRE_WEEK_DAY);
        PendingIntent priPi=PendingIntent.getBroadcast(context,0,preBtnItent,0);
        remoteViews.setOnClickPendingIntent(R.id.ib_pre_day,priPi);

        Intent nextBtnItent=new Intent();
        nextBtnItent.setAction(UPDTE_WIDGET_NEXT_WEEK_DAY);
        PendingIntent nextPi=PendingIntent.getBroadcast(context,0,nextBtnItent,0);
        remoteViews.setOnClickPendingIntent(R.id.ib_next_day,nextPi);

        Intent intent=new Intent(context,CourseWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        remoteViews.setRemoteAdapter(R.id.lv_widget_course_list,intent);

        appWidgetManager.updateAppWidget(widgetId,remoteViews);

        Log.i("#########"+widgetId,"#######################"+mWeekDay+"#######################");

    }

    private void updateCourseList(Context context){

        updateWeekDay(context);
        updateCourse(context);
        int weekState = mCurrWeek%2==0 ? Course.DOUBLE_WEEK : Course.SINGLE_WEEK;
        mDayCourse.clear();
        for(Course c:mCourseTable.getCourses()){
            if(c.getDay()==mWeekDay){
                int cState=c.getWeekState();
                if(c.getStartWeek()<=mCurrWeek&&c.getEndWeek()>=mCurrWeek){
                    if(cState==Course.ALL_WEEK||cState==weekState){
                        mDayCourse.add(c);
                    }
                }
            }
        }
    }

}



























