package com.kti.restaurant.e2e.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.kti.restaurant.e2e.utils.WaitUtils;

public class NewDishDrinkPage {
	private WebDriver driver;
	
	@FindBy(xpath = "//*[@formControlName='name']")
	private WebElement nameInputField;
	
	@FindBy(xpath = "//*[@formControlName='description']")
	private WebElement descriptionInputField;
	
	@FindBy(xpath = "//*[@formControlName='preparationTime']")
	private WebElement preparationTimeInputField;
	
	@FindBy(xpath = "//*[@formControlName='category']")
	private Select categorySelect;
	
	@FindBy(xpath = "//div[contains(text(), 'Stavka menija')]")
    private WebElement notificationMessage;

	@FindBy(xpath = "//button[contains(text(), 'Dodaj')]")
	private WebElement submitButton;
	
	public NewDishDrinkPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void clickSubmitButton() {
		WebElement button = getSubmitButton();
		button.click();
	}
	
	public WebElement getNameInputField() {
		return WaitUtils.visibilityWait(driver, nameInputField, 10);
	}
	
	public void setNameInputField(String name) {
		WebElement nameInputField = getNameInputField();
		nameInputField.clear();
		nameInputField.sendKeys(name);
	}
	
	public WebElement getDescriptionInputField() {
		return WaitUtils.visibilityWait(driver, descriptionInputField, 10);
	}
	
	public void setDescriptionInputField(String description) {
		WebElement descriptionInputField = getDescriptionInputField();
		descriptionInputField.clear();
		descriptionInputField.sendKeys(description);
	}
	
	public WebElement getPreparationTimeInputField() {
		return WaitUtils.visibilityWait(driver, preparationTimeInputField, 10);
	}
	
	public void setPreparationTimeInputField(String time) {
		WebElement preparationTimeInputField = getPreparationTimeInputField();
		preparationTimeInputField.clear();
		preparationTimeInputField.sendKeys(time);
	}
	
	public List<WebElement> getSelectOption(String option) {
		return WaitUtils.visibilityWait(driver, By.xpath("//option[contains(text(), '" + option + "')]"), 10);
	}
	
	public Select getCategorySelect() {
		return new Select(WaitUtils.visibilityWait(driver, By.xpath("//*[@formControlName='category']"), 10).get(0));
	}
	
	public void setCategorySelect(String selectCategoryOption) {
		Select selectCategory = getCategorySelect();
		getSelectOption(selectCategoryOption);
		selectCategory.selectByVisibleText(selectCategoryOption);
	}
	
	public WebElement getNotificationMessage() {
    	return WaitUtils.visibilityWait(driver, notificationMessage, 10);
    }
	
	public WebElement getSubmitButton() {
		return WaitUtils.visibilityWait(driver, submitButton, 10);
	}
	
	public boolean isSubmitButtonEnabled() {
		return submitButton.isEnabled();
	}
	
	
	
	
	
	
	
	
	
	
}
