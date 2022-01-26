package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class OrderPage {
    private WebDriver webDriver;

    @FindBy(xpath = "//*[@id=\"chat\"]//tr[2]/td[1]//h5")
    private WebElement menuItemName;

    @FindBy(xpath = "//*[@id=\"chat\"]//tr[2]/td[2]/h4")
    private WebElement menuItemQuantity;

    @FindBy(xpath = "//*[@id=\"chat\"]//tr[2]/td[5]/h4")
    private WebElement menuItemNote;

    @FindBy(xpath = "//*[@id=\"chat\"]//tr[2]/td[3]/h4")
    private WebElement menuItemPrice;


    @FindBy(xpath = "//*[@id=\"chat\"]//tr[2]/td[6]/a")
    private WebElement deleteButton;

    @FindBy(tagName = "table")
    private WebElement table;

    @FindBy(className = "btn-success")
    private WebElement createOrderButton;


    public OrderPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }


    public WebElement getMenuItemName() {
        return WaitUtils.visibilityWait(webDriver, menuItemName, 10);
    }

    public WebElement getMenuItemQuantity() {
        return WaitUtils.visibilityWait(webDriver, menuItemQuantity, 10);
    }

    public WebElement getMenuItemPrice() {
        return WaitUtils.visibilityWait(webDriver, menuItemPrice, 10);
    }

    public WebElement getMenuItemNote() {
        return WaitUtils.visibilityWait(webDriver, menuItemNote, 10);
    }

    public WebElement getDeleteButton() {
        return WaitUtils.visibilityWait(webDriver, deleteButton, 10);
    }

    public void clickDeleteButton() {
        WebElement button = getDeleteButton();
        button.click();
    }

    public WebElement getTable() {
        return WaitUtils.visibilityWait(webDriver, table, 10);
    }

    public List<WebElement> getRows() {
        return getTable().findElements(By.tagName("tr"));
    }

    public WebElement getCreateOrderButton() {
        return WaitUtils.visibilityWait(webDriver, createOrderButton, 10);
    }

    public void clickCreateOrderButton() {
        WebElement button = getCreateOrderButton();
        button.click();
    }


}
