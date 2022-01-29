package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ChangePasswordPage {
    private WebDriver webDriver;


    @FindBy(xpath = "//a[@class='nav-link']")
    private WebElement navBarLink;

    @FindBy(xpath = "//*[contains(text(),'Promjena')]/..")
    private WebElement changePasswordNavLink;

    @FindBy(xpath = "//*[contains(text(),'Odjavi')]/..")
    private WebElement logoutLink;

    @FindBy(xpath = "//input[@formcontrolname='password']")
    private WebElement password;

    @FindBy(xpath = "//input[@formcontrolname='oldPassword']")
    private WebElement oldPassword;

    @FindBy(xpath = "//input[@formcontrolname='repeatedPassword']")
    private WebElement repeatedPassword;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;


    @FindBy(xpath = "//*[contains(text(),'Uspješno promjenjena lozinka.')]")
    private WebElement successNotification;

    public ChangePasswordPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void openPage() {
        this.getNavBarLink().click();
        this.getChangePasswordNavLink().click();
    }

    public void logout(){
        this.getNavBarLink().click();
        this.getLogoutLink().click();
    }

    public boolean isButtonEnabled() {
        return this.submitButton.isEnabled();
    }

    public void changePassword(){
        this.getSubmitButton().click();
    }


    public WebElement getNavBarLink() {
        return WaitUtils.clickableWait(webDriver, navBarLink, 10);
    }

    public WebElement getLogoutLink() {
        return WaitUtils.clickableWait(webDriver, logoutLink, 10);
    }

    public WebElement getChangePasswordNavLink() {
        return WaitUtils.clickableWait(webDriver, changePasswordNavLink, 10);
    }

    public WebElement getSubmitButton() {
        return WaitUtils.clickableWait(webDriver, submitButton, 10);
    }

    public WebElement getOldPassword() {
        return WaitUtils.visibilityWait(webDriver, oldPassword, 10);
    }

    public void setOldPassword(String capacity) {
        WebElement nameElement = getOldPassword();
        nameElement.clear();
        nameElement.sendKeys(capacity);
    }

    public WebElement getPassword() {
        return WaitUtils.visibilityWait(webDriver, password, 10);
    }

    public void setPassword(String capacity) {
        WebElement nameElement = getPassword();
        nameElement.clear();
        nameElement.sendKeys(capacity);
    }


    public WebElement getRepeatedPassword() {
        return WaitUtils.visibilityWait(webDriver, repeatedPassword, 10);
    }

    public void setRepeatedPassword(String capacity) {
        WebElement nameElement = getRepeatedPassword();
        nameElement.clear();
        nameElement.sendKeys(capacity);
    }


    public WebElement getSuccessNotification() {
        return WaitUtils.visibilityWait(webDriver, successNotification, 10);
    }
}
