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
import com.kti.restaurant.e2e.pages.NotificationPage;
import com.kti.restaurant.e2e.utils.WaitUtils;

public class NotificationPreviewE2ETest {
	private WebDriver driver;
	
	private LoginPage loginPage;
	
	
	private NotificationPage notificationPage;
	
	@BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        loginPage = PageFactory.initElements(driver, LoginPage.class);
        notificationPage = PageFactory.initElements(driver, NotificationPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
    }
	
	@Test
	public void notificationsPreview() {
		loginPage.login("anapopovic@gmail.com", "123");
		assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));
		
		notificationPage.clickOnNotification();
		
		assertDoesNotThrow(() -> {
			notificationPage.getRows(2);
		});
	}
	
	@AfterEach
	public void closeSelenium() {
		driver.quit();
	}
}
