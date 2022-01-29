package com.kti.restaurant.e2e.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.kti.restaurant.e2e.utils.WaitUtils;

public class NotificationPage {

	private WebDriver driver;
	
	@FindBy(xpath = "//a[@role='button']")
	private WebElement notificationButton;
	
	
	public NotificationPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void clickOnNotification() {
        WebElement link = WaitUtils.visibilityWait(driver, notificationButton, 10);
        link.click();
    }
	
	public List<WebElement> getRows(int number) {
		return WaitUtils.numberOfElementsWait(driver, By.xpath("//ul[@class='timeline']/li"), 10, number);
	}
}
