package com.fatcat.coursetable.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fatcat.coursetable.R;
import com.fatcat.coursetable.base.BaseActivity;
import com.fatcat.coursetable.jw.bean.CourseTable;
import com.fatcat.coursetable.jw.bean.User;
import com.fatcat.coursetable.jw.constant.Constant;
import com.fatcat.coursetable.jw.factor.BeanFactor;
import com.fatcat.coursetable.jw.service.BroadcastAction;
import com.fatcat.coursetable.jw.service.DataService;
import com.fatcat.coursetable.uitls.DateUtils;
import com.fatcat.coursetable.uitls.PrefUtils;
import com.google.gson.Gson;

import java.util.Calendar;


/**
 * Created by FatCat on 2016/10/2.
 */
public class CourseLoginActivity extends BaseActivity {

    private Toolbar mToolbar;

    private EditText etCourseNumber;
    private EditText etCoursePassword;
    private Button btnCourseLogin;
    private EditText etCourseCheck;
    private ImageView ivCheckCode;
    private TextView mJwUrlView;


    private boolean isBinder = false;

    private String doQurey;

    private DataService.DataBinder myBinder;

    private MyBroadcastReceiver mReceiver;


    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (DataService.DataBinder) service;
            isBinder = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinder = false;
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x123: {//登陆成功
                    btnCourseLogin.setEnabled(false);
                    btnCourseLogin.setText(doQurey);
                    if (doQurey.equals("获取课表...")) {
                        myBinder.qureyCourseTable(null, null);
                    } else if (doQurey.equals("获取成绩...")) {
                        myBinder.qureyScore();
                    }
                    break;
                }
                case 0x124: {//获取验证码
                    Bundle bl = msg.getData();
                    byte[] data = bl.getByteArray(Constant.CHECK);
                    Bitmap bim = BitmapFactory.decodeByteArray(data, 0, data.length);
                    ivCheckCode.setImageBitmap(bim);
                    break;
                }
                case 0x125: {//登录失败
                    Bundle bl = msg.getData();
                    String error = bl.getString(Constant.LOGIN_FAIL);
                    myBinder.getCheckImg();//重新获取验证码
                    Toast.makeText(CourseLoginActivity.this, error, Toast.LENGTH_SHORT).show();
                    break;
                }
                case 0x126: {//获取课表
                    CourseTable courseTable = (CourseTable) msg.getData().getSerializable(Constant.COURSE_TABLE);
                    Gson gson = new Gson();

                    //保存课表
                    PrefUtils.setCourseInfo(CourseLoginActivity.this, gson.toJson(courseTable).toString());
                    //将当前周作为课表开始时间保存
                    PrefUtils.setBeginTime(CourseLoginActivity.this, DateUtils.countBeginTime(Calendar.getInstance(),1));

                    Intent intent=new Intent(BroadcastAction.UPDTE_COURSE);
                    CourseLoginActivity.this.sendBroadcast(intent);
                    CourseLoginActivity.this.finish();
                    break;
                }
            }
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_course_login);
        etCourseNumber = (EditText) findViewById(R.id.et_course_username);
        etCoursePassword = (EditText) findViewById(R.id.et_course_password);
        btnCourseLogin = (Button) findViewById(R.id.btn_course_login);
        etCourseCheck = (EditText) findViewById(R.id.et_course_check);
        ivCheckCode = (ImageView) findViewById(R.id.iv_checkCode);
        mJwUrlView=(TextView) findViewById(R.id.tv_jw_url);
        Intent intent = getIntent();
        doQurey = intent.getStringExtra("qurey");
        if (doQurey == null) {
            doQurey = "获取课表...";
        }

    }

    @Override
    protected void initActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setTitle("登录");// 标题的文字需在setSupportActionBar之前，不然会无效
        // toolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {

        mReceiver = new MyBroadcastReceiver();

        String jwUrl=PrefUtils.getJwUrl(this,"");
        if(jwUrl.equals("")){
            jwUrl=Constant.BASE_URL;
            PrefUtils.setJwUrl(this,jwUrl);
            Log.i("首次没有读取到教务网地址","==============="+jwUrl);
        }
        mJwUrlView.setText("教务网地址:"+jwUrl);

        Log.i("首次读取教务网地址","==============="+jwUrl);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.LOGIN_SUCCESS);
        intentFilter.addAction(BroadcastAction.LOGIN_FAIL);
        intentFilter.addAction(BroadcastAction.CHECK_IMG);
        intentFilter.addAction(BroadcastAction.COURSE_TABLE);
        registerReceiver(mReceiver, intentFilter);

        Intent it = new Intent(this, DataService.class);
        it.putExtra(Constant.JW_URL,jwUrl);
        bindService(it, sc, Context.BIND_AUTO_CREATE);

    }

    public void loginClick(View view){
        switch (view.getId()){
            case R.id.btn_course_login:{
                login();
                break;
            }
            case R.id.iv_checkCode:{
                getCode();
                break;
            }
            case R.id.tv_jw_url:{
                setJwUrl();
                break;
            }
        }
    }

    private void login() {

        String courseName = etCourseNumber.getText().toString().trim();
        String coursePassword = etCoursePassword.getText().toString();
        String checkCode = etCourseCheck.getText().toString().trim();
        User user = BeanFactor.createUser(courseName, coursePassword, checkCode);
        myBinder.login(user);

    }

    private void getCode() {

        myBinder.getCheckImg();
    }

    private void setJwUrl(){
        LinearLayout layout=(LinearLayout) this.getLayoutInflater().inflate(R.layout.set_jw_url_dialog_layout,null);
        final EditText et = (EditText) layout.findViewById(R.id.et_jwurl);
        new AlertDialog.Builder(this).setTitle("修改教务网地址")
                .setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        Toast.makeText(CourseLoginActivity.this,input,Toast.LENGTH_LONG).show();
                        String url=dealURL(input );
                        PrefUtils.setJwUrl(CourseLoginActivity.this,url);//保存到本地
                        mJwUrlView.setText("教务网地址:"+url);//显示修改后的地址
                        myBinder.setURL(url);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private String dealURL(String url){
        if(url.matches("http://(.*)/")){
            return url;
        }
        String u=url;
        if(!u.endsWith("/")){
            u+="/";
        }
        if(!u.startsWith("http://")){
            u="http://"+u;
        }
        return u;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBinder) {
            unbindService(sc);
        }
        unregisterReceiver(mReceiver);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Message msg = new Message();
            Bundle bdl = intent.getExtras();
            msg.setData(bdl);
            switch (action) {
                case BroadcastAction.LOGIN_SUCCESS: {
                    msg.what = 0x123;
                    break;
                }
                case BroadcastAction.CHECK_IMG: {
                    msg.what = 0x124;
                    break;
                }
                case BroadcastAction.LOGIN_FAIL: {
                    msg.what = 0x125;
                    break;
                }
                case BroadcastAction.COURSE_TABLE: {
                    msg.what = 0x126;
                    break;
                }
            }
            mHandler.sendMessage(msg);
        }
    }

}
