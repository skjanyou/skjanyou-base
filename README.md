`目前仅仅只是编码阶段，强烈不建议在正式生产环境中使用。`
# Skjanyou-base 

一个轻量级、快速的、模块化、插件化、可定制的软件框架。
他有以下特点：


1. 框架内部自带Http服务器,支持mvc模式开发
2. 框架自带仿Mybatis的ORM框架,支持注解式SQL
3. 因框架内许多功能都是自实现，因此第三方依赖较传统Web框架（比如Springmvc）较少，启动速度也较快
4. 应用无需部署在Weblogic、tomcat等容器，一个main方法直接启动应用
5. 有较为丰富的插件扩展机制，并且框架内部默认提供较多的插件,比如日志插件、Http支持插件、Db插件、自实现Mybatis插件，且支持自定义插件。
6. 支持web开发、桌面应用（Javafx）、移动端应用开发

## 源码下载：

[GitHub](https://github.com/skjanyou/skjanyou-base)

[Gitee](https://gitee.com/skjanyou/skjanyou-base)

## 使用方法
### 1.先拉取代码
`` git clone https://github.com/skjanyou/skjanyou-base.git ``
### 2.进入工程,进行编译打包
``` 
cd skjanyou-base
mvn clean install -DskipTests
```
![blockchain](https://uploader.shimo.im/f/sf6TiwvybLC45XIy.png!thumbnail?accessToken=eyJhbGciOiJIUzI1NiIsImtpZCI6ImRlZmF1bHQiLCJ0eXAiOiJKV1QifQ.eyJleHAiOjE2MTgwMjgwNDYsImciOiJyaENZcVlEZGQza1ZqdEhHIiwiaWF0IjoxNjE4MDI2MjQ2LCJ1Ijo2MTc1MDkwOX0.huoLbnDLyXSQCIDLcwm8d2aLSyTg3zOPCz0lIsYGaIE)
如图,表示已经完成编译了
### 3.创建自己的工程,按需引入自己需要的包
如果你开发web应用，可以使用以下依赖
```
<dependencies>
	<dependency>
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
		<version>3.8.1</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>com.skjanyou</groupId>
		<artifactId>com.skjanyou.db.mybatis</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
	<dependency>
		<groupId>com.skjanyou</groupId>
		<artifactId>com.skjanyou.mvc</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
	<dependency>
		<groupId>com.skjanyou</groupId>
		<artifactId>com.skjanyou.start</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
	<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<version>8.0.11</version>
	</dependency>		
</dependencies>
```

如果你想开发桌面应用,可以额外添加javafx的依赖
```
<dependencies>
	<dependency>
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
		<version>3.8.1</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>com.skjanyou</groupId>
		<artifactId>com.skjanyou.db.mybatis</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
	<dependency>
		<groupId>com.skjanyou</groupId>
		<artifactId>com.skjanyou.mvc</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
	<dependency>
		<groupId>com.skjanyou</groupId>
		<artifactId>com.skjanyou.javafx</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
	<!-- javafx优秀的UI框架 -->
	<dependency>
		<groupId>com.jfoenix</groupId>
		<artifactId>jfoenix</artifactId>
		<version>8.0.9</version>
	</dependency>	
	<dependency>
		<groupId>com.skjanyou</groupId>
		<artifactId>com.skjanyou.start</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
	<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<version>8.0.11</version>
	</dependency>		
</dependencies>

```
移动应用待开发。

### 4.项目结构
+ 项目结构按参照maven规范
+ skjanyou框架建议resources目录下面分为config(配置文件)、dbscript(数据库脚本)、plugin(插件配置文件)、static(静态资源文件)


![blockchain](https://uploader.shimo.im/f/pyznUORgQ74PVVVS.png)



### 5.编写入口类与配置文件
Skjanyou应用启动接收start、help、stop三个参数（可扩展），启动时需要给应用配置一个skjanyou.configfile的环境变量项,用以指定全局配置文件。

以下是入口类的示例：
```
@Configure(
		name = "Default 配置",
		scanPath = "com.skjanyou.blog",
		configManagerFactory = PropertiesConfig.class
)
public class SkjanyouBlogStart {
	public static void main(String[] args) {
		if(System.getProperty("skjanyou.configfile") == null){
			System.setProperty("skjanyou.configfile", "classpath:/config/blog.properties");
		};
		if( args.length == 0 ) {
			args = new String[] {"start"};
		}
				
		SkjanyouApplicationStart.start(SkjanyouBlogStart.class, args);
	}
}
```
以下是配置文件的示例：
```
##Mvc配置
mvc.port=1235
mvc.ip=127.0.0.1
mvc.scanPath=com.skjanyou.blog
mvc.filters=[]
##关闭simplehttpserver
simplehttpserver.use=false

#数据库,这里使用sqlite
db.className=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://127.0.0.1:3306/blog?zeroDateTimeBehavior=CONVERT_TO_NULL&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
db.user=root
db.password=123456
```


### 6.启动工程
普通的Java工程,直接通过main函数启动即可

### 7.打包应用
```
mvn -clean package -DskipTests
```

### 8.发布应用的一些建议
Web应用应用建议的发布应用方式有两种：
1.可以使用maven-shade-plugin插件将所有的依赖打包到一个Jar中，通过java -jar xx.jar启动
2.使用jsw插件，打包成系统服务（同时支持全平台），通过wrapper start启动

