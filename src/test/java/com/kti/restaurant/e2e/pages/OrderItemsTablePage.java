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

    public List<WebElement> getRows() {
        return  WaitUtils.visibilityWait(webDriver, By.xpath("//tbody/tr"), 10);
    }

    public WebElement getRow(String status, String id) {
        WebElement element = WaitUtils.visibilityWait(webDriver, By.xpath("//tbody/tr/td[1][contains(text(),'" + id + "')]/.."
                +"/td[7]//button[contains(text(),'" + status + "')]"), 10).get(0);
        return  WaitUtils.clickableWait(webDriver, element, 10);
    }

    public WebElement getNewButton(String id) {
        return WaitUtils.visibilityWait(webDriver, By.id(id), 10).get(0);
    }
}
