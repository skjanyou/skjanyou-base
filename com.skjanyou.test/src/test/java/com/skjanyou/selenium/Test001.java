package com.skjanyou.selenium;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.lang.Thread.sleep;

import org.openqa.selenium.By;

import com.codeborne.selenide.Configuration;


public class Test001 {

	
	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.chrome.driver","D:\\软件\\程序方向\\chromedriver_win32/chromedriver.exe");
		Configuration.timeout =6000;
		Configuration.browser ="chrome";//设置浏览器驱动，默认路径看下图 也可以不用默认路径
		//Configuration.reportsFolder = "test-result/reports"; selenide失败回自动保存快照  这是配置快照保存目录 默认也是这个
		Configuration.baseUrl = "https://www.baidu.com";
		open("https://www.baidu.com");
		//sleep(1000);
		$(By.id("kw")).sendKeys("acxiom");
		//$(By.id("kw")).setValue("acxiom");
		$(By.id("su")).click();
		$("body").shouldHave(text("acxiom"));
		sleep(5000);
	}

}