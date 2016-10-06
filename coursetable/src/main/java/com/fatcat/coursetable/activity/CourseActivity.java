package com.fatcat.coursetable.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.fatcat.coursetable.R;
import com.fatcat.coursetable.adapter.WeekDayAdapter;
import com.fatcat.coursetable.base.BaseActivity;
import com.fatcat.coursetable.jw.bean.Course;
import com.fatcat.coursetable.jw.bean.CourseTable;
import com.fatcat.coursetable.jw.service.BroadcastAction;
import com.fatcat.coursetable.uitls.DateUtils;
import com.fatcat.coursetable.uitls.PrefUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by FatCat on 2016/10/3.
 */
public class CourseActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{

    /**
     * 课程表
     */
    private CourseTable mCourseTable;

    /**
     * 当前周
     */
    private int mCurrWeek;

    /**
     * 选中的周数
     */
    private int mSelectWeek;

    /**
     * 选择周数的下拉框
     */
    private Spinner mWeekDaySpinner;

    /**
     * 下拉框的适配器
     */
    private  WeekDayAdapter mAdapter;

    /**
     * 周数下拉框的数据
     */
    private ArrayList<String> mWeekArr;

    /**
     * 记录课程的map
     */
    private HashMap<String, Course> mCourseMap;

    /**
     * 记录课程背景颜色的map
     */
    private HashMap<String, Integer> mBgColorMap;

    private CourseBroadcast mCourseBroadcast;

    //课程页面的button引用，6行7列
    private int[][] lessons = {
            {R.id.lesson17, R.id.lesson11, R.id.lesson12, R.id.lesson13, R.id.lesson14, R.id.lesson15, R.id.lesson16},
            {R.id.lesson27, R.id.lesson21, R.id.lesson22, R.id.lesson23, R.id.lesson24, R.id.lesson25, R.id.lesson26},
            {R.id.lesson37, R.id.lesson31, R.id.lesson32, R.id.lesson33, R.id.lesson34, R.id.lesson35, R.id.lesson36},
            {R.id.lesson47, R.id.lesson41, R.id.lesson42, R.id.lesson43, R.id.lesson44, R.id.lesson45, R.id.lesson46},
            {R.id.lesson57, R.id.lesson51, R.id.lesson52, R.id.lesson53, R.id.lesson54, R.id.lesson55, R.id.lesson56},
            {R.id.lesson67, R.id.lesson61, R.id.lesson62, R.id.lesson63, R.id.lesson64, R.id.lesson65, R.id.lesson66}
    };
    //某节课的背景图,用于随机获取
    private int[] bg = {
            R.drawable.kb1, R.drawable.kb2, R.drawable.kb3, R.drawable.kb4, R.drawable.kb5,
            R.drawable.kb6, R.drawable.kb7, R.drawable.kb8, R.drawable.kb9, R.drawable.kb10,
            R.drawable.kb11, R.drawable.kb12, R.drawable.kb13, R.drawable.kb14, R.drawable.kb15,
            R.drawable.kb16, R.drawable.kb17, R.drawable.kb18, R.drawable.kb19, R.drawable.kb20,
            R.drawable.kb21, R.drawable.kb22, R.drawable.kb23, R.drawable.kb24, R.drawable.kb25
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x200:{
                    int weekNum = msg.getData().getInt("message");
                    if (mSelectWeek != weekNum&&mCourseTable!=null) {
                        mSelectWeek = weekNum;
                        rankCourse(weekNum);
                    }
                    break;
                }
                case 0x201:{
                    updateCurrWeek();
                    if(mCurrWeek<=25){
                        mWeekDaySpinner.setSelection(mCurrWeek-1,true);
                    }
                    mAdapter.notifyDataSetChanged();
                    rankCourse(mCurrWeek);
                    break;
                }
                case 0x202:{
                    if(updateCourse()){
                        mAdapter.notifyDataSetChanged();
                        rankCourse(mSelectWeek);
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_course);
        mWeekDaySpinner = (Spinner) findViewById(R.id.week_day_spinner);

        mCourseBroadcast=new CourseBroadcast();
        IntentFilter filter=new IntentFilter();
        filter.addAction(BroadcastAction.UPDATE_CURR_WEEK_NUM);
        filter.addAction(BroadcastAction.UPDTE_COURSE);
        registerReceiver(mCourseBroadcast,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mCourseBroadcast);
    }

    /**
     * 排列课程到课表
     */
    private void rankCourse(int weekNum) {
        int currWeekState = (weekNum % 2 == 0) ? Course.DOUBLE_WEEK : Course.SINGLE_WEEK;
        //排列课程名称
        for (final String key : mCourseMap.keySet()) {
            Course course = mCourseMap.get(key);
            int courseState = course.getWeekState();
            int x = course.getNumber();
            int y = course.getDay() % 7;
            Button btn = (Button) findViewById(lessons[x / 2][y]);
            String oldText = (String) btn.getText();
            String newText = key.split("\\*")[0];
            if (oldText.equals(newText)) continue;
            if (courseState == Course.ALL_WEEK || courseState == currWeekState || oldText == null || oldText.equals("")) {
                btn.setText(newText);
                btn.setTextColor(Color.WHITE);
                btn.setBackgroundResource(bg[6]);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Course c = mCourseMap.get(key);
                        if (c == null) {
                            return;
                        }
                        Intent intent = new Intent(CourseActivity.this, CourseInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("course", c);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

        }
        //填充颜色
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                Button btn = (Button) findViewById(lessons[i][j]);
                String key = btn.getText() + "*" + (2 * i + 1) + j;
                Course c = mCourseMap.get(key);
                if (c == null) continue;
                int state = c.getWeekState();
                if (weekNum >= c.getStartWeek() && weekNum <= c.getEndWeek()) {
                    if (state == Course.ALL_WEEK || state == currWeekState) {
                        btn.setTextColor(Color.WHITE);
                        btn.setBackgroundResource(mBgColorMap.get(c.getName()));
                        continue;
                    }
                }
                btn.setTextColor(Color.GRAY);
                btn.setBackgroundResource(R.drawable.unavailable);
            }
        }

    }

    /**
     * 更新周数
     */
    private void updateCurrWeek(){
        long currTime=new Date().getTime();
        long beginTime=PrefUtils.getBeginTime(this,"begintime",currTime);
        mCurrWeek = DateUtils.countCurrWeek(beginTime,currTime);
        if(mCurrWeek>25){
            mSelectWeek=1;
        }else{
            mSelectWeek =mCurrWeek;
        }
    }

    /**
     * 更新课表
     */
    private boolean updateCourse(){
        updateCurrWeek();
        String courseString = PrefUtils.getCourseInfo(CourseActivity.this, "courseinfo", "");
        if (courseString == null || courseString.equals("")) {
            return false;
        }
        updateCurrWeek();//更新当前周
        //读取已经保存的课表
        Gson gson = new Gson();
        mCourseTable = gson.fromJson(courseString, CourseTable.class);
        mCourseMap = new HashMap<>();
        mBgColorMap = new HashMap<>();
        int k = 0;
        for (Course c : mCourseTable.getCourses()) {
            String key = c.getName() + "@" + c.getClassRoom() + "*" + c.getNumber() + (c.getDay() % 7);
            mCourseMap.put(key, c);
            mBgColorMap.put(c.getName(), bg[k++]);
        }
        return true;
    }

    @Override
    protected void initActionBar() {

    }

    @Override
    protected void initData() {

        mCurrWeek=0;

        if(!updateCourse()){
            AlertDialog.Builder ab = new AlertDialog.Builder(CourseActivity.this)
                    .setTitle("提示：")
                    .setMessage("你还未添加课程表，现在添加？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(CourseActivity.this, CourseLoginActivity.class);
                            intent.putExtra("query", "获取课表...");
                            startActivity(intent);
                        }
                    });
            ab.create().show();
        }else{
            rankCourse(mSelectWeek);
        }

        mWeekArr = new ArrayList<>();
        String s = "第1周";
        for (int i = 25; i >0; i--) {
            String w = s.replaceAll("[\\d]+", String.valueOf(i));
            mWeekArr.add(0,w);
        }

        mAdapter = new WeekDayAdapter(this, R.layout.week_day_item_layout, mWeekArr);
        mAdapter.setDropDownViewResource(R.layout.week_day_drop_item_layout);
        mWeekDaySpinner.setAdapter(mAdapter);
        if(mCurrWeek<=25){
            mWeekDaySpinner.setSelection(mCurrWeek-1,true);
        }
        mWeekDaySpinner.setOnItemSelectedListener(this);

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_setting_title:{
                Intent intent=new Intent(this,SettingActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Message msg = new Message();
        msg.what = 0x200;
        Bundle bl = new Bundle();
        bl.putInt("message", position + 1);
        msg.setData(bl);
        mHandler.sendMessage(msg);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 获取当前周
     *
     * @return 当前周数
     */
    public int getCurrWeek() {
        return mCurrWeek;
    }

    /**
     * 获取当前学年
     *
     * @return 学年
     */
    public String getCurrXnd() {
        if (mCourseTable == null) {
            return "";
        }
        return mCourseTable.getCurrXnd();
    }

    /**
     * 获取当前学期
     *
     * @return 学期
     */
    public String getCurrXqd() {
        if (mCourseTable == null) {
            return "";
        }
        return mCourseTable.getCurrXqd();
    }

    class CourseBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg=new Message();
            switch (intent.getAction()){
                case BroadcastAction.UPDATE_CURR_WEEK_NUM:{
                    msg.what=0x201;
                    break;
                }
                case BroadcastAction.UPDTE_COURSE:{
                    msg.what=0x202;
                    break;
                }
            }
            mHandler.sendMessage(msg);
        }
    }

}



































