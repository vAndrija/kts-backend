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
import com.kti.restaurant.e2e.pages.MenuItemsListPage;
import com.kti.restaurant.e2e.pages.NewDishDrinkPage;
import com.kti.restaurant.e2e.utils.WaitUtils;

public class NewDishDrinkE2ETest {
	private WebDriver driver;
	
	private LoginPage loginPage;
	
	private MenuItemsListPage menuItemsListPage;
	
	private NewDishDrinkPage newDishDrinkPage;
	
	@BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        newDishDrinkPage = PageFactory.initElements(driver, NewDishDrinkPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
    }
	
	@Test
	public void addNewDishOrDrink() {
		loginPage.login("urosmatic@gmail.com", "123");
		
		assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));
		
		menuItemsListPage.clickAddNewDishButton();
		
		assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/create-menu-item", 10));
		
		assertFalse(newDishDrinkPage.isSubmitButtonEnabled());
		
		newDishDrinkPage.setNameInputField("ime");
		
		assertFalse(newDishDrinkPage.isSubmitButtonEnabled());
		
		newDishDrinkPage.setDescriptionInputField("opis");
		
		assertFalse(newDishDrinkPage.isSubmitButtonEnabled());
		
		newDishDrinkPage.setPreparationTimeInputField("2");
		
		assertFalse(newDishDrinkPage.isSubmitButtonEnabled());
		
		newDishDrinkPage.setCategorySelect("Supa");
		
		assertTrue(newDishDrinkPage.isSubmitButtonEnabled());
		
		newDishDrinkPage.clickSubmitButton();
		
		assertDoesNotThrow(() -> {
        	newDishDrinkPage.getNotificationMessage();
        });
		
	}

	@AfterEach
	public void closeSelenium() {
		driver.quit();
	}
}
