package com.kti.restaurant.e2e.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.kti.restaurant.e2e.utils.WaitUtils;

public class TableReservationPage {

	private WebDriver driver;
	
	@FindBy(xpath = "//tbody")
	private WebElement tableBody;
	
	@FindBy(xpath = "//button[contains(text(), 'Rezerviši sto')]")
	private WebElement reserveTableButton;
	
	@FindBy(xpath = "//div[@class='modal-body']//button[contains(text(), 'Rezerviši')]")
	private WebElement reserveButton;
	
	@FindBy(xpath = "//div[@class='modal-body']//button[contains(text(), 'Izmeni')]")
	private WebElement changeButton;
	
	@FindBy(xpath = "//button[@class='close']")
	private WebElement closeButton;
	
	@FindBy(xpath = "//div[contains(text(), 'Rezervacija')]")
	private WebElement notificationMessage;
	
	@FindBy(xpath = "//*[@formControlName='name']")
	private WebElement name;

	@FindBy(xpath = "//*[@formControlName='reservationDate']")
	private  WebElement reservationDate;
	
	@FindBy(xpath = "//*[@formControlName='reservationStart']")
	private WebElement reservationStart;
	
	@FindBy(xpath = "//*[@formControlName='reservationEnd']")
	private WebElement reservationEnd;
	
	@FindBy(xpath = "//*[@formControlName='tableId']")
	private Select tableIdSelect;
	
	 
	public TableReservationPage(WebDriver driver) {
		this.driver = driver;
	}
	
	private WebElement getCloseButton() {
        return WaitUtils.visibilityWait(driver, closeButton, 10);
    }
	
	public void clickCloseButton() {
		getCloseButton().click();
	}
	
	public void clickCancelButton(String name) {
		WebElement element = WaitUtils.visibilityWait(driver, By.xpath(String.format("//tbody/tr/td[2]//h5[text() = '%s']/ancestor::tr/td[7]", name)), 10).get(0);
		element.click();
	}
	
	public void clickEdit(String name) {
		WebElement element = WaitUtils.visibilityWait(driver, By.xpath(String.format("//tbody/tr/td[2]//h5[text() = '%s']/ancestor::tr/td[6]", name)), 10).get(0);
		element.click();
	}
	
	private WebElement getReserveTableButton() {
        return WaitUtils.visibilityWait(driver, reserveTableButton, 10);
    }
	
	public void clickReserveTableButton() {
		getReserveTableButton().click();
	}
	
	private WebElement getReserveButton() {
        return WaitUtils.visibilityWait(driver, reserveButton, 10);
    }
	
	public void clickReserveButton() {
		getReserveButton().click();
	}
	
	private WebElement getChangeButton() {
        return WaitUtils.visibilityWait(driver, changeButton, 10);
    }
	
	public void clickChangeButton() {
		getChangeButton().click();
	}
	public boolean isReserveButtonEnabled() {
		return this.reserveButton.isEnabled();
	}
	public boolean isChangeButtonEnabled() {
		return this.changeButton.isEnabled();
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

	public WebElement getNotificationMessage() {
		 return WaitUtils.visibilityWait(driver, notificationMessage, 10);
	}
	
	public List<WebElement> getSelectOption(String option) {
		return WaitUtils.visibilityWait(driver, By.xpath("//option[contains(text(), '" + option + "')]"), 10);
	}
	
	public Select getCategorySelect() {
		return new Select(WaitUtils.visibilityWait(driver, By.xpath("//*[@formControlName='tableId']"), 10).get(0));
	}
	
	public void setCategorySelect(String selectCategoryOption) {
		Select selectCategory = getCategorySelect();
		getSelectOption(selectCategoryOption);
		selectCategory.selectByVisibleText(selectCategoryOption);
	}
	
	public WebElement getName() {
		return WaitUtils.visibilityWait(driver, name, 10);
	}
	
	public void setName(String name) {
		WebElement nameInputField = getName();
		nameInputField.clear();
		nameInputField.sendKeys(name);
	}
	
	public WebElement getReservationDate() {
		return WaitUtils.visibilityWait(driver, this.reservationDate, 10);
	}
	
	public void setReservationDate(String name) {
		WebElement nameInputField = getReservationDate();
		nameInputField.clear();
		nameInputField.sendKeys(name);
	}
	
	public WebElement getReservationStart() {
		return WaitUtils.visibilityWait(driver, this.reservationStart, 10);
	}
	
	public void setReservationStart(String name) {
		WebElement nameInputField = getReservationStart();
		nameInputField.clear();
		nameInputField.sendKeys(name);
	}
	
	public WebElement getReservationEnd() {
		return WaitUtils.visibilityWait(driver, this.reservationEnd, 10);
	}
	
	public void setReservationEnd(String name) {
		WebElement nameInputField = getReservationEnd();
		nameInputField.clear();
		nameInputField.sendKeys(name);
	}
	
	public boolean isRowPresent(String name, String tableId, String reservationStart, String reservationEnd) {
		WebElement element = WaitUtils.visibilityWait(driver, By.xpath(String.format("//tbody/tr/td[2]//h5[text() = '%s']/ancestor::tr", name)), 10).get(0);
		if(!element.findElement(By.xpath("./td[3]//h5")).getText().equalsIgnoreCase(tableId)) {
			return false;
		}
		if(!element.findElement(By.xpath("./td[4]//h5")).getText().equalsIgnoreCase(reservationStart)) {
			return false;
		}
		if(!element.findElement(By.xpath("./td[5]//h5")).getText().equalsIgnoreCase(reservationEnd)) {
			return false;
		}
		return true;
	}
	
	
}
