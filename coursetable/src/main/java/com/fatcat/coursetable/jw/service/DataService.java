package com.fatcat.coursetable.jw.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.fatcat.coursetable.jw.bean.Course;
import com.fatcat.coursetable.jw.bean.CourseTable;
import com.fatcat.coursetable.jw.bean.ScoreTable;
import com.fatcat.coursetable.jw.bean.StuSimpleInfo;
import com.fatcat.coursetable.jw.bean.User;
import com.fatcat.coursetable.jw.constant.Constant;
import com.fatcat.coursetable.jw.dao.OkHttpDAO;
import com.fatcat.coursetable.jw.tool.HtmlTools;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DataService extends Service implements OkHttpDAO{

    /**
     * Http客户端
     */
    private OkHttpClient mOkHttpClient;

    /**
     * 记录cookie
     */
//    private String mCookie;

    /**
     * 记录正方教务系统页面表单的__VIEWSTATE的值
     */
    private String mViewState;

    /**
     * 已登陆用户的信息
     */
    private User mUser;

    /**
     * 登陆错误信息
     */
    private String mErrorMessege;

    /**
     * 查询课程表信息的URL
     */
    private String mCourseURL;

    /**
     * 查询个人信息的URL
     */
    private String mPersonalInfoURL;

    /**
     * 查询成绩表的URL
     */
    private String mScorceURL;

    private  DataBinder myBinder=new DataBinder();

    public class DataBinder extends Binder{

        public void login(User user){
            DataService.this.login(user);
        }

        public void getCheckImg(){
            DataService.this.getCheckImg();
        }

        public void qureyCourseTable(String xnd, String xqd){
            DataService.this.getCourseTable(xnd,xqd);
        }

        public void qureyScore(){
            DataService.this.getScore();
        }

        public void qureyStuInfo(){
            DataService.this.getPersonalInfo();
        }

    }

    public DataService() {
        this.mViewState="";
        mOkHttpClient=new OkHttpClient.Builder().cookieJar(new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                String u=url.host();
                String urlStr=url.url().toString();
                if(urlStr.equals(Constant.BASE_URL)){
                    cookieStore.put(u, cookies);
                }
            }


            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        }).build();

        init();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return myBinder;
    }

    @Override
    public void init() {
        Log.i("init","********************* init *********************");
        String url= Constant.BASE_URL;
        Request request = new Request.Builder().url(url).build();
        Call call=mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mViewState=HtmlTools.findViewState(response.body().string());
                getCheckImg();
            }
        });
    }

    @Override
    public void sendGetRequest(String url, String ref) {

    }

    @Override
    public void sendPostRequest(String url, String ref) {

    }

    @Override
    public void getCheckImg() {
        Log.i("init","********************* getCheckImg *********************");
        Request request = new Request.Builder().url(Constant.CHECK_IMAGE_URL).build();
        Call call=mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body=response.body();
                byte[] data=body.bytes();
                Intent it=new Intent(BroadcastAction.CHECK_IMG);
                it.putExtra(Constant.CHECK,data);
                sendBroadcast(it);
            }
        });
    }

    @Override
    public void login(User user) {
        Log.i("login","********************* login *********************");
        FormBody body=new FormBody.Builder()
                .add("__VIEWSTATE", mViewState)
                .add("txtUserName", user.getName())
                .add("TextBox2", user.getPasswd())
                .add("txtSecretCode",user.getCheck())
                .add("RadioButtonList1", Constant.RADIO_BUTTON_LIST)
                .add("Button1","")
                .add("lbLanguage","")
                .add("hidPdrs","")
                .add("hidsc","")
                .build();
        Request request=new Request.Builder()
                .url(Constant.LOGIN_URL)
                .post(body)
                .build();
        mUser=user;//记录登陆的用户
        Call call=mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //检测是否有登陆错误的信息，有则记录信息，否则登陆成功
                ResponseBody body=response.body();
                String html=body.string();
                String  errorMessege="";
                String p="<script language='javascript' defer>alert\\('([\\s\\S]+?)'\\);document\\.getElementById\\('([\\s\\S]+?)'\\)\\.focus\\(\\);</script>";
                Pattern pattern=Pattern.compile(p);
                Matcher m=pattern.matcher(html);
                Intent it=new Intent();
                if(m.find()){
                    //登陆失败
                    errorMessege=m.group(1);
                    it.setAction(BroadcastAction.LOGIN_FAIL);
                    it.putExtra(Constant.LOGIN_FAIL,errorMessege);
                }else {
                    saveQueryURL(html);//保存查询链接
                    it.setAction(BroadcastAction.LOGIN_SUCCESS);
                    it.putExtra(Constant.LOGIN_SUCCESS,"登陆成功");
                }
                sendBroadcast(it);

            }
        });

    }

    @Override
    public void getCourseTable(final String xnd,final String xqd) {
        Log.i("getCourseTable","********************* getCourseTable *********************");
        String urlStr=Constant.BASE_URL+mCourseURL;//查询课表的URL
        String referer=Constant.STUDENT_URL+mUser.getName();//引用地址
        Log.i("getCourseTable","*********   "+urlStr+"   *********");
        Request.Builder builder=new Request.Builder().url(urlStr).addHeader("Referer",referer);
        if(xnd!=null&&xqd!=null){
            FormBody body=new FormBody.Builder()
                    .add("__EVENTTARGET", "xqd")
                    .add("__EVENTARGUMENT", "")
                    .add("__VIEWSTATE", mViewState)
                    .add("xnd", xnd)
                    .add("xqd", xqd)
                    .build();
            builder.post(body);
        }
        Request request=builder.build();
        Call call=mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String html=response.body().string();
                CourseTable courseTable=new CourseTable();
                mViewState=HtmlTools.findViewState(html);//记录__VIEWSTATE
                StuSimpleInfo stuInfo=HtmlTools.getCourseStuInfo(html);
                String[] xnds=HtmlTools.getXnd(html);	//在响应内容中获取学年度选项列表
                String[] xqds=HtmlTools.getXqd(html);	//在响应内容中获取学期选项列表
                ArrayList<Course> courses=HtmlTools.getCourseList(html);	//在响应内容中获取课表

                String _xnd=xnd;
                String _xqd=xqd;

                //如果传进来的学年度和学期参数为空，则使用默认选项
                if(_xnd==null&&xnds.length>0) _xnd=xnds[0];
                if(_xqd==null&&xqds.length>0) _xqd=xqds[0];

                //保存获取到的所有信息
                courseTable.setXnd(xnds);
                courseTable.setXqd(xqds);
                courseTable.setCurrXnd(_xnd);
                courseTable.setCurrXqd(_xqd);
                courseTable.setCourses(courses);
                courseTable.setSimpleInfo(stuInfo);

                Intent it=new Intent(BroadcastAction.COURSE_TABLE);
                Bundle bl=new Bundle();
                bl.putSerializable(Constant.COURSE_TABLE,courseTable);
                it.putExtras(bl);
                sendBroadcast(it);
            }
        });

    }

    @Override
    public void getPersonalInfo() {

        Log.i("getPersonalInfo","********************* getPersonalInfo *********************");
        String urlStr=Constant.BASE_URL+mPersonalInfoURL;//查询课表的URL
        Log.i("getPersonalInfo","*********************"+urlStr+"*********************");

    }

    @Override
    public void getScore() {

        Log.i("getScore","********************* getScore *********************");
        String urlStr=Constant.BASE_URL+mScorceURL;//查询课表的URL
        String referer=Constant.STUDENT_URL+mUser.getName();//引用地址
        Request request = new Request.Builder().url(urlStr).addHeader("Referer",referer).build();
        Call call=mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html=response.body().string();
                mViewState=HtmlTools.findViewState(html);
                score();
            }
        });
    }

    public void score() {

        String urlStr=Constant.BASE_URL+mScorceURL;//查询课表的URL
        String ref=urlStr.substring(0,urlStr.indexOf("&xm=")+4);
        try{
            int index=urlStr.indexOf("&gnmkdm=");
            ref=ref+URLEncoder.encode(urlStr.substring(ref.length(),index),"ISO-8859-1")+urlStr.substring(index);
        }catch (Exception e){
            Log.i("score",">>>>>>>>    出错："+ref);
            return;
        }
        FormBody body=new FormBody.Builder()
                .add("__VIEWSTATE",mViewState)
                .add("ddlXN","")
                .add("ddlXQ","")
                .add("Button1","按学期查询")
                .build();
        Request request = new Request.Builder()
                .url(urlStr)
                .addHeader("Referer", ref)
                .post(body)
                .build();
        Call call=mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html=response.body().string();
                mViewState=HtmlTools.findViewState(html);

                ScoreTable scoreTable=HtmlTools.getScoreTable(html);

                Intent it=new Intent(BroadcastAction.SCORE);
                Bundle bl=new Bundle();
                bl.putSerializable(Constant.SCORE_TABLE,scoreTable);
                it.putExtras(bl);
                sendBroadcast(it);
            }
        });

    }

    /**
     * 查找并保存查询各种信息的URL
     * @param html HTML文档
     */
    private void saveQueryURL(String html) {
        // TODO Auto-generated method stub

        String pattern="<a href=\"(\\w+)\\.aspx\\?xh=(\\d+)&xm=(.+?)&gnmkdm=N(\\d+)\" target='zhuti' onclick=\"GetMc\\('(.+?)'\\);\">(.+?)</a>";
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(html);

        while(m.find()){
            String res=m.group();
            String url=res.substring(res.indexOf("href=\"")+6);
            url=url.substring(0,url.indexOf("\""));

            if(res.contains("学生个人课表")){
                mCourseURL=url;
                continue;
            }
            if(res.contains("成绩查询")){
                mScorceURL=url;
                continue;
            }
            if(res.contains("个人信息")){
                mPersonalInfoURL=url;
            }
        }

    }
}
