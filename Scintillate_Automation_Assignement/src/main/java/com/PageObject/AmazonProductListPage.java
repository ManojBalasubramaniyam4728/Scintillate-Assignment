package com.PageObject;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class AmazonProductListPage {
	// WedDriver invoking
		WebDriver driver;

		// Giving life to driver by Constracor
		public AmazonProductListPage(WebDriver driver) {
			this.driver = driver;
			PageFactory.initElements(driver, this);
		}

	// **************************************************************************************************************************************

		// User Defined Method To Above Elements
		// Dynamic Element
		public List<WebElement> SearchedBookList() {
		By SearchedBookList=By.xpath("//span[@data-component-type='s-search-results']/..//div[@data-component-type='s-search-result']");
		return driver.findElements(SearchedBookList);
		}
		
		public WebElement BookName(int index) {
		By BookName=By.xpath("(//span[@data-component-type='s-search-results']/..//div[@data-component-type='s-search-result'])["+index+"]/.//h2/a//span[text()]");
	    return driver.findElement(BookName);
		}
		
		public WebElement BookPrice(int index) {
			By BookPrice=By.xpath("(//span[@data-component-type='s-search-results']/..//div[@data-component-type='s-search-result'])["+index+"]/.//span[@class='a-price-whole']");
			return driver.findElement(BookPrice);
		}
		
		public WebElement BookReview(int index) {
			By BookReview=By.xpath("(//span[@data-component-type='s-search-results']/..//div[@data-component-type='s-search-result'])["+index+"]/.//i/span[text()]/../../../..");
			return driver.findElement(BookReview);
		}
	}
