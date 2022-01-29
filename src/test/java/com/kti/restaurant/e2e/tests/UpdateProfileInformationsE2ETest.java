package com.kti.restaurant.e2e.tests;

import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.pages.UpdateProfileInfoPage;
import com.kti.restaurant.e2e.utils.WaitUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateProfileInformationsE2ETest {
    public WebDriver driver;


    private LoginPage loginPage;

    private UpdateProfileInfoPage updateProfileInfoPage;
    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        updateProfileInfoPage = PageFactory.initElements(driver,UpdateProfileInfoPage.class);
        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
        loginPage.login("mirkomiric@gmail.com", "123");
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));
    }

    @Test
    public void updateProfileInfo() {
        this.updateProfileInfoPage.openProfilePage();
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/user-update", 10));
        this.updateProfileInfoPage.setName("milovan");
        this.updateProfileInfoPage.setAccountNumber("323232323");
        this.updateProfileInfoPage.submit();

    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
