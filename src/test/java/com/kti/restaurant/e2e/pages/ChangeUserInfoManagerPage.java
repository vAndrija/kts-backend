package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ChangeUserInfoManagerPage {
    private WebDriver webDriver;


    @FindBy(xpath = "//input[@formcontrolname='value']")
    private WebElement formSalary;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//*[contains(text(),'Uspješno ažurirana plata radnika.')]")
    private WebElement successNotification;

    public ChangeUserInfoManagerPage(WebDriver webDriver){
        this.webDriver  =  webDriver;
    }



    public void updateSalary(int salary){
        this.setFormSalary(Integer.toString(salary));
        this.getSubmitButton();
        this.getSuccessNotification();
    }

    public int getSalaryValue(){
        return Integer.parseInt(this.getFormSalary().getText());
    }



    public WebElement getFormSalary() {
        return WaitUtils.visibilityWait(webDriver, formSalary, 10);
    }

    public void setFormSalary(String capacity) {
        WebElement nameElement = getFormSalary();
        nameElement.clear();
        nameElement.sendKeys(capacity);
    }

    public WebElement getSubmitButton() {
        return WaitUtils.clickableWait(webDriver, submitButton, 10);
    }

    public WebElement getSuccessNotification(){ return WaitUtils.visibilityWait(webDriver,submitButton,10);}
}
