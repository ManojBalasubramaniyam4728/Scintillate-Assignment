package com.TestScripts;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.PageObject.AmazonHomeScreen;
import com.Utils.TestBase;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

public class TS002_GetTheDataOfProductListPageFromScreenshotUsingAppium extends TestBase {

	@BeforeTest
	public void OpenTheAmazonApplication() throws IOException {
		// Make sure you downloaded amazon application from playstore only
		// Reading The logger
		logger = Logger.getLogger("Get The Scraped Data Of Screenshot");// Adding logger
		PropertyConfigurator.configure("log4j.properties");// Adding logger
		openAmzonAppication();
		logger.info("Sucessfully Opend amazon Apllication");
	}

	@Test
	public void GetTheScreenShotOfTheProductListPage() throws IOException, TesseractException {
		// Given life of driver to Page Object
	    ahs = new AmazonHomeScreen(androidDriver);

		// settings to obtaining Data from Excel
		testConfigKeyValue = getDataFromExcel(excelPath, testConfigSheetName, uniqueDataTestConfig, isTableVertical);
		TS002KeyValue = getDataFromExcel(excelPath, TestDataSheetName, uniqueDataTS002, isTableHorizontal);
		String ignoreCaseInString = fetchDatFromMap(testConfigKeyValue, "Ignore_Case");
		boolean ignoreCase = stringToBoolean(ignoreCaseInString);
		String languageOption=fetchDatFromMap(TS002KeyValue, "Choose_Your_Language");
		String amazonHomeScreenVerification=fetchDatFromMap(TS002KeyValue, "Amazon_Home_Screen_Verification");
		String productName=fetchDatFromMap(TS002KeyValue, "Product_Name");
		String SwipeCount=fetchDatFromMap(TS002KeyValue, "Swipe_Count");
		Integer swipeCount=Integer.parseInt(SwipeCount);
		String tessDataFolderValue=fetchDatFromMap(TS002KeyValue, "Tess_Data_Folder_Value");

		// The Application is opening from first so it will ask for choose language so
		// it may or may not
		try {
			ahs.ChooseYourLanguage();
			logger.info("Successfully Verifyed The Choose Your Language is pesent");
			tap(ahs.LanguageOption(languageOption));
			logger.info("Sucessfully Tapped on "+languageOption+" Option");
			tap(ahs.ContinueButton());
			logger.info("Sucessfully Tapped on Continue Button");
			tap(ahs.SkipSignIn());
			logger.info("Sucessfully Tapped on Skip Sign In Option");
		} catch (NoSuchElementException e) {
			logger.info("Elemnet Is Not Present So Direct Go For Seacrching The Product");
		}
		verifyElementIsHavingExpectedText(ahs.SearchTextField(), amazonHomeScreenVerification, ignoreCase);
		logger.info("Sucessfully verifyed Element Is Having "+amazonHomeScreenVerification+" Text");
		tap(ahs.SearchTextField());
		logger.info("Sucessfully Tapped on Search TextField");
		enterInputIntoTheElement(ahs.SearchTextfield(),productName);
		logger.info("Sucessfully Entered "+productName+" into Search Textfield");
		pressEnterKeyInKeyboard();
		logger.info("Sucessfully Pressed enter key in android keyboard");
		WaitImplicitlyForAppium();
		
		
		swipe(swipeCount);
		logger.info("Sucessfully swiped "+swipeCount+" Times");
		takeScreenshot();
		logger.info("Sucessfully Captchered Screenshot");
		
		//Creating Instance for the Tesseract class.
		Tesseract tesseract = new Tesseract();

		// Set the Tesseract data path
		File tessDataFolder = LoadLibs.extractTessResources(tessDataFolderValue);
		tesseract.setDatapath(tessDataFolder.getAbsolutePath());

		// Perform OCR on an image
		String TextFromImage = tesseract.doOCR(new File(screenshootOfProductListScreen));
		logger.info("Sucessfully Fatched Data From Image");
		WriteDataInToExcel(excelPath, TestDataSheetName, uniqueDataTS002, "Image_Data",TextFromImage, isTableHorizontal);
		logger.info("Sucessfully Stored Data Into Excel");
	}
	
	@AfterTest
	public void CloseTheApplication() {
		closingApplication();
		logger.info("Sucessfully Closed Amazon All");
	}

}
