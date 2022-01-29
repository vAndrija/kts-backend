package com.kti.restaurant.e2e.tests;

import com.kti.restaurant.e2e.pages.LoginPage;
import com.kti.restaurant.e2e.pages.MenuItemsListPage;
import com.kti.restaurant.e2e.pages.ReportPage;
import com.kti.restaurant.e2e.utils.WaitUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportsE2ETest {
    private WebDriver driver;

    private MenuItemsListPage menuItemsListPage;

    private LoginPage loginPage;

    private ReportPage reportPage;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        reportPage = PageFactory.initElements(driver, ReportPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");

        loginPage.login("sarajovic@gmail.com", "123");

        menuItemsListPage.clickLinkToReports();
    }

    @Test
    public void getMealDrinkCostReport() {
        menuItemsListPage.clickLinkToCostsForMealDrinkReport();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/report/meal-drink-costs", 10));

        reportPage.setInputYear("2021");
        reportPage.clickYearlyReportButton();

        List<WebElement> pathElementsYearly = reportPage.getYearlyBars();
        assertEquals(12, pathElementsYearly.size());
        assertEquals("1860", pathElementsYearly.get(10).getAttribute("val"));

        reportPage.setInputYearForMonthlyReport("2021");
        reportPage.setInputMonth("11");
        reportPage.clickMonthlyReportButton();

        List<WebElement> pathElementsMonthly = reportPage.getMonthlyBars();
        assertEquals(30, pathElementsMonthly.size());
        assertEquals("1860", pathElementsMonthly.get(18).getAttribute("val"));
    }

    @Test
    public void getMealDrinkSalesReport() {
        menuItemsListPage.clickLinkToBenefitReport();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/report/meal-drink-sales", 10));

        reportPage.setInputYear("2021");
        reportPage.setYearlySelect("coca cola");
        reportPage.clickYearlyReportButton();

        List<WebElement> pathElementsYearly = reportPage.getYearlyBars();
        assertEquals(12, pathElementsYearly.size());
        assertEquals("3", pathElementsYearly.get(10).getAttribute("val"));

        reportPage.setInputYearForMonthlyReport("2021");
        reportPage.setInputMonth("11");
        reportPage.setMonthlySelect("coca cola");
        reportPage.clickMonthlyReportButton();

        List<WebElement> pathElementsMonthly = reportPage.getMonthlyBars();
        assertEquals(30, pathElementsMonthly.size());
        assertEquals("3", pathElementsMonthly.get(18).getAttribute("val"));
    }

    @Test
    public void getCostBenefitRatioReport() {
        menuItemsListPage.clickLinkToCostBenefitRatioReport();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/report/cost-benefit-ratio", 10));

        reportPage.setInputYear("2021");
        reportPage.clickYearlyReportButton();

        List<WebElement> pathElementsYearly = reportPage.getYearlyBars();
        assertEquals(12, pathElementsYearly.size());
        assertEquals("0", pathElementsYearly.get(0).getAttribute("val"));
        assertEquals("-135000", pathElementsYearly.get(1).getAttribute("val"));
        assertEquals("-135000", pathElementsYearly.get(2).getAttribute("val"));
        assertEquals("-135000", pathElementsYearly.get(3).getAttribute("val"));
        assertEquals("-135000", pathElementsYearly.get(4).getAttribute("val"));
        assertEquals("-135000", pathElementsYearly.get(5).getAttribute("val"));
        assertEquals("-135000", pathElementsYearly.get(6).getAttribute("val"));
        assertEquals("-135000", pathElementsYearly.get(7).getAttribute("val"));
        assertEquals("-195000", pathElementsYearly.get(8).getAttribute("val"));
        assertEquals("-195000", pathElementsYearly.get(9).getAttribute("val"));
        assertEquals("-242050", pathElementsYearly.get(10).getAttribute("val"));
        assertEquals("-464000", pathElementsYearly.get(11).getAttribute("val"));

        reportPage.setInputYearForMonthlyReport("2021");
        reportPage.setInputMonth("11");
        reportPage.clickMonthlyReportButton();

        List<WebElement> pathElementsMonthly = reportPage.getMonthlyBars();
        assertEquals(30, pathElementsMonthly.size());
        assertEquals("-5217", pathElementsMonthly.get(18).getAttribute("val"));
        assertEquals("-8167", pathElementsMonthly.get(0).getAttribute("val"));
    }

    @Test
    public void getPreparingTimeReport() {
        menuItemsListPage.clickLinkToTimeForPreparationReport();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/report/preparing-time", 10));

        reportPage.setInputYear("2021");
        reportPage.setYearlySelect("kristina");
        reportPage.clickYearlyReportButton();

        List<WebElement> pathElementsYearly = reportPage.getYearlyBars();
        assertEquals(12, pathElementsYearly.size());
        assertEquals("18", pathElementsYearly.get(10).getAttribute("val"));

        reportPage.setInputYearForMonthlyReport("2021");
        reportPage.setInputMonth("11");
        reportPage.setMonthlySelect("kristina");
        reportPage.clickMonthlyReportButton();

        List<WebElement> pathElementsMonthly = reportPage.getMonthlyBars();
        assertEquals(30, pathElementsMonthly.size());
        assertEquals("18", pathElementsMonthly.get(18).getAttribute("val"));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
