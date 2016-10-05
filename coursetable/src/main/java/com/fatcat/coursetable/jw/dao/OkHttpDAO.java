package com.fatcat.coursetable.jw.dao;

import com.fatcat.coursetable.jw.bean.User;

/**
 * 网络访问接口
 * Created by EsauLu on 2016-10-01.
 */

public interface OkHttpDAO {

    /**
     * 初始化，主要用于收集cookie和viewState
     */
    public void init();

    /**
     * 根据指定url发送给请求
     * @param url 请求url
     * @param ref 引用
     * @return 响应页面的HTML文档
     */
    public void sendGetRequest(String url,String ref);

    /**
     * 根据指定url和参数值发送post请求
     * @param url 请求url
     * @param ref 引用
     * @return 响应页面的HTML文档
     */
    public void sendPostRequest(String url,String ref);

    /**
     * 获取验证码
     * @return 验证码图片
     */
    public void getCheckImg();

    /**
     * 登陆
     * @param user 用户信息
     * @return 返回登陆是否成功
     */
    public void login(User user);

    /**
     * 根据学年度和学期获取课表
     * @param xnd 学年度
     * @param xqd 学期
     * @return 课表
     */
    public void getCourseTable(String xnd, String xqd);

    /**
     * 获取个人信息
     * @return 个人信息
     */
    public void getScore();

    /**
     * 获取个人信息
     * @param url 查询个人信息的url
     * @return 个人信息
     */
    public void getPersonalInfo();


}
