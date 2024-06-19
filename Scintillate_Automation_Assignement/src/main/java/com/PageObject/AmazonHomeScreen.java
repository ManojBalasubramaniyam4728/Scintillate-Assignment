package com.PageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class AmazonHomeScreen {
	AndroidDriver androidDriver;

	// Giving life to driver by Constracor
	public AmazonHomeScreen(AndroidDriver androidDriver) {
		this.androidDriver=androidDriver;
		PageFactory.initElements(new AppiumFieldDecorator(androidDriver), this);
	}

// **************************************************************************************************************************************

	// Locaters and Storing In Variable
	@AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='in.amazon.mShop.android.shopping:id/choose_language']")
	WebElement ChooseYourLanguage;

	@AndroidFindBy(xpath = "//android.widget.Button[contains(@text,'Continue')]")
	WebElement ContinueButton;

	@AndroidFindBy(xpath = "//android.widget.Button[@text='Skip sign in']")
	WebElement SkipSignIn;

	@AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='in.amazon.mShop.android.shopping:id/chrome_search_hint_view']")
	WebElement SearchTextField;

	@AndroidFindBy(xpath = "//android.widget.ImageView[@resource-id='in.amazon.mShop.android.shopping:id/chrome_action_bar_search_icon']")
	WebElement SearchIcon;
	
	@AndroidFindBy(xpath = "//android.widget.EditText[@text='Search Amazon.in']")
	WebElement SearchTextfield;

	// User Defined Method To Above Elements
	public WebElement ChooseYourLanguage() {
		return ChooseYourLanguage;
	}
	
	public WebElement LanguageOption(String LanguageName) {
		By LanguageOption = By.xpath("//android.widget.ImageView[@content-desc='"+LanguageName+"']");
		return androidDriver.findElement(LanguageOption);
	}
	
	public WebElement ContinueButton() {
		return ContinueButton;
	}
	
	public WebElement SkipSignIn() {
		return SkipSignIn;
	}
	
	public WebElement SearchTextField() {
		return SearchTextField;
	}
	
	public WebElement SearchTextfield() {
		return SearchTextfield;
	}
	
	public WebElement SearchIcon() {
		return SearchIcon;
	}

}
