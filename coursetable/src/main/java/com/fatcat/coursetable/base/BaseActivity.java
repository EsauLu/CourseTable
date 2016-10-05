package com.fatcat.coursetable.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * 抽取BaseActivity   管理所有activity 方便退出
 *
 * @author fatcat
 */
public abstract class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initActionBar();
        initData();
        initListener();
        initFragment();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public abstract void initView();

    protected void initActionBar() {
    }

    protected void initData() {

    }

    protected void initListener() {
    }

    protected void initFragment() {
    }


}
