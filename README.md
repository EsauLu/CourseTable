吉珠课表
==========

##代码说明

###主要包的说明
* com.fatcat.coursetable.activity<br>
应用的界面activity所在包
* com.fatcat.coursetable.widget<br>
负责窗口小部件的包
* com.fatcat.coursetable.jw<br>
这个包用于访问教务网，从教务网上抽取课表等数据后把数据封装起来<br>
  * com.fatcat.coursetable.jw.service<br>
  activity主要通过这个包里的Service获取数据，通过这个包下的Service调用所有网络访问网络的类并返回封装的数据<br>
  * com.fatcat.coursetable.jw.dao<br>
  网络访问接口 所在的包<br>
  * com.fatcat.coursetable.jw.tool<br>
  存放工具类的包，主要是用于解析访问教务网放回的html，获取数据<br>
  * com.fatcat.coursetable.jw.factor<br>
   把各种数据封装成javabean的工厂类包<br>
  
##应用示图
![](https://github.com/EsauLu/CourseTable/raw/master/CaseImg/Screenshot01.png)
![](https://github.com/EsauLu/CourseTable/raw/master/CaseImg/Screenshot02.png)
