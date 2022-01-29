package com.kti.restaurant.e2e.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.kti.restaurant.e2e.utils.WaitUtils;

public class AcceptOrderItemPage {
	 private WebDriver webDriver;
	 
	 @FindBy(xpath = "//tr[1]/td[2]")
	 private WebElement orderItemName;
	 	
	 @FindBy(tagName = "tbody")
	 private WebElement tableBody;
	 
	 public AcceptOrderItemPage(WebDriver webDriver) {
		 this.webDriver = webDriver; 
	 }
	 
	 public WebElement getTableBody() {
		 return WaitUtils.visibilityWait(webDriver, tableBody, 10);
	 }

	 public List<WebElement> getRows() {
		 List<WebElement> elements = getTableBody().findElements(By.xpath("//tbody/tr"));
	     return elements;
	 }
	 
	 public String getOrderItemNameText() {
		 return WaitUtils.visibilityWait(webDriver, orderItemName, 10).getText();
	 }
	 
	 public void clickAcceptButton(String name) {
		WaitUtils.visibilityWait(webDriver, By.xpath(String.format("//tbody/tr/td[2]//h5[text() = '%s']/ancestor::tr/td[6]//button", name)), 10).get(0).click();
	 }
	 
	 public WebElement getNotificationMessage(String message) {
	    	return WaitUtils.presenceWait(webDriver, By.xpath(String.format("//div[contains(text(), '%s')]", message)), 10);
	 }
}
