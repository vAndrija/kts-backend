package com.kti.restaurant.e2e.tests;

import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.pages.MenuItemsListPage;
import com.kti.restaurant.e2e.pages.RestaurantPreviewPage;
import com.kti.restaurant.e2e.utils.WaitUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestaurantPreviewE2ETest {

    private WebDriver driver;

    private RestaurantPreviewPage restaurantPreviewPage;

    private MenuItemsListPage menuItemsListPage;

    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        restaurantPreviewPage = PageFactory.initElements(driver, RestaurantPreviewPage.class);
        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");

        loginPage.login("mirkomiric@gmail.com", "123");
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));

        menuItemsListPage.clickRestaurantButton();
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/restaurant/preview", 10));
    }

    @Test
    public void addAndDeleteRestaurantTable() {

        restaurantPreviewPage.clickAddButton();

        restaurantPreviewPage.setCapacity("6");

        restaurantPreviewPage.clickAddCapacity();


        new Actions(driver).moveToElement(restaurantPreviewPage.getCanvas()).moveByOffset(470, -177).click().perform();

        restaurantPreviewPage.clickDeleteButton();

        restaurantPreviewPage.clickYesButton();

    }

    @Test
    public void invalidDelete() {

        restaurantPreviewPage.clickDeleteButton();

        assertEquals("Morate selektovati sto!", restaurantPreviewPage.getMessage().getText());
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
