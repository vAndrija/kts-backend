package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UpdateProfileInfoPage {
    private WebDriver webDriver;

    @FindBy(xpath = "//a[@class='nav-link']")
    private WebElement navBarLink;

    @FindBy(xpath = "(//*[contains(@class,'nav-item dropdown header-profile')])/*[2]/*[1]")
    private WebElement navProfileLink;

    @FindBy(xpath = "//input[@formcontrolname='name']")
    private WebElement name;

    @FindBy(xpath = "//input[@formcontrolname='accountNumber']")
    private WebElement accountNumber;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//*[contains(text(),'Uspješno ažurirane lične informacije.')]")
    private WebElement successNotification;


    public UpdateProfileInfoPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void openProfilePage() {
        getNavBarLink().click();
        getNavProfileLink().click();
        System.out.println("Ozvrsep");
    }

    public void submit() {
        getSubmitButton().click();
        getSuccessNotification();
    }

    public WebElement getNavBarLink() {
        return WaitUtils.clickableWait(webDriver, navBarLink, 10);
    }

    public WebElement getNavProfileLink() {
        return WaitUtils.clickableWait(webDriver, navProfileLink, 10);
    }

    public WebElement getSubmitButton() {
        return WaitUtils.clickableWait(webDriver, submitButton, 10);
    }


    public WebElement getAccountNumber() {
        return WaitUtils.visibilityWait(webDriver, accountNumber, 10);
    }

    public void setAccountNumber(String capacity) {
        WebElement nameElement = getAccountNumber();
        nameElement.clear();
        nameElement.sendKeys(capacity);
    }

    public WebElement getSuccessNotification() {
        return WaitUtils.visibilityWait(webDriver, successNotification, 10);
    }

    public WebElement getName() {
        return WaitUtils.visibilityWait(webDriver, name, 10);
    }

    public void setName(String capacity) {
        WebElement nameElement = getName();
        nameElement.clear();
        nameElement.sendKeys(capacity);
    }
}
