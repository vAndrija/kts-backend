package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class MenuItemDetailsPage {
    private WebDriver driver;

    @FindBy(xpath = "//h4")
    private WebElement menuItemName;

    @FindBy(className = "price")
    private WebElement menuItemPrice;

    public MenuItemDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getMenuItemName() {
        return WaitUtils.visibilityWait(driver, menuItemName, 10);
    }

    public WebElement getMenuItemPrice() {
        return WaitUtils.visibilityWait(driver, menuItemPrice, 10);
    }
}
