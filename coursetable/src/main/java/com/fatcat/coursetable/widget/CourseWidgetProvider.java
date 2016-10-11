package com.fatcat.coursetable.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

/**
 * 窗口小部件
 *
 * Created by EsauLu on 2016-10-09.
 */

public class CourseWidgetProvider extends AppWidgetProvider {

    public static final int PRE_BUTTON=1;
    public static final int NEXT_BUTTON=2;
    public static final String UPDATE_ALL="com.jluzh.jw.update";

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

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("++++++onReceive++++++++","+++++++---------广播----------++++++");
        Log.i("++++++onReceive++++++++","+++++++---------广播----------++++++");
        Log.i("+++++++onReceive+++++++","+++++++---------广播----------++++++");
        Uri data=intent.getData();
        if(intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)){
            int id=Integer.parseInt(data.getSchemeSpecificPart());
            changeDay(context,id);
        }
        String ac=intent.getAction();
        if(ac!=null&&ac.equals(UPDATE_ALL))
        {
            Log.i("+++++++onReceive+++++++","+++++++---------更新----------++++++");
            Log.i("+++++++onReceive+++++++","+++++++---------更新----------++++++");
            Log.i("+++++++onReceive+++++++","+++++++---------更新----------++++++");
            changeDay(context,0);
        }
    }

    private void changeDay(Context context, int btn_id){
        updateWeekDay(context);
        updateCourse(context);
        switch (btn_id){
            case PRE_BUTTON:
                mWeekDay=(mWeekDay-1+7)%7;
                break;
            case NEXT_BUTTON:
                mWeekDay=(mWeekDay+1)%7;
                break;
        }
        updateCourseList(context);
        AppWidgetManager manager=AppWidgetManager.getInstance(context);
        int[] ids=manager.getAppWidgetIds(new ComponentName(context.getPackageName(),CourseWidgetProvider.class.getName()));
        for(int id:ids){
            RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.widget_list_layout);
            remoteViews.setTextViewText(R.id.tv_widget_week_day,getWeekDayStr(mWeekDay));
            remoteViews.setTextViewText(R.id.tv_widget_week_num,"第"+mCurrWeek+"周");
            manager.updateAppWidget(id,remoteViews);
            manager.notifyAppWidgetViewDataChanged(id,R.id.lv_widget_course_list);
            Log.i("++++changeDay++++++","******************"+id+"+++++++++++++");
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
        Log.i("********updateWeekDay*","week day : "+mWeekDay);
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
        Log.i("+++getWeekDayStr++++","++++++++++++++++"+str+"+++++++++++++");
        return str;
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        updateWidgetView(context,appWidgetManager);

    }

    private void updateWidgetView(Context context, AppWidgetManager appWidgetManager){

        updateWeekDay(context);
        updateCourse(context);
        updateCourseList(context);
        int[] ids=appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(),CourseWidgetProvider.class.getName()));
        for(int id:ids){

            RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget_list_layout);
            remoteViews.setTextViewText(R.id.tv_widget_week_num,"第"+mCurrWeek+"周");
            remoteViews.setTextViewText(R.id.tv_widget_week_day,getWeekDayStr(mWeekDay));
            remoteViews.setOnClickPendingIntent(R.id.ib_pre_day,getBtnPendingIntent(context,PRE_BUTTON));
            remoteViews.setOnClickPendingIntent(R.id.ib_next_day,getBtnPendingIntent(context,NEXT_BUTTON));

            Intent intent=new Intent(context,CourseWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            remoteViews.setRemoteAdapter(R.id.lv_widget_course_list,intent);

            appWidgetManager.updateAppWidget(id,remoteViews);

            Log.i("#updateWidgetView#####"+id,"#######################"+mWeekDay+"#######################");

        }

    }

    private PendingIntent getBtnPendingIntent(Context context, int buttonId){
        Intent intent=new Intent(context,CourseWidgetProvider.class);
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        intent.setData(Uri.parse("custom:"+buttonId));
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

    private void updateCourseList(Context context){
        int weekState = mCurrWeek%2==0 ? Course.DOUBLE_WEEK : Course.SINGLE_WEEK;
        mDayCourse.clear();
        for(Course c:mCourseTable.getCourses()){
            if(c.getDay()==mWeekDay){
                int cState=c.getWeekState();
                if(c.getStartWeek()<=mCurrWeek&&c.getEndWeek()>=mCurrWeek){
                    if(cState==Course.ALL_WEEK||cState==weekState){
                        mDayCourse.add(c);
                        Log.i("+updateCourseList+","++++++++++++++++++++++"+c.getName()+"+++++++++++++++++++++++");
                    }
                }
            }
        }
    }

}



























