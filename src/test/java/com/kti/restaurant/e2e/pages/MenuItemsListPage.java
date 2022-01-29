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
    
    @FindBy(xpath = "//a[.='Kreiraj meni']")
    private WebElement addMenuButton;

    @FindBy(xpath = "//*[@id=\"menu\"]/li/div/li[4]/a")
    private WebElement orderButton;
    
    @FindBy(xpath = "//*[@id=\"menu\"]/li/div/li[3]/a")
    private WebElement addNewDishButton;
    
    @FindBy(xpath = "//*[@id=\"menu\"]/li/div/li[5]/a")
    private WebElement acceptOrderItemButton;

    @FindBy(xpath = "//*[@id=\"menu\"]/li/div/li[3]/a")
    private WebElement orderItemsButton;

    @FindBy(xpath = "//a[.='Pregled menija']")
    private WebElement menuReviewButton;
    
    @FindBy(xpath = "//*[@formControlName=\"searchParam\"]")
    private WebElement searchParamInputField;

    @FindBy(xpath = "//*[@formControlName=\"category\"]")
    private WebElement categorySelect;

    @FindBy(className = "btn-sm")
    private WebElement searchButton;

    @FindBy(xpath = "//*[@id=\"menu\"]/li/div/li[2]/a")
    private WebElement restaurantButton;
    
    @FindBy(xpath = "//a[.='Rezervacije']")
    private WebElement reservationsButton;

    @FindBy(xpath = "//*[@href=\"/menu/pending-menu-items\"]")
    private WebElement linkToUnacceptedMenuItemsPage;

    public MenuItemsListPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickOnLinkToUnacceptedMenuItemSPage() {
        WebElement link = WaitUtils.visibilityWait(driver, linkToUnacceptedMenuItemsPage, 10);
        link.click();
    }
    
    public void clickOnLMenuPreviewPage() {
        WebElement link = WaitUtils.visibilityWait(driver, menuReviewButton, 10);
        link.click();
    }

    public void clickLoadMore() {
        WebElement button = getLoadMoreButton();
        button.click();
    }

    public void clickDetailsButton(int indexOfButton) {
        WebElement button = getDetailsButton().get(indexOfButton);
        button.click();
    }

    public void clickSearchButton() {
        WebElement button = getSearchButton();
        button.click();
    }

    public void clickAddMenuButton() {
    	WebElement button = getAddMenuButton();
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
    
    public WebElement getAddMenuButton() {
    	return WaitUtils.visibilityWait(driver, addMenuButton, 10);
    }
  
    public WebElement getAddNewDishButton() {
    	return WaitUtils.visibilityWait(driver, addNewDishButton, 10);
    }
    
    public void clickAddNewDishButton() {
    	WebElement button = getAddNewDishButton();
    	button.click();
    }
  
    public WebElement getAcceptOrderItemButton() {
    	return WaitUtils.visibilityWait(driver, acceptOrderItemButton, 10);
    }
    
    public void clickAcceptOrderButton() {
    	WebElement button = getAcceptOrderItemButton();
    	button.click();
    }
    
    public WebElement getOrderItemsButton() {
        return WaitUtils.visibilityWait(driver, orderItemsButton, 10);
    }

    public void clickOrderItemsButton() {
        WebElement button = getOrderItemsButton();
        button.click();
    }

    public WebElement getSearchParamInputField() {
        return WaitUtils.visibilityWait(driver, searchParamInputField, 10);
    }

    public void setSearchParamInputField(String searchParam) {
        WebElement searchInputField = getSearchParamInputField();
        searchInputField.clear();
        searchInputField.sendKeys(searchParam);
    }

    public Select getCategorySelect() {
        return new Select(WaitUtils.visibilityWait(driver, By.xpath("//*[@formControlName=\"category\"]"), 10).get(0));
    }

    public void setCategorySelect(String category) {
        Select selectMenu = getCategorySelect();
        getSelectOption(category);
        selectMenu.selectByVisibleText(category);
    }

    public WebElement getSearchButton() {
        return WaitUtils.clickableWait(driver, searchButton, 10);
    }

    public WebElement getRestaurantButton() {
        return WaitUtils.visibilityWait(driver, restaurantButton, 10);
    }

    public void clickRestaurantButton() {
        WebElement button = getRestaurantButton();
        button.click();
    }
    
    private WebElement getReservationsButton() {
        return WaitUtils.visibilityWait(driver, reservationsButton, 10);
    }

    public void clickReservationButton() {
        WebElement button = getReservationsButton();
        button.click();
    }
}
