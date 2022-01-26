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

    @FindBy(xpath = "//*[@id=\"menu\"]/li/div/li[3]/a")
    private WebElement orderButton;
    
    @FindBy(xpath = "//a[.='Kreiraj stavku menija']")
    private WebElement addNewDishButton;

    @FindBy(xpath = "//*[@id=\"menu\"]/li/div/li[3]/a")
    private WebElement orderItemsButton;

    @FindBy(xpath = "//*[@id=\"menu\"]/li/div/li[2]/a")
    private WebElement restaurantButton;


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

    public WebElement getOrderButton() {
        return WaitUtils.visibilityWait(driver, orderButton, 10);
    }

    public void clickOrderButton() {
        WebElement button = getOrderButton();
        button.click();
    }
    
    public WebElement getAddNewDishButton() {
    	return WaitUtils.visibilityWait(driver, addNewDishButton, 10);
    }
    
    public void clickAddNewDishButton() {
    	WebElement button = getAddNewDishButton();
    	button.click();
    }
  
    public WebElement getOrderItemsButton() {
        return WaitUtils.visibilityWait(driver, orderItemsButton, 10);
    }

    public void clickOrderItemsButton() {
        WebElement button = getOrderItemsButton();
        button.click();
    }

    public WebElement getRestaurantButton() {
        return WaitUtils.visibilityWait(driver, restaurantButton, 10);
    }

    public void clickRestaurantButton() {
        WebElement button = getRestaurantButton();
        button.click();
    }
}
