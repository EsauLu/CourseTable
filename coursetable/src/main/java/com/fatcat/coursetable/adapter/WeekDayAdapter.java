package com.fatcat.coursetable.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fatcat.coursetable.R;
import com.fatcat.coursetable.activity.CourseActivity;

/**
 * Created by EsauL on 2016-10-04.
 */

public class WeekDayAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;

    public WeekDayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout view;

        if(convertView == null){
            view = new LinearLayout(getContext());
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(resource, view, true);
        }
        else{
            view = (LinearLayout)convertView;
        }

        String item = getItem(position);
        TextView weekNum=(TextView) view.findViewById(R.id.tv_week_num);
        TextView xnd=(TextView) view.findViewById(R.id.tv_xnd);

        if(item.contains("本周")){
            item=item.substring(0,item.indexOf("(本周)"));
        }else{
            item=item+"(非本周)";
        }
        weekNum.setText(item);

        CourseActivity courseActivity=(CourseActivity)context;
        String s=courseActivity.getCurrXnd()+"学年 第"+courseActivity.getCurrXqd()+"学期";
        xnd.setText(s);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        LinearLayout view;
        if(convertView == null){
            view = new LinearLayout(getContext());
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(R.layout.week_day_drop_item_layout, view, true);
        }
        else{
            view = (LinearLayout)convertView;
        }

        String item = getItem(position);
        TextView weekNum=(TextView) view.findViewById(R.id.tv_week_item);
        if(item.contains("本周")){
            weekNum.setBackgroundColor(0xff2e94da);
            weekNum.setTextColor(Color.WHITE);
        }else{
            weekNum.setBackgroundColor(Color.WHITE);
            weekNum.setTextColor(0xff2e94da);
        }
        weekNum.setText(item);

        return view;
    }
}

























