package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ReportPage {
    private WebDriver driver;

    @FindBy(id = "yearly")
    private WebElement inputYearForYearlyReport;

    @FindBy(id = "yearlyBtn")
    private WebElement yearlyReportButton;

    @FindBy(id = "monthly")
    private WebElement inputYearForMonthlyReport;

    @FindBy(xpath = "//*[@formControlName=\"month\"]")
    private WebElement inputMonth;

    @FindBy(id = "monthlyBtn")
    private WebElement monthlyReportButton;

    public ReportPage(WebDriver webDriver) {
        this.driver = webDriver;
    }

    public List<WebElement> getYearlyBars() {
        return WaitUtils.presenceWaitAll(driver, By.xpath("//*[name()='g' and @class='apexcharts-series']//*[name()='path']"), 10);
    }

    public List<WebElement> getMonthlyBars() {
        return WaitUtils.presenceWaitAll(driver, By.xpath("(//*[name()='g' and @class='apexcharts-series'])[2]//*[name()='path']"), 10);
    }

    public void setYearlySelect(String selectMenuOption) {
        Select selectMenu = getSelectForYearlyReport();
        getSelectOption(selectMenuOption);
        selectMenu.selectByVisibleText(selectMenuOption);
    }

    public void setMonthlySelect(String selectMenuOption) {
        Select selectMenu = getSelectForMonthlyReport();
        getSelectOption(selectMenuOption);
        selectMenu.selectByVisibleText(selectMenuOption);
    }

    public Select getSelectForYearlyReport() {
        return new Select(WaitUtils.visibilityWait(driver, By.xpath("//*[@id=\"menuItemsYearly\"]"), 10).get(0));
    }

    public Select getSelectForMonthlyReport() {
        return new Select(WaitUtils.visibilityWait(driver, By.xpath("//*[@id=\"menuItemsMonthly\"]"), 10).get(0));
    }

    public List<WebElement> getSelectOption(String option) {
        return WaitUtils.visibilityWait(driver, By.xpath("//option[contains(text(), '" + option + "')]"), 10);
    }

    public void clickYearlyReportButton() {
        getYearlyReportButton().click();
    }

    public WebElement getInputYear() {
        return WaitUtils.visibilityWait(driver, inputYearForYearlyReport, 10);
    }

    public void clickMonthlyReportButton() {
        getMonthlyReportButton().click();
    }

    public void setInputYear(String year) {
        WebElement inputField = getInputYear();
        inputField.clear();
        inputField.sendKeys(year);
    }

    public WebElement getYearlyReportButton() {
        return WaitUtils.clickableWait(driver, yearlyReportButton, 10);
    }

    public List<WebElement> getBarWithVal(String value) {
        return WaitUtils.visibilityWait(driver, By.xpath("//*[@val='" + value + "']"), 10);
    }

    public WebElement getInputYearForMonthlyReport() {
        return WaitUtils.visibilityWait(driver, inputYearForMonthlyReport, 10);
    }

    public WebElement getInputMonth() {
        return WaitUtils.visibilityWait(driver, inputMonth, 10);
    }

    public WebElement getMonthlyReportButton() {
        return WaitUtils.visibilityWait(driver, monthlyReportButton, 10);
    }

    public void setInputYearForMonthlyReport(String year) {
        WebElement inputField = getInputYearForMonthlyReport();
        inputField.clear();
        inputField.sendKeys(year);
    }

    public void setInputMonth(String month) {
        WebElement inputField = getInputMonth();
        inputField.clear();
        inputField.sendKeys(month);
    }
}
