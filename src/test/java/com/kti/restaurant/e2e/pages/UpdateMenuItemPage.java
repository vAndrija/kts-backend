package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class UpdateMenuItemPage {
    private WebDriver driver;

    @FindBy(xpath = "//*[@formControlName=\"name\"]")
    private WebElement nameInputField;

    @FindBy(xpath = "//*[@formControlName=\"description\"]")
    private WebElement descriptionInputField;

    @FindBy(xpath = "//*[@formControlName=\"preparationTime\"]")
    private WebElement preparationTimeInputField;

    @FindBy(xpath = "//*[@formControlName=\"price\"]")
    private WebElement priceInputField;

    @FindBy(xpath = "//*[@formControlName=\"preparationPrice\"]")
    private WebElement preparationPriceInputField;

    @FindBy(xpath = "//*[@formControlName=\"menuId\"]")
    private Select selectMenu;

    @FindBy(xpath = "//*[@type=\"submit\"]")
    private WebElement acceptButton;

    @FindBy(id = "decline")
    private WebElement declineButton;

    @FindBy(id = "delete")
    private WebElement deleteButton;

    @FindBy(xpath = "//div[contains(text(), 'Uspešno ste sačuvali promene')]")
    private WebElement notificationMessage;

    public UpdateMenuItemPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickAcceptButton() {
        WebElement button = getAcceptButton();
        button.click();
    }

    public void clickDeclineButton() {
        WebElement button = getDeclineButton();
        button.click();
    }

    public void clickDeleteButton() {
        WebElement button = getDeleteButton();
        button.click();
    }

    public List<WebElement> getSelectOption(String option) {
        return WaitUtils.visibilityWait(driver, By.xpath("//option[contains(text(), '" + option + "')]"), 10);
    }

    public WebElement getPriceInputField() {
        return WaitUtils.visibilityWait(driver, priceInputField, 10);
    }

    public void setPriceInputField(String price) {
        WebElement priceInputField = getPriceInputField();
        priceInputField.clear();
        priceInputField.sendKeys(price);
    }

    public WebElement getPreparationPriceInputField() {
        return WaitUtils.visibilityWait(driver, preparationPriceInputField, 10);
    }

    public void setPreparationPriceInputField(String preparationPrice) {
        WebElement preparationPriceItemFiled = getPreparationPriceInputField();
        preparationPriceItemFiled.clear();
        preparationPriceItemFiled.sendKeys(preparationPrice);
    }

    public Select getSelectMenu() {
        return new Select(WaitUtils.visibilityWait(driver, By.xpath("//*[@formControlName=\"menuId\"]"), 10).get(0));
    }

    public void setSelectMenu(String selectMenuOption) {
        Select selectMenu = getSelectMenu();
        getSelectOption(selectMenuOption);
        selectMenu.selectByVisibleText(selectMenuOption);
    }

    public WebElement getAcceptButton() {
        return WaitUtils.visibilityWait(driver, acceptButton, 10);
    }

    public WebElement getDeclineButton() {
        return WaitUtils.visibilityWait(driver, declineButton, 10);
    }

    public WebElement getNameInputField() {
        return WaitUtils.visibilityWait(driver, nameInputField, 10);
    }

    public WebElement getDescriptionInputField() {
        return WaitUtils.visibilityWait(driver, descriptionInputField, 10);
    }

    public WebElement getPreparationTimeInputField() {
        return WaitUtils.visibilityWait(driver, preparationTimeInputField, 10);
    }

    public WebElement getDeleteButton() {
        return WaitUtils.clickableWait(driver, deleteButton, 10);
    }

    public void setNameInputField(String name) {
        WebElement inputField = getNameInputField();
        inputField.clear();
        inputField.sendKeys(name);
    }

    public void setDescriptionInputField(String description) {
        WebElement inputField = getDescriptionInputField();
        inputField.clear();
        inputField.sendKeys(description);
    }

    public void setPreparationTimeInputField(String preparationTime) {
        WebElement inputField = getPreparationTimeInputField();
        inputField.clear();
        inputField.sendKeys(preparationTime);
    }

    public WebElement getNotificationMessage() {
        return WaitUtils.visibilityWait(driver, notificationMessage, 10);
    }
}
