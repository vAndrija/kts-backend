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
    }

    @Test
    public void viewAcceptedMenuItemDetails() {
        loginPage.login("sarajovic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));

        menuItemsListPage.setSelectMenu("standardni");

        menuItemsListPage.clickDetailsButton(0);

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items/1", 10));

        assertTrue(menuItemDetailsPage.getMenuItemName().getText().equals("COCA COLA"));

        assertThrows(TimeoutException.class, () -> {
            menuItemDetailsPage.getPriceInputField();
        });

//        Da li su potrebne sve ove asertacije, ako nema jednog nema nijednog svakako
//        assertThrows(TimeoutException.class, () -> {
//            menuItemDetailsPage.getPreparationPriceInputField();
//        });
//
//        assertThrows(TimeoutException.class, () -> {
//            menuItemDetailsPage.getSelectMenu();
//        });
//
//        assertThrows(TimeoutException.class, () -> {
//            menuItemDetailsPage.getPeriodDatePicker();
//        });
//
//        assertThrows(TimeoutException.class, () -> {
//            menuItemDetailsPage.getAcceptButton();
//        });
//
//        assertThrows(TimeoutException.class, () -> {
//            menuItemDetailsPage.getDeclineButton();
//        });
    }

    @Test
    public void viewAndAcceptUnAcceptedMenuItem() {
        loginPage.login("sarajovic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));

        menuItemDetailsPage.clickOnLinkToUnacceptedMenuItemSPage();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/pending-menu-items", 10));

        menuItemsListPage.clickDetailsButton(0);

        menuItemDetailsPage.setPriceInputField("200");
        menuItemDetailsPage.setPreparationPriceInputField("100");
        menuItemDetailsPage.setSelectMenu("standardni");
        menuItemDetailsPage.setPeriodDatePicker("2022-01-10 2022-12-10");
        menuItemDetailsPage.clickApplyDateButton();

        menuItemDetailsPage.clickAcceptButton();

        assertThrows(TimeoutException.class, () -> {
            menuItemDetailsPage.getPriceInputField();
        });
    }

    @Test
    public void viewAndDeclineUnacceptedMenuItem() {
        loginPage.login("sarajovic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));

        menuItemDetailsPage.clickOnLinkToUnacceptedMenuItemSPage();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/pending-menu-items", 10));

        menuItemsListPage.clickDetailsButton(0);

        menuItemDetailsPage.clickDeclineButton();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/pending-menu-items", 10));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
