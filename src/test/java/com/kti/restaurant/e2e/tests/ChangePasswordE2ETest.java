package com.kti.restaurant.e2e.tests;

import com.kti.restaurant.e2e.pages.ChangePasswordPage;
import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.utils.WaitUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

public class ChangePasswordE2ETest {

    private WebDriver driver;

    private LoginPage loginPage;

    private ChangePasswordPage changePasswordPage;
    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        changePasswordPage = PageFactory.initElements(driver,ChangePasswordPage.class);
        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
        loginPage.login("mirkomiric@gmail.com", "123");
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));
    }

    @Test
    public void changePassword() {
        this.changePasswordPage.openPage();
        this.changePasswordPage.setOldPassword("123");
        assertFalse(this.changePasswordPage.isButtonEnabled());
        this.changePasswordPage.setPassword("1234");
        assertFalse(this.changePasswordPage.isButtonEnabled());
        this.changePasswordPage.setRepeatedPassword("1234");
        assertTrue(this.changePasswordPage.isButtonEnabled());
        this.changePasswordPage.changePassword();
        this.changePasswordPage.getSuccessNotification();
        this.changePasswordPage.logout();
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/auth/login", 10));
        loginPage.login("mirkomiric@gmail.com", "1234");
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
