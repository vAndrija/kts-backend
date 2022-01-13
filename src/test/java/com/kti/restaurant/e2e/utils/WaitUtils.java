package com.kti.restaurant.e2e.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class WaitUtils {

	public static boolean urlWait(WebDriver driver, String url, int wait) {
		return new WebDriverWait(driver, wait).until(ExpectedConditions.urlToBe(url));
	}

	public static boolean titleWait(WebDriver driver, String title, int wait) {
		return new WebDriverWait(driver, wait).until(ExpectedConditions.titleIs(title));
	}

	public static WebElement visibilityWait(WebDriver driver, WebElement element, int wait) {
		return new WebDriverWait(driver, wait).until(ExpectedConditions.visibilityOf(element));
	}
	
	public static List<WebElement> visibilityWait(WebDriver driver, By locator, int wait) {
		return new WebDriverWait(driver, wait).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	public static WebElement presenceWait(WebDriver driver, By locator, int wait) {
		return new WebDriverWait(driver, wait).until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public static List<WebElement> presenceWaitAll(WebDriver driver, By locator, int wait) {
		return new WebDriverWait(driver, wait).until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}

	public static WebElement clickableWait(WebDriver driver, WebElement element, int wait) {
		return new WebDriverWait(driver, wait).until(ExpectedConditions.elementToBeClickable(element));
	}

	public static List<WebElement> numberOfElementsWait(WebDriver driver, By locator, int wait, int number) {
		return new WebDriverWait(driver, wait).until(ExpectedConditions.numberOfElementsToBe(locator, number));
	}

	public static boolean isPresent(WebDriver driver, By locator) {

		try {
			return driver.findElement(locator).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
