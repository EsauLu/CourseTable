package com.fatcat.coursetable.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;

import com.fatcat.coursetable.R;
import com.fatcat.coursetable.jw.bean.Course;
import com.fatcat.coursetable.jw.bean.CourseTable;
import com.fatcat.coursetable.jw.service.BroadcastAction;
import com.fatcat.coursetable.uitls.DateUtils;
import com.fatcat.coursetable.uitls.PrefUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 窗口小部件
 *
 * Created by EsauLu on 2016-10-09.
 */

public class CourseWidgetProvider extends AppWidgetProvider {

    private CourseTable mCourseTable;

    private int mCurrWeek;

    private int mWeekDay;
    private String mWeekDayStr;

    public CourseWidgetProvider() {

    }

//    /**
//     * 更新周数
//     */
//    private void updateCurrWeek(){
//        long currTime=new Date().getTime();
//        long beginTime=PrefUtils.getBeginTime(this,currTime);
//        mCurrWeek = DateUtils.countCurrWeek(beginTime,currTime);
////        if(mCurrWeek>25){
////            mSelectWeek=1;
////        }else{
////            mSelectWeek =mCurrWeek;
////        }
//    }

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
        mWeekDay=DateUtils.countCurrWeekDay(beginTime,currTime);//星期几，0代表星期日
        switch (mWeekDay){
            case 0:
                mWeekDayStr="星期日";
                break;
            case 1:
                mWeekDayStr="星期一";
                break;
            case 2:
                mWeekDayStr="星期二";
                break;
            case 3:
                mWeekDayStr="星期三";
                break;
            case 4:
                mWeekDayStr="星期四";
                break;
            case 5:
                mWeekDayStr="星期五";
                break;
            case 6:
                mWeekDayStr="星期六";
                break;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        switch (intent.getAction()){
            case BroadcastAction.UPDATE_CURR_WEEK_NUM:{
                updateWeekDay(context);
                Intent it=new Intent();
                it.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                context.sendBroadcast(it);
                break;
            }
            case BroadcastAction.UPDTE_COURSE:{
                updateCourse(context);
                Intent it=new Intent();
                it.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                context.sendBroadcast(it);
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


        updateCourse(context);
        updateWeekDay(context);
        ArrayList<Course> courses=getCourseList();

        for(int id:appWidgetIds){
            updateWidgetView(context,appWidgetManager,id,courses);
        }

    }

    private void updateWidgetView(Context context, AppWidgetManager appWidgetManager, int widgetId, ArrayList<Course> data){

        Log.i("","========================================================");
        Log.i("","=====================更新小部件=========================");
        Log.i("","========================================================");

        RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget_list_layout);

        remoteViews.setTextViewText(R.id.tv_widget_week_num,"第"+mCurrWeek+"周");
        remoteViews.setTextViewText(R.id.tv_widget_week_day,mWeekDayStr);

        Intent intent=new Intent(context,CourseWidgetService.class);
        intent.putExtra("list",data);
        remoteViews.setRemoteAdapter(R.id.lv_widget_course_list,intent);

        appWidgetManager.updateAppWidget(widgetId,remoteViews);
    }

    private ArrayList<Course> getCourseList(){

        ArrayList<Course> list=new ArrayList<>();
        int weekState = mCurrWeek%2==0 ? Course.DOUBLE_WEEK : Course.SINGLE_WEEK;

        for(Course c:mCourseTable.getCourses()){
            if(c.getDay()==mWeekDay){
                int cState=c.getWeekState();
                if(cState==Course.ALL_WEEK||cState==weekState){
                    list.add(c);
                }
            }
        }

        return list;

    }

}



























