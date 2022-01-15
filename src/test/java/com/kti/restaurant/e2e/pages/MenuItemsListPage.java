package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class MenuItemsListPage {
    private WebDriver driver;

    @FindBy(xpath = "//*[@formControlName=\"menuId\"]")
    private Select selectMenu;

    @FindBy(className = "btn-primary")
    private WebElement loadMoreButton;

    @FindBy(className = "card-body")
    private List<WebElement> loadedMenuItems;

    @FindBy(className = "btn")
    private WebElement detailsButton;

    public MenuItemsListPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickLoadMore() {
        WebElement button = getLoadMoreButton();
        button.click();
    }

    public void clickDetailsButton(int indexOfButton) {
        WebElement button = getDetailsButton().get(indexOfButton);
        button.click();
    }

    public List<WebElement> getSelectOption(String option) {
        return WaitUtils.visibilityWait(driver, By.xpath("//option[contains(text(), '" + option + "')]"), 10);
    }

    public Select getSelectMenu() {
        return new Select(WaitUtils.visibilityWait(driver, By.xpath("//*[@formControlName=\"menuId\"]"), 10).get(0));
    }

    public void setSelectMenu(String selectMenuOption) {
        Select selectMenu = getSelectMenu();
        getSelectOption(selectMenuOption);
        selectMenu.selectByVisibleText(selectMenuOption);
    }

    public WebElement getLoadMoreButton() {
        return WaitUtils.visibilityWait(driver, loadMoreButton, 10);
    }

    public List<WebElement> getLoadedMenuItems(int number) {
        return WaitUtils.numberOfElementsWait(driver, By.className("card-body"), 10, number);
    }

    public List<WebElement> getDetailsButton() {
        return WaitUtils.visibilityWait(driver, By.xpath("//button[contains(text(), 'Opširnije')]"), 10);
    }
}
