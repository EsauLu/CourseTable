package com.fatcat.coursetable.jw.dao;

import com.fatcat.coursetable.jw.bean.CourseTable;
import com.fatcat.coursetable.jw.bean.PersonalInfo;
import com.fatcat.coursetable.jw.bean.User;

/**
 * 获取数据的接口
 * @author EsauLu
 *
 */
public interface HttpDAO {	

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
	public String sendGetRequest(String url,String ref);
	
	/**
	 * 根据指定url和参数值发送post请求
	 * @param url 请求url
	 * @param ref 引用
	 * @return 响应页面的HTML文档
	 */
	public String sendPostRequest(String url,String ref);
	
	/**
	 * 获取验证码
	 * @return 验证码图片
	 */
	public byte[] getCheckImg();
	
	/**
	 * 登陆
	 * @param user 用户信息
	 * @return 返回登陆是否成功
	 */
	public boolean login(User user);
	
	/**
	 * 根据学年度和学期获取课表
	 * @param xnd 学年度
	 * @param xqd 学期
	 * @return 课表
	 */
	public CourseTable getCourseTable(String xnd, String xqd);
	
	/**
	 * 根据学年度和学期获取课表的Json串
	 * @param xnd 学年度
	 * @param xqd 学期
	 * @return 课表Json串
	 */
	public String getCourseTableAsJson(String xnd,String xqd);
	
	/**
	 * 获取个人信息
	 * @param url 查询个人信息的url
	 * @return 个人信息
	 */
	public PersonalInfo getPersonalInfo(String url);
	
	/**
	 * 获取错误信息
	 * @return 返回错误信息
	 */
	public String getErrorMessege();

}















































