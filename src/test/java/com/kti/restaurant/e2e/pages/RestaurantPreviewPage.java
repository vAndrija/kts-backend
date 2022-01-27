package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RestaurantPreviewPage {

    private WebDriver webDriver;

    @FindBy(className = "konvajs-content")
    private WebElement canvas;

    @FindBy(className = "btn-success")
    private WebElement addButton;

    @FindBy(className = "btn-danger")
    private WebElement deleteButton;

    @FindBy(xpath = "//*[@formControlName=\"quantity\"]")
    private WebElement capacity;

    @FindBy(className = "btn-primary")
    private WebElement addCapacity;

    @FindBy(id = "yes")
    private WebElement yesButton;

    @FindBy(tagName = "p")
    private WebElement message;

    public RestaurantPreviewPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getCanvas() {
        return WaitUtils.visibilityWait(webDriver, canvas, 10);
    }

    public WebElement getAddButton() {
        return WaitUtils.visibilityWait(webDriver, addButton, 10);
    }

    public void clickAddButton() {
        WebElement button = getAddButton();
        button.click();
    }

    public WebElement getDeleteButton() {
        return WaitUtils.visibilityWait(webDriver, deleteButton, 10);
    }

    public void clickDeleteButton() {
        WebElement button = getDeleteButton();
        button.click();
    }

    public WebElement getCapacity() {
        return WaitUtils.visibilityWait(webDriver, capacity, 10);
    }

    public void setCapacity(String capacity) {
        WebElement nameElement = getCapacity();
        nameElement.clear();
        nameElement.sendKeys(capacity);
    }

    public WebElement getAddCapacity() {
        return WaitUtils.visibilityWait(webDriver, addCapacity, 10);
    }

    public void clickAddCapacity() {
        WebElement button = getAddCapacity();
        button.click();
    }

    public WebElement getYesButton() {
        return WaitUtils.visibilityWait(webDriver, yesButton, 10);
    }

    public void clickYesButton() {
        WebElement button = getYesButton();
        button.click();
    }

    public WebElement getMessage() {
        return WaitUtils.visibilityWait(webDriver, message, 10);
    }

}
