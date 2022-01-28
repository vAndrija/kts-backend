package com.kti.restaurant.e2e.tests;

import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.pages.MenuItemDetailsPage;
import com.kti.restaurant.e2e.pages.MenuItemsListPage;
import com.kti.restaurant.e2e.utils.WaitUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

public class MenuItemDetailsE2ETest {
    private WebDriver driver;

    private LoginPage loginPage;

    private MenuItemsListPage menuItemsListPage;

    private MenuItemDetailsPage menuItemDetailsPage;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        menuItemDetailsPage = PageFactory.initElements(driver, MenuItemDetailsPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");

        loginPage.login("anapopovic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));
    }

    @Test
    public void viewMenuItemDetails() {
        menuItemsListPage.clickDetailsButton(0);

        assertDoesNotThrow(() -> {
            menuItemDetailsPage.getMenuItemName();
        });

        assertDoesNotThrow(() -> {
            menuItemDetailsPage.getMenuItemPrice();
        });
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
