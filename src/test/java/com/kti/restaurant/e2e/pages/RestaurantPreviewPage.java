package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RestaurantPreviewPage {

    private WebDriver webDriver;

    @FindBy(className = "konvajs-content")
    private WebElement canvas;


    public RestaurantPreviewPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getCanvas() {
        return WaitUtils.visibilityWait(webDriver, canvas, 10);
    }

}
