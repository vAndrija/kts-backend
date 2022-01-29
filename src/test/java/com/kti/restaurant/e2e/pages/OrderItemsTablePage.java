package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class OrderItemsTablePage {
    private WebDriver webDriver;

    @FindBy(xpath = "//*[@formControlName=\"filterName\"]")
    private Select selectStatus;
//
//    @FindBy(xpath = "//tr[1]/td[7]")
//    private WebElement changeStatusButton;
//
//    @FindBy(xpath = "//*[@id=\"6\"]")
//    private WebElement newStatusButton;
//
//    @FindBy(name = "Pripremljeno")
//    private WebElement changedButton;
//
//    @FindBy(tagName = "tbody")
//    private WebElement tableBody;


    public OrderItemsTablePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public Select getSelectStatus() {
        return new Select(WaitUtils.visibilityWait(webDriver, By.xpath("//*[@formControlName=\"filterName\"]"), 10).get(0));
    }

    public void getSelectOption(String option) {
        WaitUtils.visibilityWait(webDriver, By.xpath("//option[contains(text(), '" + option + "')]"), 10);
    }

    public void setSelectStatus(String selectStatusOption) {
        Select selectStatus = getSelectStatus();
        getSelectOption(selectStatusOption);
        selectStatus.selectByVisibleText(selectStatusOption);
    }


//    public WebElement getStatusButton() {
//        return WaitUtils.visibilityWait(webDriver, changeStatusButton, 10);
//    }
//
//    public void clickStatusButton() {
//        WebElement button = getStatusButton();
//        button.click();
//    }
//
//    public WebElement getNewStatusButton() {
//        return WaitUtils.visibilityWait(webDriver, newStatusButton, 10);
//    }
//
//    public void clickNewStatusButton() {
//        WebElement button = getNewStatusButton();
//        button.click();
//    }
//
//    public WebElement getChangedButton() {
//        return WaitUtils.visibilityWait(webDriver, changedButton, 10);
//
//    }
//
//    public WebElement getTableBody() {
//        return WaitUtils.visibilityWait(webDriver, tableBody, 10);
//    }

    public List<WebElement> getRows() {
        return  WaitUtils.visibilityWait(webDriver, By.xpath("//tbody/tr"), 10);
    }

    public WebElement getRow(String status, String id) {
        return  WaitUtils.visibilityWait(webDriver, By.xpath("//tbody/tr/td[1][contains(text(),'" + id + "')]/.."
                +"/td[7]//button[contains(text(),'" + status + "')]"), 10).get(0);
    }

    public WebElement getNewButton(String id) {
        return WaitUtils.visibilityWait(webDriver, By.id(id), 10).get(0);
    }
}
