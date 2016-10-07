package com.fatcat.coursetable.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.fatcat.coursetable.R;
import com.fatcat.coursetable.base.BaseActivity;
import com.fatcat.coursetable.jw.bean.Course;

/**
 * Created by FatCat on 2016/10/3.
 */
public class CourseInfoActivity extends BaseActivity {

    private Toolbar mToolbar;
    Course course;

    private TextView courseName;
    private TextView courseRoom;
    private TextView courseTeacher;
    private TextView courseNum;
    private TextView courseWeek;

    @Override
    public void initView() {
        setContentView(R.layout.activity_courseinfo);

        Intent intent = this.getIntent();
        course = (Course) intent.getSerializableExtra("course");

        Log.i("传递的course对象", course.toString());

        courseName = (TextView) findViewById(R.id.course_name);
        courseRoom = (TextView) findViewById(R.id.course_room);
        courseTeacher = (TextView) findViewById(R.id.course_teacher);
        courseNum = (TextView) findViewById(R.id.course_num);
        courseWeek = (TextView) findViewById(R.id.course_week);

    }

    @Override
    protected void initActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setTitle(course.getName());// 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.colorBlue));
        // toolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {


        courseName.setText(course.getName());
        courseRoom.setText(course.getClassRoom());
        courseTeacher.setText(course.getTeacher());
        courseNum.setText(course.getClassTime());
        courseWeek.setText(course.getWeekNum());

    }

    @Override
    protected void initListener() {

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
}
