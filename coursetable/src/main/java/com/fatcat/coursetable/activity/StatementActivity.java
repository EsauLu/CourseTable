package com.fatcat.coursetable.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.fatcat.coursetable.R;
import com.fatcat.coursetable.base.BaseActivity;

/**
 * 声明界面
 * Created by FatCat on 2016/10/7.
 */
public class StatementActivity extends BaseActivity {

    private Toolbar mToolbar;

    @Override
    public void initView() {
        setContentView(R.layout.activity_statement);
    }

    @Override
    protected void initActionBar() {

        mToolbar = (Toolbar) findViewById(R.id.statement_toolbar);
        mToolbar.setTitle("声明");// 标题的文字需在setSupportActionBar之前，不然会无效
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

}
