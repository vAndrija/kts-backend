package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;


public class OrderTablePage {
    private WebDriver webDriver;

    @FindBy(xpath = "//*[@formControlName=\"filterName\"]")
    private Select selectStatus;

    @FindBy(xpath = "//tr[1]/td[4]")
    private WebElement changeInvalidStatusButton;

    @FindBy(xpath = "//*[@id=\"1\"]")
    private WebElement newInvalidStatusButton;

    @FindBy(xpath = "//tr[2]/td[4]")
    private WebElement changeValidStatusButton;

    @FindBy(name = "Završeno")
    private WebElement changedButton;

    @FindBy(xpath = "//*[@id=\"3\"]")
    private WebElement newValidStatusButton;

    @FindBy(tagName = "tbody")
    private WebElement tableBody;

    public OrderTablePage(WebDriver webDriver) {
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

    public WebElement getInvalidStatusButton() {
        return WaitUtils.visibilityWait(webDriver, changeInvalidStatusButton, 10);
    }

    public void clickInvalidStatusButton() {
        WebElement button = getInvalidStatusButton();
        button.click();
    }

    public WebElement getNewInvalidStatusButton() {
        return WaitUtils.visibilityWait(webDriver, newInvalidStatusButton, 10);
    }

    public void clickNewInvalidStatusButton() {
        WebElement button = getNewInvalidStatusButton();
        button.click();
    }


    public WebElement getValidStatusButton() {
        return WaitUtils.visibilityWait(webDriver, changeValidStatusButton, 10);

    }

    public WebElement getChangedButton() {
        return WaitUtils.visibilityWait(webDriver, changedButton, 10);

    }

    public void clickValidStatusButton() {
        WebElement button = getValidStatusButton();
        button.click();
    }

    public WebElement getNewValidStatusButton() {
        return WaitUtils.visibilityWait(webDriver, newValidStatusButton, 10);
    }

    public void clickNewValidStatusButton() {
        WebElement button = getNewValidStatusButton();
        button.click();
    }

    public WebElement getTableBody() {
        return WaitUtils.visibilityWait(webDriver, tableBody, 10);
    }

    public List<WebElement> getRows() {
        return getTableBody().findElements(By.tagName("tr"));
    }
}
