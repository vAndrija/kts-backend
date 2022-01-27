package com.kti.restaurant.e2e.tests;

import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.pages.MenuItemsListPage;
import com.kti.restaurant.e2e.pages.OrderItemsTablePage;
import com.kti.restaurant.e2e.utils.WaitUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderItemsTableE2ETest {
    private WebDriver driver;

    private OrderItemsTablePage orderItemsTablePage;

    private MenuItemsListPage menuItemsListPage;

    private LoginPage loginPage;


    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        orderItemsTablePage = PageFactory.initElements(driver, OrderItemsTablePage.class);
        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
    }

    @Test
    public void viewOrderItems() {
        loginPage.login("kristinamisic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));

        menuItemsListPage.clickOrderItemsButton();
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/order/order-items", 10));

        orderItemsTablePage.setSelectStatus("U pripremi");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/order/order-items", 10));

        assertEquals(2, orderItemsTablePage.getRows().size());


        orderItemsTablePage.clickStatusButton();

        orderItemsTablePage.clickNewStatusButton();

        assertEquals("Pripremljeno", orderItemsTablePage.getChangedButton().getText());

    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
