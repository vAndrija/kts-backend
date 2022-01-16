package com.kti.restaurant.e2e.tests;

import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.pages.ResetPasswordPage;
import com.kti.restaurant.e2e.utils.WaitUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResetPasswordPageTest {
    private WebDriver driver;

    private ResetPasswordPage resetPasswordPage;

    @BeforeEach
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        resetPasswordPage = PageFactory.initElements(driver, ResetPasswordPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
    }


    @Test
    public void getToPage(){
        resetPasswordPage.clickToResetPasswordLink();
        assertTrue(WaitUtils.urlWait(driver,"http://localhost:4200/auth/password-reset-start",10));
        resetPasswordPage.resetPassword("mirkomiric@gmail.com");
        assertTrue(WaitUtils.urlWait(driver,"http://localhost:4200/auth/login",10));
    }
}
