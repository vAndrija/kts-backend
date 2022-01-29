package com.kti.restaurant.e2e.tests;

import com.kti.restaurant.e2e.pages.ChangeUserInfoManagerPage;
import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.pages.UserListPage;
import com.kti.restaurant.e2e.utils.WaitUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangeUserInfoManagerE2ETest {

    private WebDriver driver;

    private LoginPage loginPage;

    private UserListPage userListPage;

    private ChangeUserInfoManagerPage changeUserInfoManagerPage;

    @BeforeEach
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver-lin");
        driver = new ChromeDriver();
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        userListPage = PageFactory.initElements(driver, UserListPage.class);
        changeUserInfoManagerPage = PageFactory.initElements(driver,ChangeUserInfoManagerPage.class);
        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
        loginPage.login("sarajovic@gmail.com", "123");
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));
    }

    @Test
    public void updateUserByManager(){
        userListPage.goToUsersPage();
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/users", 10));
        userListPage.updateUser();
        changeUserInfoManagerPage.updateSalary(90000);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
