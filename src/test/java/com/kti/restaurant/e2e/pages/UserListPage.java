package com.kti.restaurant.e2e.pages;

import com.kti.restaurant.e2e.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UserListPage {
    private WebDriver driver;

    @FindBy(xpath = "//a[@href=\"/users\"]")
    private WebElement usersNavLink;

    @FindBy(xpath = "//*[contains(text(),'kristinamisic@gmail.com')]/../../*[last()]")
    private  WebElement userOpenMenu;

    @FindBy(xpath = "//*[@class='dropdown-menu dropdown-menu-right border py-0 show']/*/*")
    private WebElement menuActionLink;

    @FindBy(xpath = "//*[contains(text(),'Korisnik je trenutno zadužen za određeni posao.')]")
    private WebElement successNotification;


    public UserListPage(WebDriver driver) {
        this.driver = driver;
    }


    public void goToUsersPage(){
        this.getUsersNavLink().click();
    }

    public void deleteUser(){
        this.getUserOpenMenu().click();
        this.getMenuActionLink().click();
        this.getSuccessNotification();
    }


    public void updateUser(){
        this.getUserOpenMenu().click();
        this.getMenuActionLink().click();
    }

    public WebElement getUsersNavLink() {
        return WaitUtils.clickableWait(driver, usersNavLink, 10);
    }


    public WebElement getUserOpenMenu() {
        return WaitUtils.clickableWait(driver, userOpenMenu, 10);
    }

    public WebElement getMenuActionLink() {
        return WaitUtils.clickableWait(driver, menuActionLink, 10);
    }

    public WebElement getSuccessNotification(){ return WaitUtils.visibilityWait(driver,successNotification,10);}

}
