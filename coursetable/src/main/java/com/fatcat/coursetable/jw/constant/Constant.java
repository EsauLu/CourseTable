package com.fatcat.coursetable.jw.constant;

/**
 * 常量类
 * @author EsauLu
 *
 */
public class Constant {

	
	/**
	 * 字符编码
	 */
	public static final String ENCODING="GB2312";
	
	/**
	 * 用户类型
	 */
	public static final String RADIO_BUTTON_LIST="学生";
	
	/**
	 * 基础地址
	 */
	public static final String BASE_URL="http://172.16.16.132/";
	
	/**
	 * 验证码URL
	 */
	public static final String CHECK_IMAGE_URL=BASE_URL+"CheckCode.aspx";
	
	/**
	 * 登陆URL
	 */
	public static final String LOGIN_URL=BASE_URL+"default2.aspx";
	
	/**
	 * 登陆后主页面URL
	 */
	public static final String STUDENT_URL=BASE_URL+"xs_main.aspx?xh=";

	/**
	 * 验证码
	 */
	public static final String CHECK="check";

	/**
	 * 登陆成功
	 */
	public static  final String LOGIN_SUCCESS="loginSuccess";

	/**
	 * 登陆失败
	 */
	public static  final String LOGIN_FAIL="loginfail";

	/**
	 * 课表
	 */
	public static final String COURSE_TABLE="courseTable";

	/**
	 * 成绩表
	 */
	public static final String SCORE_TABLE="scoreTable";
	
}
