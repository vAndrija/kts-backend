package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class MenuItemDetailsPage {
    private WebDriver driver;

    @FindBy(xpath = "//h4")
    private WebElement menuItemName;

    @FindBy(className = "price")
    private WebElement menuItemPrice;

    @FindBy(xpath = "//*[@formControlName=\"price\"]")
    private WebElement priceInputField;

    @FindBy(xpath = "//*[@formControlName=\"preparationPrice\"]")
    private WebElement preparationPriceInputField;

    @FindBy(xpath = "//*[@formControlName=\"menuId\"]")
    private Select selectMenu;

    @FindBy(xpath = "//*[@type=\"submit\"]")
    private WebElement acceptButton;

    @FindBy(xpath = "//*[@type=\"button\"]")
    private WebElement declineButton;

    @FindBy(xpath = "//*[@href=\"/menu/pending-menu-items\"]")
    private WebElement linkToUnacceptedMenuItemsPage;

    public MenuItemDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickAcceptButton() {
        WebElement button = getAcceptButton();
        button.click();
    }

    public void clickDeclineButton() {
        WebElement button = getDeclineButton();
        button.click();
    }

    public void clickOnLinkToUnacceptedMenuItemSPage() {
        WebElement link = WaitUtils.visibilityWait(driver, linkToUnacceptedMenuItemsPage, 10);
        link.click();
    }

    public List<WebElement> getSelectOption(String option) {
        return WaitUtils.visibilityWait(driver, By.xpath("//option[contains(text(), '" + option + "')]"), 10);
    }

    public WebElement getMenuItemName() {
        return WaitUtils.visibilityWait(driver, menuItemName, 10);
    }

    public WebElement getPriceInputField() {
        return WaitUtils.visibilityWait(driver, priceInputField, 10);
    }

    public void setPriceInputField(String price) {
        WebElement priceInputField = getPriceInputField();
        priceInputField.clear();
        priceInputField.sendKeys(price);
    }

    public WebElement getPreparationPriceInputField() {
        return WaitUtils.visibilityWait(driver, preparationPriceInputField, 10);
    }

    public void setPreparationPriceInputField(String preparationPrice) {
        WebElement preparationPriceItemFiled = getPreparationPriceInputField();
        preparationPriceItemFiled.clear();
        preparationPriceItemFiled.sendKeys(preparationPrice);
    }

    public Select getSelectMenu() {
        return new Select(WaitUtils.visibilityWait(driver, By.xpath("//*[@formControlName=\"menuId\"]"), 10).get(0));
    }

    public void setSelectMenu(String selectMenuOption) {
        Select selectMenu = getSelectMenu();
        getSelectOption(selectMenuOption);
        selectMenu.selectByVisibleText(selectMenuOption);
    }

    public WebElement getAcceptButton() {
        return WaitUtils.visibilityWait(driver, acceptButton, 10);
    }

    public WebElement getDeclineButton() {
        return WaitUtils.visibilityWait(driver, declineButton, 10);
    }

    public WebElement getMenuItemPrice() {
        return WaitUtils.visibilityWait(driver, menuItemPrice, 10);
    }
}
