package com.fatcat.coursetable.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.fatcat.coursetable.R;
import com.fatcat.coursetable.adapter.ScoreAdapter;
import com.fatcat.coursetable.base.BaseActivity;
import com.fatcat.coursetable.jw.bean.CourseScore;
import com.fatcat.coursetable.jw.bean.ScoreTable;
import com.fatcat.coursetable.jw.constant.Constant;

import java.util.ArrayList;

/**
 * 成绩表界面
 * Created by EsauLu on 2016-10-07.
 */

public class ScoreAtivity extends BaseActivity {

    private Toolbar mToolbar;
    private ListView mScoreList;
    private ScoreAdapter mScoreAdapter;
    private ArrayList<CourseScore> mCourseScores;
    private ScoreTable mScoreTable;

    @Override
    public void initView() {
        setContentView(R.layout.score_activity_layout);
        mScoreList=(ListView)findViewById(R.id.lv_score_list);
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent=getIntent();
        mScoreTable=(ScoreTable) intent.getSerializableExtra(Constant.SCORE_TABLE);
        mCourseScores=mScoreTable.getScoreList();
        if(mCourseScores==null){
            mCourseScores=new ArrayList<>();
        }

        mScoreAdapter=new ScoreAdapter(this,R.layout.score_item_layout,mCourseScores);
        mScoreList.setAdapter(mScoreAdapter);
    }


    @Override
    protected void initActionBar() {
        super.initActionBar();
        mToolbar = (Toolbar) findViewById(R.id.score_activity_toolbar);
        mToolbar.setTitle("查看成绩");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar); // toolbar.setSubtitle("副标题");
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorBlue));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}






































