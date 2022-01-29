package com.kti.restaurant.e2e.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.pages.MenuFormPage;
import com.kti.restaurant.e2e.pages.MenuItemsListPage;
import com.kti.restaurant.e2e.utils.WaitUtils;

public class MenuFormE2ETest {

	private WebDriver driver;
	
	private MenuFormPage menuFormPage;
	
	private MenuItemsListPage menuItemsListPage;
	
	private LoginPage loginPage;
	
	@BeforeEach
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        loginPage = PageFactory.initElements(driver, LoginPage.class);
        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        menuFormPage = PageFactory.initElements(driver, MenuFormPage.class);

        
        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
        loginPage.login("sarajovic@gmail.com", "123");
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));
	}
	
	@Test
	public void addNewMenu() {
        menuItemsListPage.clickAddMenuButton();        
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/create-menu", 10));
        
        assertFalse(menuFormPage.isButtonEnabled());
        
        menuFormPage.setName("naziv");
        menuFormPage.clickPeriod();
        menuFormPage.clickStartDate();
        menuFormPage.clickEndDate();
        assertTrue(menuFormPage.isButtonEnabled());
        
        menuFormPage.clickApplyButton();
        menuFormPage.clickSubmitButton();
        
        assertDoesNotThrow(() -> {
        	menuFormPage.getNotificationMessage("Meni naziv");
        });
	}
	
	@AfterEach
	public void closeSelenium() {
		driver.quit();
	}
	
	
}
