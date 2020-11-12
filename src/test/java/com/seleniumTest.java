package com;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class seleniumTest {
    @Test
    public void startSelenium(){
        WebDriver wd = new ChromeDriver();
        //System.setProperty("webdriver.chrome.driver","D:/chromedriver");
        wd.get("https://home.testing-studio.com/");
        wd.findElement(By.xpath("//span[contains(text(),'登录')]")).click();
    }

    @Test
    void testSearch(){
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://ceshiren.com");
        driver.findElement(By.cssSelector(".search-dropdown .d-icon-search")).click();
        driver.findElement(By.cssSelector("#search-term")).sendKeys("selenium");
    }

    @Test
    void testLogin() throws IOException, InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://work.weixin.qq.com/wework_admin/frame");
        //sleep 20
        Thread.sleep(15000);
        Set<Cookie> cookies = driver.manage().getCookies();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.writeValue(new File("cookies.yaml"), cookies);
    }

    @Test
    void testLogined() throws IOException, InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://work.weixin.qq.com/wework_admin/frame");

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TypeReference typeReference=new TypeReference<List<HashMap<String, Object>>>() {};
        List<HashMap<String, Object>> cookies = mapper.readValue(new File("cookies.yaml"), typeReference);
        System.out.println(cookies);

        cookies.forEach(cookieMap->{
            driver.manage().addCookie(new Cookie(cookieMap.get("name").toString(), cookieMap.get("value").toString()));
        });

        driver.navigate().refresh();
        //窗口最大化
       driver.manage().window().maximize();

//        driver.findElement(By.xpath("//span[contains(text(),'添加成员')]")).click();
        //css获取，保证唯一
        driver.findElement(By.cssSelector(".js_service_list .ww_indexImg_AddMember")).click();
        Thread.sleep(3000);
        //循环添加成员
        for(int i=0;i<10;i++){
            //输入信息
            driver.findElement(By.id("username")).sendKeys("测试00"+i);
            Thread.sleep(1000);
            driver.findElement(By.name("acctid")).sendKeys("001001"+i);
            Thread.sleep(1000);
            driver.findElement(By.id("memberAdd_phone")).sendKeys("1312345678"+i);
            Thread.sleep(1000);
            driver.findElement(By.cssSelector(".js_member_editor_form .ww_operationBar .js_btn_continue")).click();
            Thread.sleep(2000);
        }
        //退出浏览器
        //driver.quit();
    }

}
