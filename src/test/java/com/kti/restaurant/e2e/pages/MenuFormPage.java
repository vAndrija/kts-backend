package com.kti.restaurant.e2e.pages;

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
    
    @FindBy(xpath = "//div[contains(text(), \"Menu added!\")]")
    private WebElement notificationMessage;
    
    @FindBy(className = "applyBtn")
    private WebElement applyButton;
    
    @FindBy(className = "btn")
    private WebElement submitButton;
    
    public MenuFormPage(WebDriver driver) {
        this.driver = driver;
    }

    public void menuForm(String name, String period) {
        this.setName(name);
        this.setPeriod(period);
        this.getSubmitButton().click();
    }

    public WebElement getName() {
        return WaitUtils.visibilityWait(driver, name, 10);
    }

    public void setName(String name) {
        WebElement nameElement = getName();
        nameElement.clear();
        nameElement.sendKeys(name);
    }

    public WebElement getPeriod() {
        return WaitUtils.visibilityWait(driver, period, 10);
    }

    public void setPeriod(String password) {
        WebElement periodElement = getPeriod();
        periodElement.clear();
        periodElement.sendKeys(password);
    }

    public WebElement getNotificationMessage() {
    	return WaitUtils.visibilityWait(driver, notificationMessage, 10);
    }
    
    public boolean isButtonEnabled() {
    	return submitButton.isEnabled();
    }
    
    public WebElement getSubmitButton() {
        return WaitUtils.clickableWait(driver, submitButton, 10);
    }
    
    public void clickSubmitButton() {
    	WebElement button = getSubmitButton();
    	button.click();
    }
    
    public WebElement getApplyButton() {
        return WaitUtils.clickableWait(driver, applyButton, 10);
    }
    
    public void clickApplyButton() {
    	WebElement button = getApplyButton();
    	button.click();
    }

}
