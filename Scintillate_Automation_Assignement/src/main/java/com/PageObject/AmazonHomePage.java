package com.PageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AmazonHomePage {
	// WedDriver invoking
	WebDriver driver;

	// Giving life to driver by Constracor
	public AmazonHomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

// **************************************************************************************************************************************
	// Storing Elements By FindBy anotation
	@FindBy(xpath = "//a[@aria-label='Amazon.in']")
	WebElement AmazonLogo;
	
	@FindBy(id = "twotabsearchtextbox")
	WebElement SearchTextfield;
	
	@FindBy(xpath = "//input[@type='submit']")
	WebElement SearchIcon;

	// User Defined Method To Above Elements
	public WebElement AmazonLogo() {
		return AmazonLogo;
	}
	
	public WebElement SearchTextfield() {
		return SearchTextfield;
	}
	
	public WebElement SearchIcon() {
		return SearchIcon;
	}
}
