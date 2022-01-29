package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage {
    private WebDriver driver;

    @FindBy(xpath = "//*[@formControlName=\"username\"]")
    private WebElement emailInputField;

    @FindBy(xpath = "//*[@formControlName=\"password\"]")
    private  WebElement passwordInputField;

    @FindBy(className = "btn")
    private WebElement submitButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }


    public void login(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
        this.getSubmitButton().click();
    }

    public WebElement getEmail() {
        return WaitUtils.visibilityWait(driver, emailInputField, 10);
    }

    public void setEmail(String email) {
        WebElement emailElement = getEmail();
        emailElement.clear();
        emailElement.sendKeys(email);
    }

    public WebElement getPassword() {
        return WaitUtils.visibilityWait(driver, passwordInputField, 10);
    }

    public void setPassword(String password) {
        WebElement passwordElement = getPassword();
        passwordElement.clear();
        passwordElement.sendKeys(password);
    }

    public WebElement getSubmitButton() {
        return WaitUtils.clickableWait(driver, submitButton, 10);
    }



}
