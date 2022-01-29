package com.kti.restaurant.e2e.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.kti.restaurant.e2e.pages.AcceptOrderItemPage;
import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.pages.MenuItemsListPage;
import com.kti.restaurant.e2e.utils.WaitUtils;

public class AcceptOrderItemE2ETest {
private WebDriver driver;
	
	private LoginPage loginPage;
	
	private MenuItemsListPage menuItemsListPage;
	
	private AcceptOrderItemPage orderItemsPage;
	
	@BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        orderItemsPage = PageFactory.initElements(driver, AcceptOrderItemPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
    }
	
	@Test
	public void acceptOrderItem() {
		loginPage.login("urosmatic@gmail.com", "123");
		
		assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));
		
		menuItemsListPage.clickAcceptOrderButton();
		
		assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/order/unaccepted-order-items", 10));
		
		String orderItemName = orderItemsPage.getOrderItemNameText();
		
		orderItemsPage.clickAcceptButton();
		
		assertDoesNotThrow(() -> {
        	orderItemsPage.getNotificationMessage();
        });
		
		assertNotEquals(orderItemName, orderItemsPage.getOrderItemNameText());
		
	}
	
	@AfterEach
	public void closeSelenium() {
		driver.quit();
	}
	
	
	
}
