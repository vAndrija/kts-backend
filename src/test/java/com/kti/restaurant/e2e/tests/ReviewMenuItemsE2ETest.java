package com.kti.restaurant.e2e.tests;

import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.pages.MenuItemsListPage;
import com.kti.restaurant.e2e.utils.WaitUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReviewMenuItemsE2ETest {
    private WebDriver driver;

    private MenuItemsListPage menuItemsListPage;

    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
    }

    @Test
    public void ChooseMenuAndReviewMenuItemsTest() throws InterruptedException {
        loginPage.login("sarajovic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));

        menuItemsListPage.setSelectMenu("standardni");

        assertTrue(menuItemsListPage.getLoadedMenuItems(2).size() == 2);

        menuItemsListPage.clickLoadMore();

        assertTrue(menuItemsListPage.getLoadedMenuItems(4).size() == 4);

        menuItemsListPage.setSelectMenu("letnji");
        assertTrue(menuItemsListPage.getLoadedMenuItems(1).size() == 1);
    }

    @AfterEach
    public void tearDown() {
        //driver.quit();
    }
}
