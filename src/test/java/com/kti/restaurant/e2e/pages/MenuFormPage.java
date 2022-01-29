package com.kti.restaurant.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.kti.restaurant.e2e.utils.WaitUtils;


public class MenuFormPage {

	private WebDriver driver;
	
	@FindBy(xpath = "//*[@formControlName=\"name\"]")
    private WebElement name;

    @FindBy(xpath = "//*[@formControlName=\"period\"]")
    private WebElement period;
    
    @FindBy(className = "applyBtn")
    private WebElement applyButton;
    
    @FindBy(className = "btn")
    private WebElement submitButton;
    
    @FindBy(xpath = "//div[@class='drp-calendar left']//div[@class='calendar-table']//tbody//td[5]")
    private WebElement startDate;
    
    @FindBy(xpath = "//div[@class='drp-calendar right']//div[@class='calendar-table']//tbody//td[6]")
    private WebElement endDate;
    

    
    public MenuFormPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getName() {
        return WaitUtils.visibilityWait(driver, name, 10);
    }

    public void setName(String name) {
        WebElement nameElement = getName();
        nameElement.clear();
        nameElement.sendKeys(name);
    }

    public void clickStartDate() {
    	WaitUtils.visibilityWait(driver, startDate, 10).click();
    }
    
    public void clickEndDate() {
    	WaitUtils.visibilityWait(driver, endDate, 10).click();
    }
    
    public void clickPeriod() {
    	WaitUtils.visibilityWait(driver, period, 10).click();
    }
    
    public WebElement getNotificationMessage(String message) {
    	return WaitUtils.presenceWait(driver, By.xpath(String.format("//div[contains(text(), '%s')]", message)), 10);
    }
    
    public boolean isButtonEnabled() {
    	return this.submitButton.isEnabled();
    }
    
    private WebElement getSubmitButton() {
        return WaitUtils.clickableWait(driver, submitButton, 10);
    }
    
    public void clickSubmitButton() {
    	WebElement button = getSubmitButton();
    	button.click();
    }
    
    private WebElement getApplyButton() {
        return WaitUtils.clickableWait(driver, applyButton, 10);
    }
    
    public void clickApplyButton() {
    	WebElement button = getApplyButton();
    	button.click();
    }

}
