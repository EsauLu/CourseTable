package com.fatcat.coursetable.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.fatcat.coursetable.R;
import com.fatcat.coursetable.base.BaseActivity;
import com.fatcat.coursetable.jw.bean.Course;

/**
 * Created by FatCat on 2016/10/3.
 */
public class CourseInfoActivity extends BaseActivity {

    private Toolbar mToolbar;
    Course course;

    @Override
    public void initView() {
        setContentView(R.layout.activity_courseinfo);

        Intent intent = this.getIntent();
        course=(Course)intent.getSerializableExtra("course");

        Log.i("传递的course对象",course.toString());
    }

    @Override
    protected void initActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setTitle("课程信息");// 标题的文字需在setSupportActionBar之前，不然会无效
        // toolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
