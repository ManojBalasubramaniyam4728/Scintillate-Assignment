package com.TestScripts;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.PageObject.AmazonHomePage;
import com.PageObject.AmazonProductListPage;
import com.Utils.TestBase;

public class TS001_GetTheSearchedHindiBookNamePriceRatingInSeleniumWedAutomation extends TestBase {

	@BeforeTest
	public void open_Browser_And_Navigate_To_Amazon_Website() throws IOException {

		// Reading The logger
		logger = Logger.getLogger("Get The Scraped Data Of Searched Book");// Adding logger
		PropertyConfigurator.configure("log4j.properties");// Adding logger
		// Featching Data fro execl
		testConfigKeyValue = getDataFromExcel(excelPath, testConfigSheetName, uniqueDataTestConfig, isTableVertical);
		String url = fetchDatFromMap(testConfigKeyValue, "Web_URL");

		driver = initializeDriver();
		logger.info("Browser Opened Successfully");
		driver.get(url);
		logger.info("Successfully Navigated to Amazon url ");
		WaitImplicitly();
	}

	
	@Test
	public void get_All_The_BooksName_Price_Review_In_Product_List_Page() throws IOException, InterruptedException {

		// Given life of driver to Page Object
		ahp = new AmazonHomePage(driver);
		aplp = new AmazonProductListPage(driver);

		// settings to obtaining Data from Excel
		testConfigKeyValue = getDataFromExcel(excelPath, testConfigSheetName, uniqueDataTestConfig, isTableVertical);
		TS001KeyValue = getDataFromExcel(excelPath, TestDataSheetName, uniqueDataTS001, isTableHorizontal);
		String ignoreCaseInString = fetchDatFromMap(testConfigKeyValue, "Ignore_Case");
		boolean ignoreCase = stringToBoolean(ignoreCaseInString);
		String amazonLogoVerification = fetchDatFromMap(TS001KeyValue, "Amazon_Logo_Verification");
		String productName = fetchDatFromMap(TS001KeyValue, "Product_Name");
		String attributeName = fetchDatFromMap(TS001KeyValue, "Attribute_Name");
		String bookName = fetchDatFromMap(TS001KeyValue, "Book_Name");
		String bookPrice = fetchDatFromMap(TS001KeyValue, "Book_Price");
		String bookReview = fetchDatFromMap(TS001KeyValue, "Book_Review");
		String theBookHasNoReviews = fetchDatFromMap(TS001KeyValue, "The_Book_Has_No_Reviews");

		// Ui Actions
		verifyAttributOfElementIsHavingExpectedText(ahp.AmazonLogo(), attributeName, amazonLogoVerification,ignoreCase);
		logger.info("The Amazon Logo Is Having " + attributeName + " Attribut and Text Is " + amazonLogoVerification);
		clickOnTheElement(ahp.SearchTextfield());
		logger.info("Successfully Clicked On the Search Text field");
		enterInputIntoTheElement(ahp.SearchTextfield(), productName);
		logger.info("Successfully Entred " + productName + " into Search Text field");
		clickOnTheElement(ahp.SearchIcon());
		logger.info("Successfully Clicked On the Search Icon");
		WaitImplicitly();
		logger.info("Successfully Waited till Product List Page Loads");

		Integer NumberOfBooksInPLP = aplp.SearchedBookList().size();
		logger.info("Successfully Fetched " + NumberOfBooksInPLP + " Number of Product in Product List Page");
		LinkedHashMap<String, String> scrapedData = new LinkedHashMap<String, String>();
		for (int i = 1; i <= NumberOfBooksInPLP; i++) {
			scrollTillElementIsvisable(aplp.BookName(i));
			logger.info("Successfully scroll Till Book " + i + " Name");
			String BookName = aplp.BookName(i).getText();
			logger.info("The Book " + i + " Name is " + BookName);
			String BookPrice = aplp.BookPrice(i).getText();
			logger.info("The Book " + i + " Price is " + BookPrice);
			String BookReview;
			// There is a chance that the review of the book might not be there, so try and
			// catch
			try {
				BookReview = aplp.BookReview(i).getAttribute(attributeName);
				logger.info("The Book " + i + " Review Rating is " + BookReview);
			} catch (NoSuchElementException e) {
				BookReview = theBookHasNoReviews;
				logger.info("The Book " + i + " Review Rating is Not Given");
			}
			// Storing The scraped Data into Json Formate or comma-separated values `
			scrapedData.put(bookName + i, BookName);
			scrapedData.put(bookPrice + i, BookPrice);
			scrapedData.put(bookReview + i, BookReview);
			logger.info("Successfully Stored Book " + i + " Deatils in The Key Value pair ");
		}
		String mapAsString = scrapedData.toString();
		WriteDataInToExcel(excelPath, TestDataSheetName, uniqueDataTS001, "Searched_BooksName_BooksPrice_BooksReview",
				mapAsString, isTableHorizontal);
		logger.info("Successfully Written The scraped Data of Book in The Execl");

		// To Store in a json file for Safer side using json-simple library
		JSONArray bookArray = new JSONArray();
		logger.info("Storing The Data in Json File");
		for (int i = 1; i <= NumberOfBooksInPLP; i++) {
			scrollTillElementIsvisable(aplp.BookName(i));
			String BookName = aplp.BookName(i).getText();
			String BookPrice = aplp.BookPrice(i).getText();
			String BookReview;
			// There is a chance that the review of the book might not be there, so try and
			// catch
			try {
				BookReview = aplp.BookReview(i).getAttribute(attributeName);
			} catch (NoSuchElementException e) {
				BookReview = theBookHasNoReviews;
			}

			// Storing The scraped Data into Json Formate or comma-separated values `
			JSONObject bookObject = new JSONObject();
			bookObject.put("name" + i, BookName);
			bookObject.put("price" + i, BookPrice);
			bookObject.put("rating" + i, BookReview);

			bookArray.add(bookObject);
		}
		WriteJsonDataInToJsonFile(bookArray);
		logger.info("Successfully Written The scraped Data of Book in The Json File");
	}

	
	@AfterTest
	public void TearDownTheDriver() {
		closingDriver();
		logger.info("Successfully Closed The Browser");
	}
}
