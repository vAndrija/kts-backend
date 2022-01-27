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

        loginPage.login("sarajovic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));
    }

    @Test
    public void viewAcceptedMenuItemDetails() {
        menuItemsListPage.clickDetailsButton(0);

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items/1", 10));

        assertTrue(menuItemDetailsPage.getMenuItemName().getText().equals("COCA COLA"));
        assertTrue(menuItemDetailsPage.getMenuItemPrice().getText().equals("180 RSD"));

        assertThrows(TimeoutException.class, () -> {
            menuItemDetailsPage.getPriceInputField();
        });
    }

    @Test
    public void viewAndAcceptUnAcceptedMenuItem() {
        menuItemDetailsPage.clickOnLinkToUnacceptedMenuItemSPage();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/pending-menu-items", 10));

        assertEquals("PITA SA VISNJAMA", menuItemDetailsPage.getMenuItemName().getText());

        menuItemsListPage.clickDetailsButton(0);

        menuItemDetailsPage.setPriceInputField("200");
        menuItemDetailsPage.setPreparationPriceInputField("100");
        menuItemDetailsPage.setSelectMenu("standardni");

        menuItemDetailsPage.clickAcceptButton();

        assertThrows(TimeoutException.class, () -> {
            menuItemDetailsPage.getPriceInputField();
        });

        assertEquals("PITA SA VISNJAMA", menuItemDetailsPage.getMenuItemName().getText());

        assertEquals("200 RSD", menuItemDetailsPage.getMenuItemPrice().getText());
    }

    @Test
    public void viewAndDeclineUnacceptedMenuItem() {
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
