package com.fatcat.coursetable.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.View;

import com.fatcat.coursetable.R;
import com.fatcat.coursetable.jw.service.BroadcastAction;
import com.fatcat.coursetable.uitls.DateUtils;
import com.fatcat.coursetable.uitls.PrefUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by EsauLu on 2016-10-06.
 */

public class SettingActivity extends PreferenceActivity {

    private Preference myCourse;
    private Preference currWeek;
    private Preference qureyScore;
    private int currWeekNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        myCourse=findPreference("my_course");
        currWeek=findPreference("curr_week");
        qureyScore=findPreference("qurey_score");
        updateCurrWeek();
        setOnclicListener();
    }
    /**
     * 为各个选项处理点击事件
     */
    private void setOnclicListener(){
        myCourse.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });
        currWeek.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String[] num=new String[25];
                for(int i=0;i<25;i++){
                    num[i]="第"+String.valueOf(i+1)+"周";
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("选择当前周");
                builder.setItems(num, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which+1==currWeekNum){
                            return;
                        }
                        PrefUtils.setBeginTime(SettingActivity.this,"begintime", DateUtils.countBeginTime(Calendar.getInstance(),which+1));
                        updateCurrWeek();
                        Intent intent=new Intent();
                        intent.setAction(BroadcastAction.CHANG_CURR_WEEK_NUM);
                        SettingActivity.this.sendBroadcast(intent);
                    }
                });
                builder.create().show();
                return true;
            }
        });
        qureyScore.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });
    }

    public void Back(View v){
        super.onBackPressed();
    }

    public void updateCurrWeek(){
        long currTime=new Date().getTime();
        long beginTime=PrefUtils.getBeginTime(this,"begintime",currTime);
        currWeekNum = DateUtils.countCurrWeek(beginTime,currTime);
        currWeek.setSummary("现在是第"+currWeekNum+"周");
    }

}

























