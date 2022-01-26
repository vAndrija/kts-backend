package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;


public class OrderReviewPage {
    private WebDriver webDriver;

    @FindBy(xpath = "//table//tr[2]/td[6]/a")
    private WebElement deleteButton;

    @FindBy(className = "btn-success")
    private WebElement createOrderButton;

    public OrderReviewPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<WebElement> getRows(int number) {
        return WaitUtils.numberOfElementsWait(webDriver, By.tagName("tr"), 10, number);
    }

    public WebElement getDeleteButton() {
        return WaitUtils.visibilityWait(webDriver, deleteButton, 10);
    }

    public void clickDeleteButton() {
        WebElement button = getDeleteButton();
        button.click();
    }

    public WebElement getCreateOrderButton() {
        return WaitUtils.visibilityWait(webDriver, createOrderButton, 10);
    }

    public void clickCreateOrderButton() {
        WebElement button = getCreateOrderButton();
        button.click();
    }
}
