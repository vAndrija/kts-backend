package com.kti.restaurant.e2e.tests;

import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.pages.MenuItemsListPage;
import com.kti.restaurant.e2e.pages.OrderTablePage;
import com.kti.restaurant.e2e.utils.WaitUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTableE2ETest {
    private WebDriver driver;

    private OrderTablePage orderTablePage;

    private MenuItemsListPage menuItemsListPage;

    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        orderTablePage = PageFactory.initElements(driver, OrderTablePage.class);
        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
    }

    @Test
    public void viewOrdersAndFilterByStatus() {
        loginPage.login("jovanpetrovic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));

        menuItemsListPage.clickOrderButton();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/order/orders", 10));

        orderTablePage.setSelectStatus("Poručeno");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/order/orders", 10));

        assertEquals(2, orderTablePage.getRows().size());

        assertEquals("Poručeno", orderTablePage.getInvalidStatusButton().getText());

        orderTablePage.clickInvalidStatusButton();

        orderTablePage.clickNewInvalidStatusButton();

        assertEquals("Poručeno", orderTablePage.getInvalidStatusButton().getText());

        orderTablePage.clickValidStatusButton();

        orderTablePage.clickNewValidStatusButton();

        assertEquals("Završeno", orderTablePage.getChangedButton().getText());


    }

}
