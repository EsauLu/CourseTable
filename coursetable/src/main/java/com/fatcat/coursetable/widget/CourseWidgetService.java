package com.fatcat.coursetable.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.fatcat.coursetable.R;
import com.fatcat.coursetable.jw.bean.Course;
import com.fatcat.coursetable.jw.bean.CourseTable;
import com.fatcat.coursetable.uitls.PrefUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * 窗口小部件的ListView集合窗口更新服务
 * Created by EsauLu on 2016-10-10.
 */

public class CourseWidgetService extends RemoteViewsService {

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
            mCourseList=(ArrayList<Course>) intent.getSerializableExtra("list");
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {

            Course c=mCourseList.get(position);

            RemoteViews remoteViews=new RemoteViews(mContext.getPackageName(), R.layout.widget_course_list_itemlayout);

            remoteViews.setTextViewText(R.id.tv_name_of_course,String.valueOf(c.getNumber())+"-"+String.valueOf(c.getNumber()+1));
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
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

}
