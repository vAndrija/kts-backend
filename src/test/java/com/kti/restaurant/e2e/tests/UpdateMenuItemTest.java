package com.kti.restaurant.e2e.tests;

import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.pages.MenuItemsListPage;
import com.kti.restaurant.e2e.pages.UpdateMenuItemPage;
import com.kti.restaurant.e2e.utils.WaitUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateMenuItemTest {
    private WebDriver driver;

    private LoginPage loginPage;

    private MenuItemsListPage menuItemsListPage;

    private UpdateMenuItemPage updateMenuItemPage;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        updateMenuItemPage = PageFactory.initElements(driver, UpdateMenuItemPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");

        loginPage.login("sarajovic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));
    }

    @Test
    public void viewAndAcceptUnacceptedMenuItem() {
        menuItemsListPage.clickOnLinkToUnacceptedMenuItemSPage();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/pending-menu-items", 10));

        menuItemsListPage.clickDetailsButton(0);

        updateMenuItemPage.setPreparationTimeInputField("10");
        updateMenuItemPage.setPriceInputField("200");
        updateMenuItemPage.setPreparationPriceInputField("100");
        updateMenuItemPage.setSelectMenu("standardni");

        updateMenuItemPage.clickAcceptButton();

        assertDoesNotThrow(() -> {
            updateMenuItemPage.getNotificationMessage();
        });
    }

    @Test
    public void viewAndDeclineUnacceptedMenuItem() {
        menuItemsListPage.clickOnLinkToUnacceptedMenuItemSPage();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/pending-menu-items", 10));

        menuItemsListPage.clickDetailsButton(0);

        updateMenuItemPage.clickDeclineButton();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/pending-menu-items", 10));
    }

    @Test
    public void viewAndUpdateAcceptedMenuItem() {
        menuItemsListPage.clickDetailsButton(0);

        assertEquals("coca cola", updateMenuItemPage.getNameInputField().getAttribute("value"));
        assertEquals("bezalkoholno gazirano pice", updateMenuItemPage.getDescriptionInputField().getAttribute("value"));
        assertEquals("2", updateMenuItemPage.getPreparationTimeInputField().getAttribute("value"));

        updateMenuItemPage.setNameInputField("Coca-cola");
        updateMenuItemPage.setDescriptionInputField("Gazirano pice");
        updateMenuItemPage.setPreparationTimeInputField("5");
        updateMenuItemPage.setPriceInputField("250");
        updateMenuItemPage.setPreparationPriceInputField("120");

        updateMenuItemPage.clickAcceptButton();

        assertDoesNotThrow(() -> updateMenuItemPage.getNotificationMessage());
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
