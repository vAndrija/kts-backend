package com.kti.restaurant.e2e.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.kti.restaurant.e2e.utils.WaitUtils;

public class AcceptOrderItemPage {
	 private WebDriver webDriver;
	 
	 @FindBy(xpath = "//div[contains(text(), 'Stavka')]")
	 private WebElement notificationMessage;
	 
	 @FindBy(xpath = "//tr[1]/td[6]")
	 private WebElement acceptOrderItemButton;
	 
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
	 
	 public WebElement getAcceptButton() {
	     return WaitUtils.visibilityWait(webDriver, acceptOrderItemButton, 10);
	 }
	 
	 public void clickAcceptButton() {
	     WebElement button = getAcceptButton();
	     button.click();
	 }
	 
	 public WebElement getNotificationMessage() {
		 return WaitUtils.visibilityWait(webDriver, notificationMessage, 10);
	 }
}
