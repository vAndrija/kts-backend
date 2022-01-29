package com.kti.restaurant.e2e.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.kti.restaurant.e2e.utils.WaitUtils;

public class MenuReviewPage {

	private WebDriver driver;
	
	@FindBy(xpath = "//tbody")
	private WebElement tableBody;
	
	
	public MenuReviewPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void clickEdit(String name) {
		WebElement element = WaitUtils.visibilityWait(driver, By.xpath(String.format("//tbody/tr/td[2]//h5[text() = '%s']/ancestor::tr/td[5]//button", name)), 10).get(0);
		element.click();
	}

	public WebElement getTableBody() {
        return WaitUtils.visibilityWait(driver, tableBody, 10);
    }
	
	public List<WebElement> getRows() {
        return getTableBody().findElements(By.tagName("tr"));
    }	
	
	public List<WebElement> getLoadedReservations(int number) {
        return WaitUtils.numberOfElementsWait(driver, By.xpath("//tbody"), 10, number);
    }
}
