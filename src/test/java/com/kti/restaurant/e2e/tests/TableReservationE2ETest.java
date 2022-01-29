package com.kti.restaurant.e2e.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.pages.MenuItemsListPage;
import com.kti.restaurant.e2e.pages.TableReservationPage;
import com.kti.restaurant.e2e.utils.WaitUtils;

public class TableReservationE2ETest {

	private WebDriver driver;
	private LoginPage loginPage;
	private MenuItemsListPage menuItemsListPage;
	private TableReservationPage reservationPage;
	
	@BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        reservationPage = PageFactory.initElements(driver, TableReservationPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
        
        loginPage.login("anapopovic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));

    }
	
	@Test
	public void addReservations() {
		menuItemsListPage.clickReservationButton();
		assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/reservation/reservation-table", 10));
		
		int reservationSize = reservationPage.getRows().size();
		
		reservationPage.clickReserveTableButton();
		
		assertFalse(reservationPage.isReserveButtonEnabled());
		
		reservationPage.setName("Ime");
		assertFalse(reservationPage.isReserveButtonEnabled());
		
		reservationPage.setReservationDate("02-02-2022");
		assertFalse(reservationPage.isReserveButtonEnabled());

		reservationPage.setReservationStart("13:30");
		assertFalse(reservationPage.isReserveButtonEnabled());

		reservationPage.setReservationEnd("14:30");
		assertFalse(reservationPage.isReserveButtonEnabled());

		reservationPage.setCategorySelect("1");
		assertTrue(reservationPage.isReserveButtonEnabled());
		
		reservationPage.clickReserveButton();
		
		assertDoesNotThrow(() -> {
        	reservationPage.getNotificationMessage();
        });
		
		assertEquals(reservationSize + 1, reservationPage.getRows().size());
		
		reservationPage.clickCloseButton();
	} 
	
	@Test
	public void editTable() {
		menuItemsListPage.clickReservationButton();
		assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/reservation/reservation-table", 10));

		reservationPage.clickEdit("Ana Jojic");
		
		reservationPage.setReservationDate("03-03-2022");
		reservationPage.setReservationStart("12:00");
		reservationPage.setReservationEnd("13:00");
		reservationPage.setCategorySelect("2");
		
		reservationPage.clickChangeButton();
		
		reservationPage.clickCloseButton();
		
		assertEquals(true, reservationPage.isRowPresent("Ana Jojic", "2", "3.3.2022. 12:00", "3.3.2022. 13:00"));
	}
	
	@Test
	public void deleteTable() {
		menuItemsListPage.clickReservationButton();
		assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/reservation/reservation-table", 10));
		
		int pageElement = reservationPage.getRows().size();
		
		reservationPage.clickCancelButton("Milica Petric");
		
		assertDoesNotThrow(() -> {
        	reservationPage.getLoadedReservations(pageElement-1);
        });
		
		assertDoesNotThrow(() -> {
        	reservationPage.getNotificationMessage();
        });
		
	}
}
