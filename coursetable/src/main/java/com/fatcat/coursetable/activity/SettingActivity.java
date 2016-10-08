package com.fatcat.coursetable.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fatcat.coursetable.R;
import com.fatcat.coursetable.base.BaseActivity;
import com.fatcat.coursetable.jw.service.BroadcastAction;
import com.fatcat.coursetable.uitls.DateUtils;
import com.fatcat.coursetable.uitls.PrefUtils;

import java.util.Calendar;
import java.util.Date;

public class SettingActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mCourseTableView;
    private TextView mCurrWeekView;
    private int mCurrWeekNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_setting);
        mCourseTableView = (TextView) findViewById(R.id.setting_mycourse_des);
        mCurrWeekView = (TextView) findViewById(R.id.setting_curr_week_des);
        updateCurrWeek();
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        mToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        mToolbar.setTitle("更多选项");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar); // toolbar.setSubtitle("副标题");
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorBlue));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void updateCurrWeek() {
        long currTime = new Date().getTime();
        long beginTime = PrefUtils.getBeginTime(this, currTime);
        mCurrWeekNum = DateUtils.countCurrWeek(beginTime, currTime);
        String s = "";
        if (mCurrWeekNum == 0) {
            s = "你还没添加过任何课表";
        } else if (mCurrWeekNum > 25) {
            s = "本学期已结束";
        } else {
            s = "现在是第" + mCurrWeekNum + "周";
        }
        mCurrWeekView.setText(s);
    }

    public void settingOnClick(View view) {

        switch (view.getId()) {
            case R.id.setting_mycourse: {
                clickMyCourse();
                break;
            }
            case R.id.setting_curr_week: {
                clickCurrWeekNum();
                break;
            }
            case R.id.setting_qurey_score: {
                qureyScore();
                break;
            }
            case R.id.setting_statement: {
                clickMyStatement();
                break;
            }
            case R.id.setting_version: {
                break;
            }
        }

    }

    private void clickMyStatement() {
        Intent intent = new Intent(SettingActivity.this, StatementActivity.class);
        startActivity(intent);
    }


    private void clickMyCourse() {
        Intent intent = new Intent(SettingActivity.this, CourseLoginActivity.class);
        intent.putExtra("qurey", "获取课表...");
        startActivity(intent);
        SettingActivity.this.finish();
    }

    public void qureyScore(){
        Intent intent = new Intent(SettingActivity.this, CourseLoginActivity.class);
        intent.putExtra("qurey", "获取成绩...");
        startActivity(intent);
        SettingActivity.this.finish();
    }

    private void clickCurrWeekNum() {
        if (mCurrWeekNum == 0) {
            return;
        }
        String[] num = new String[25];
        for (int i = 0; i < 25; i++) {
            num[i] = "第" + String.valueOf(i+1) + "周";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("选择当前周");
        builder.setItems(num, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which + 1 == mCurrWeekNum) {
                    return;
                }
                System.out.println("===============>>>>>>>"+mCurrWeekNum+" , "+(which+1));
                PrefUtils.setBeginTime(SettingActivity.this, DateUtils.countBeginTime(Calendar.getInstance(), which + 1));
                updateCurrWeek();
                Intent intent = new Intent();
                intent.setAction(BroadcastAction.UPDATE_CURR_WEEK_NUM);
                SettingActivity.this.sendBroadcast(intent);
            }
        });
        builder.create().show();
    }

}




































