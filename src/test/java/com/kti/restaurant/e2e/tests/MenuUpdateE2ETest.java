package com.kti.restaurant.e2e.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import com.kti.restaurant.e2e.pages.MenuReviewPage;
import com.kti.restaurant.e2e.utils.WaitUtils;

public class MenuUpdateE2ETest {
	private WebDriver driver;

    private MenuItemsListPage menuItemsListPage;

    private LoginPage loginPage;
    
    private MenuFormPage formPage;
    
    private MenuReviewPage menuReviewPage;
    
    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        menuReviewPage = PageFactory.initElements(driver, MenuReviewPage.class);
        formPage = PageFactory.initElements(driver, MenuFormPage.class);
        
        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");

        loginPage.login("sarajovic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));
        
    }
    
    @Test
    public void updateMenu() {
    	menuItemsListPage.clickOnLMenuPreviewPage();
    	
    	assertTrue(WaitUtils.urlWait(driver,"http://localhost:4200/menu/menu-table", 10));
    	
    	menuReviewPage.clickEdit("letnji");
    	
    	assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/update-menu/2", 10));
    
    	 
    	formPage.setName("letnji 2.0");
    	formPage.clickPeriod();
    	formPage.clickStartDate();
    	formPage.clickEndDate();
        assertTrue(formPage.isButtonEnabled());
        
        formPage.clickApplyButton();
       
        formPage.clickSubmitButton();
       
        menuItemsListPage.clickOnLMenuPreviewPage();
        
        assertDoesNotThrow(() -> {
        	formPage.getNotificationMessage("Meni letnji 2.0");
        });
    }
    
    @AfterEach
	public void closeSelenium() {
		driver.quit();
	}
}
