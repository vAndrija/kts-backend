package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ResetPasswordPage {
    private WebDriver driver;

    @FindBy(xpath = "//a[@href=\"/auth/password-reset-start\"]")
    private WebElement resetPasswordLink;

    @FindBy(xpath = "//*[@formControlName=\"email\"]")
    private WebElement emailInputFiled;

    @FindBy(xpath = "//*[contains(text(),'Link za resetovanje je poslat na vašu email adresu')]")
    private WebElement successNotification;

    @FindBy(className = "btn")
    private WebElement submitButton;


    public ResetPasswordPage(WebDriver driver) {
        this.driver = driver;
    }


    public void clickToResetPasswordLink() {
        this.getResetPasswordLink().click();

    }

    public void resetPassword(String email){
        this.setEmail(email);
        this.getSubmitButton().click();
        this.getSuccessNotification();
    }

    public WebElement getResetPasswordLink() {
        return WaitUtils.visibilityWait(driver, resetPasswordLink, 10);
    }

    public WebElement getEmail() {

        return WaitUtils.visibilityWait(driver, emailInputFiled, 10);
    }

    public WebElement getSuccessNotification() {
        return WaitUtils.visibilityWait(driver,successNotification,10);
    }

    public void setEmail(String email) {
        WebElement emailElement = getEmail();
        emailElement.clear();
        emailElement.sendKeys(email);
    }

    public WebElement getSubmitButton() {
        return WaitUtils.clickableWait(driver, submitButton, 10);
    }

}
