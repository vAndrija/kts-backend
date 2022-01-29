package com.kti.restaurant.e2e.pages;


import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class OrderItemsPage {
    private WebDriver webDriver;

    @FindBy(id = "Glavno jelo")
    private WebElement categoryButton;

    @FindBy(id = "load")
    private WebElement loadMoreButton;

    @FindBy(className = "order-search")
    private WebElement inputSearch;

    @FindBy(className = "btn-primary")
    private WebElement buttonSearch;

    @FindBy(id = "Sve")
    private WebElement allCategoryButton;

    @FindBy(xpath = "//app-order-item-card[1]//div[1]/input")
    private WebElement quantity;

    @FindBy(xpath = "//app-order-item-card[1]//div[2]/input")
    private WebElement priority;

    @FindBy(xpath = "//app-order-item-card[1]//div[3]/input")
    private WebElement note;

    @FindBy(xpath = "//app-order-item-card[1]//div[4]/button")
    private WebElement addButton;

    @FindBy(xpath = "//app-order-item-card[3]//div[1]/input")
    private WebElement quantitySecond;

    @FindBy(xpath = "//app-order-item-card[3]//div[2]/input")
    private WebElement prioritySecond;

    @FindBy(xpath = "//app-order-item-card[3]//div[4]/button")
    private WebElement addButtonSecond;

    @FindBy(id = "click-order")
    private WebElement orderButton;

    public OrderItemsPage(WebDriver webDriver){
        this.webDriver = webDriver;
    }

    public WebElement getCategoryButton() {
        return WaitUtils.visibilityWait(webDriver, categoryButton, 10);
    }

    public void clickCategoryButton() {
        WebElement button = getCategoryButton();
        button.click();
    }

    public List<WebElement> getMenuItemsByCategory(int number) {
        return WaitUtils.numberOfElementsWait(webDriver, By.tagName("app-order-item-card"), 10, number);
    }

    public WebElement getLoadMoreButton() {
        return WaitUtils.visibilityWait(webDriver, loadMoreButton, 10);
    }

    public void clickLoadMoreButton() {
        WebElement button = getLoadMoreButton();
        button.click();
    }

    public WebElement getInputSearch() {
        return WaitUtils.visibilityWait(webDriver, inputSearch, 10);
    }

    public void setInputSearch(String search) {
        WebElement element = getInputSearch();
        element.clear();
        element.sendKeys(search);
    }

    public WebElement getAllCategoryButton() {
        return WaitUtils.visibilityWait(webDriver, allCategoryButton, 10);
    }

    public void clickAllCategoryButton() {
        WebElement button = getAllCategoryButton();
        button.click();
    }

    public WebElement getButtonSearch() {
        return WaitUtils.visibilityWait(webDriver, buttonSearch, 10);
    }

    public void clickButtonSearch() {
        WebElement button = getButtonSearch();
        button.click();
    }

    public WebElement getQuantity() {
        return WaitUtils.visibilityWait(webDriver, quantity, 10);
    }

    public void setQuantity(String quantity) {
        WebElement element = getQuantity();
        element.clear();
        element.sendKeys(quantity);
    }


    public WebElement getPriority() {
        return WaitUtils.visibilityWait(webDriver, priority, 10);
    }

    public void setPriority(String priority) {
        WebElement element = getPriority();
        element.clear();
        element.sendKeys(priority);
    }

    public WebElement getNote() {
        return WaitUtils.visibilityWait(webDriver, note, 10);
    }

    public void setNote(String note) {
        WebElement element = getNote();
        element.clear();
        element.sendKeys(note);
    }

    public WebElement getAddButton() {
        return WaitUtils.visibilityWait(webDriver, addButton, 10);
    }

    public void clickAddButton() {
        WebElement button = getAddButton();
        button.click();
    }

    public WebElement getOrderButton() {
        return WaitUtils.visibilityWait(webDriver, orderButton, 10);
    }

    public void clickOrderButton() {
        WebElement button = getOrderButton();
        button.click();
    }

    public WebElement getQuantitySecond() {
        return WaitUtils.visibilityWait(webDriver, quantitySecond, 10);
    }

    public void setQuantitySecond(String quantity) {
        WebElement element = getQuantitySecond();
        element.clear();
        element.sendKeys(quantity);
    }


    public WebElement getPrioritySecond() {
        return WaitUtils.visibilityWait(webDriver, prioritySecond, 10);
    }

    public void setPrioritySecond(String priority) {
        WebElement element = getPrioritySecond();
        element.clear();
        element.sendKeys(priority);
    }


    public WebElement getAddButtonSecond() {
        return WaitUtils.visibilityWait(webDriver, addButtonSecond, 10);
    }

    public void clickAddButtonSecond() {
        WebElement button = getAddButtonSecond();
        button.click();
    }


//    public WebElement findByName(String name ){
//        return WaitUtils.visibilityWait(webDriver, By.xpath("//div[@class='new-arrival-content order-item-card-na']/h4[contains(text(),'"+name+"')]/.."), 10).get(0);
//    }


}
