package com.fatcat.coursetable.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.fatcat.coursetable.R;
import com.fatcat.coursetable.jw.bean.Course;

import java.util.ArrayList;

/**
 * 窗口小部件的ListView集合窗口更新服务
 * Created by EsauLu on 2016-10-10.
 */

public class CourseWidgetService extends RemoteViewsService {

//    public static

    public CourseWidgetService() {
        super();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CourseWidgetFactory(this,intent);
    }

    public class CourseWidgetFactory implements RemoteViewsService.RemoteViewsFactory{

        private Context mContext;

        private int mAppWidgetId;

        private ArrayList<Course> mCourseList;

        @SuppressWarnings("unchecked")
        public CourseWidgetFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            mCourseList=new ArrayList<>();
        }

        private void initData(){

            mCourseList.clear();
            mCourseList.addAll(CourseWidgetProvider.mDayCourse);
            Log.i("CourseWidgetFactory","=========================================================");
            for(Course c:mCourseList){
                Log.i("CourseWidgetFactory",""+c.getName());
            }
            Log.i("CourseWidgetFactory","=========================================================");

        }

        @Override
        public void onCreate() {

            initData();

        }

        @Override
        public void onDataSetChanged() {
            initData();
        }

        @Override
        public void onDestroy() {
            mCourseList.clear();
        }

        @Override
        public int getCount() {
            return mCourseList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            Course c=mCourseList.get(position);

            RemoteViews remoteViews=new RemoteViews(mContext.getPackageName(), R.layout.widget_course_list_item_layout);

            remoteViews.setTextViewText(R.id.tv_number_of_course,String.valueOf(c.getNumber())+"-"+String.valueOf(c.getNumber()+1));
            remoteViews.setTextViewText(R.id.tv_name_of_course,c.getName());
            remoteViews.setTextViewText(R.id.tv_classroom_of_course,c.getClassRoom());

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    public class WidgetUIBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

}
