package com.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.simple.JSONArray;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.PageObject.AmazonHomePage;
import com.PageObject.AmazonHomeScreen;
import com.PageObject.AmazonProductListPage;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;


public class TestBase {

	// global Variables
	public static WebDriver driver;
	public static AndroidDriver androidDriver;
	public static Logger logger;
	public static AmazonHomePage ahp;
	public static AmazonProductListPage aplp;
	public static AmazonHomeScreen ahs;
	public static String screenshootFilePath = "screenshoot/";
	public static String screenshootOfProductListScreen = "Screenshot Of Product List Screen.png";
	public static String jsonFilePath = "books.json";
	public static String apkFilePath = "com-amazon-mshop-android-shopping-1241274011-67963971-a700fa57e27ad3614943bdf40327d5b9.apk";
	public static String excelPath = "Scintillate_TestCase_And_TestData.xlsx";
	public static String testConfigSheetName = "Test Config";
	public static String TestDataSheetName = "Test Data For The Test Scripts";
	public static String uniqueDataTestConfig = "Test";
	public static String uniqueDataTS001 = "TS001";
	public static String uniqueDataTS002 = "TS002";
	public static LinkedHashMap<String, String> testConfigKeyValue = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> TS001KeyValue = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> TS002KeyValue = new LinkedHashMap<String, String>();
	public static boolean isTableVertical = true;
	public static boolean isTableHorizontal = false;

//**************************************************************************************************************************************
	// User Defined Method To Invoking Browsers
	public WebDriver initializeDriver() throws IOException {
		testConfigKeyValue = getDataFromExcel(excelPath, testConfigSheetName, uniqueDataTestConfig, isTableVertical);
		String browserName = fetchDatFromMap(testConfigKeyValue, "Browser_Name");

		if (browserName.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}

		else if (browserName.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
		}

		else if (browserName.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
			driver.manage().window().maximize();
		}

		else {
			driver = new InternetExplorerDriver();
			driver.manage().window().maximize();
		}

		return driver;
	}

//**************************************************************************************************************************************
		//User Defined Method To open amazon application with app Packag,Activity no reset false
		public static AndroidDriver openAmzonAppication() throws IOException {
			testConfigKeyValue = getDataFromExcel(excelPath, testConfigSheetName, uniqueDataTestConfig, isTableVertical);
			String platformName = fetchDatFromMap(testConfigKeyValue, "Platform_Name");
			String deviceName = fetchDatFromMap(testConfigKeyValue, "Device_Name");
			String amzonAppPackage = fetchDatFromMap(testConfigKeyValue, "Amzon_App_Package");
			String amzonAppActivity = fetchDatFromMap(testConfigKeyValue, "Amzon_App_Activity");
			String AmzonNoReset = fetchDatFromMap(testConfigKeyValue, "Amzon_No_Reset");
			boolean noReset=stringToBoolean(AmzonNoReset);
			String AutoGrantpermissions = fetchDatFromMap(testConfigKeyValue, "Auto_Grantpermissions");
			boolean autoGrantpermissions=stringToBoolean(AutoGrantpermissions);
			String mobileHubUrl = fetchDatFromMap(testConfigKeyValue, "Mobile_Hub_Url");
		

			// Creating Capabilities
			DesiredCapabilities cap = new DesiredCapabilities();

			// Setting Capabilities
			cap.setCapability("platformName",platformName );
			cap.setCapability("deviceName", deviceName);
			cap.setCapability("appPackage", amzonAppPackage);
			cap.setCapability("appActivity", amzonAppActivity);
			cap.setCapability("noReset", noReset);
			cap.setCapability("autoGrantPermissions", autoGrantpermissions);

			// url To server
			URL url = new URL(mobileHubUrl);

			// open the app
			androidDriver = new AndroidDriver(url, cap);
			WaitImplicitlyForAppium();
			return androidDriver;
		}

//**************************************************************************************************************************************
	// User Defined Method For Taking Screnshot On Failed Steps
	public void getScreenShot(String result) throws IOException {
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(src, new File(screenshootFilePath + result + " screenshot.png"));
	}

//**************************************************************************************************************************************
	// User Defined Method Get Data FromExcel
	public static LinkedHashMap<String, String> getDataFromExcel(String excelPath, String sheetName, String uniqueData,
			boolean isTableVertical) throws IOException {
		FileInputStream fisExcel = new FileInputStream(excelPath);
		Workbook workbook = WorkbookFactory.create(fisExcel);
		DataFormatter df = new DataFormatter();
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		Sheet sheet = workbook.getSheet(sheetName);
		int lastRowNumber = sheet.getLastRowNum(); // return index ==> index

		String value = "";
		String actualTestCaseName = "";
		String actualKey = "";

		// For Horizontal Data Featching In Key Value Pair
		if (isTableVertical == false) {
			for (int i = 0; i <= lastRowNumber; i++) {
				String actualTestcase = df.formatCellValue(sheet.getRow(i).getCell(0));
				if (actualTestcase.equals(uniqueData)) {
					short lastcellNumber = sheet.getRow(i).getLastCellNum(); // return count/size ==> count-1
					for (int j = 1; j < lastcellNumber - 1; j++) {
						actualKey = df.formatCellValue(sheet.getRow(i - 1).getCell(j));
						value = df.formatCellValue(sheet.getRow(i).getCell(j));
						map.put(actualKey, value);
					}
					break;
				}
			}
		}

		// For Vertical Data Featching In Key Value Pair
		else if (isTableVertical == true) {
			for (int i = 1; i <= sheet.getRow(i).getLastCellNum(); i++) {

				try {
					actualTestCaseName = df.formatCellValue(sheet.getRow(0).getCell(i));

				} catch (Exception e) {
				}
				if (actualTestCaseName.equalsIgnoreCase(uniqueData)) {
					for (int j = 0; j <= sheet.getLastRowNum(); j++) {

						try {
							actualKey = df.formatCellValue(sheet.getRow(j).getCell(i - 1));
							try {
								value = df.formatCellValue(sheet.getRow(j).getCell(i));
							} catch (Exception e) {
							}

							if ((actualKey.isEmpty() && value.isEmpty()) || actualKey.isEmpty()) {
							} else {
								map.put(actualKey, value);
							}
						} catch (Exception e) {
						}
					}
					break;
				}
			}
		}
		workbook.close();
		fisExcel.close();
		return map;
	}

//**************************************************************************************************************************************
	// User Defined Method Get Data Map
	public static String fetchDatFromMap(LinkedHashMap<String, String> testConfigKeyValue, String value) {
		return value = testConfigKeyValue.get(value);
	}

//**************************************************************************************************************************************
	// User Defined Method write Data FromExcel
	public static void WriteDataInToExcel(String excelPath, String sheetName, String uniqueData, String header,
		String data, boolean isTableVertical) throws EncryptedDocumentException, IOException {
		FileInputStream excelFile = new FileInputStream(new File(excelPath));
		Workbook workbook = WorkbookFactory.create(excelFile);
		Sheet sheet = workbook.getSheet(sheetName);
		DataFormatter df = new DataFormatter();
		boolean flag = false;
		String actualTestCaseName = "";
		String actualKey = "";

		// For Horizontal Data Write Into Excel
		if (isTableVertical == false) {
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				try {
					actualTestCaseName = df.formatCellValue(sheet.getRow(i).getCell(0));
				} catch (Exception e) {
				}
				if (actualTestCaseName.equalsIgnoreCase(uniqueData)) {
					for (int j = 1; j <= sheet.getRow(i).getLastCellNum(); j++) {
						try {
							actualKey = df.formatCellValue(sheet.getRow(i - 1).getCell(j));
						} catch (Exception e) {
						}
						if (actualKey.equalsIgnoreCase(header)) {
							try {
								sheet.getRow(i).createCell(j).setCellValue(data);
							} catch (Exception e) {
							}
							flag = true;
							break;
						}
					}
				}
				if (flag == true) {
					break;
				}
			}
		}
		// For Vertical Data Write Into Excel
		else if (isTableVertical == true) {
			for (int i = 1; i <= sheet.getRow(i).getLastCellNum(); i++) {

				try {
					actualTestCaseName = df.formatCellValue(sheet.getRow(0).getCell(i));

				} catch (Exception e) {
				}
				if (actualTestCaseName.equalsIgnoreCase(uniqueData)) {
					for (int j = 0; j <= sheet.getLastRowNum(); j++) {

						try {
							actualKey = df.formatCellValue(sheet.getRow(j).getCell(i - 1));
						} catch (Exception e) {
						}
						if (actualKey.equalsIgnoreCase(header)) {
							try {
								sheet.getRow(j).createCell(i).setCellValue(data);
							} catch (Exception e) {
							}
							flag = true;
							break;
						}
					}
				}
				if (flag == true) {
					break;
				}
			}
		}
		FileOutputStream fos = new FileOutputStream(excelPath);
		workbook.write(fos);
		fos.close();
		workbook.close();
		excelFile.close();
	}
//**************************************************************************************************************************************
	// User Defined Method To Explicit Wait
	public void WaitUntilvisibilityOfElement(WebElement Element) throws IOException {
		testConfigKeyValue = getDataFromExcel(excelPath, testConfigSheetName, uniqueDataTestConfig, isTableVertical);
		String explicitWait = fetchDatFromMap(testConfigKeyValue, "Explicit_Wait_Time");
		Integer explicitWaitTime = Integer.parseInt(explicitWait);
		// logic gos here
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWaitTime));
		wait.until(ExpectedConditions.visibilityOf(Element));
	}

//**************************************************************************************************************************************
	// User Defined Method To Implicitly Wait
	public static void WaitImplicitly() throws IOException {
		testConfigKeyValue = getDataFromExcel(excelPath, testConfigSheetName, uniqueDataTestConfig, isTableVertical);
		String implicitlyWait = fetchDatFromMap(testConfigKeyValue, "Implicit_Wait _Time");
		Integer implicitlyWaitTime = Integer.parseInt(implicitlyWait);
		// logic gos here
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitlyWaitTime));
	}
	
//**************************************************************************************************************************************
		// User Defined Method To Implicitly Wait
		public static void WaitImplicitlyForAppium() throws IOException {
			testConfigKeyValue = getDataFromExcel(excelPath, testConfigSheetName, uniqueDataTestConfig, isTableVertical);
			String implicitlyWait = fetchDatFromMap(testConfigKeyValue, "Implicit_Wait _Time");
			Integer implicitlyWaitTime = Integer.parseInt(implicitlyWait);
			// logic gos here
			androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitlyWaitTime));
		}

// **************************************************************************************************************************************
	// Verify Element Is having Expected Text
	public boolean verifyElementIsHavingExpectedText(WebElement element, String expectedText, boolean ignoreCase) {
		// Get the text from the element
		String actualText = element.getText();
		// Perform text verification (case insensitive)
		boolean isTextMatching;

		if (ignoreCase == true) {
			isTextMatching = actualText.equalsIgnoreCase(expectedText);
		} else {
			isTextMatching = actualText.equals(expectedText);
		}
		return isTextMatching;
	}

	// **************************************************************************************************************************************
	// Verify Element Is Contains Expected Text
	public boolean verifyElementIsContainsExpectedText(WebElement element, String expectedText, boolean ignoreCase) {
		// Get the text from the element
		String actualText = element.getText();
		// Perform text verification (case insensitive)
		boolean isTextMatching;

		if (ignoreCase == true) {
			isTextMatching = actualText.toLowerCase().contains(expectedText);
		} else {
			isTextMatching = actualText.contains(expectedText);
		}

		return isTextMatching;

	}

// **************************************************************************************************************************************
	// Verify Attribute Of Element Is having Expected Text
	public boolean verifyAttributOfElementIsHavingExpectedText(WebElement element, String attributName,
			String expectedText, boolean ignoreCase) {
		// Get the text from the element
		String actualText = element.getAttribute(attributName);
		// Perform text verification (case insensitive)
		boolean isTextMatching;

		if (ignoreCase == true) {
			isTextMatching = actualText.equalsIgnoreCase(expectedText);
		} else {
			isTextMatching = actualText.equals(expectedText);
		}

		return isTextMatching;

	}

// **************************************************************************************************************************************
	// Verify Attribute Of Element Is Contains Expected Text
	public boolean verifyAttributOfElementIsContainsExpectedText(WebElement element, String attributName,
			String expectedText, boolean ignoreCase) {
		// Get the text from the element
		String actualText = element.getAttribute(attributName);
		// Perform text verification (case insensitive)
		boolean isTextMatching;

		if (ignoreCase == true) {
			isTextMatching = actualText.toLowerCase().contains(expectedText);
		} else {
			isTextMatching = actualText.contains(expectedText);
		}

		return isTextMatching;

	}

// **************************************************************************************************************************************
	// Verify Element is displayed are Not
	public boolean verifyElementIsDisplayedAreNot(WebElement element) {
		boolean isDisplayed;
		try {
			isDisplayed = element.isDisplayed();
			isDisplayed = true;

		} catch (NoSuchElementException e) {
			isDisplayed = false;
		}
		return isDisplayed;
	}

// **************************************************************************************************************************************
	// Enter Input Into The Element
	public void enterInputIntoTheElement(WebElement element, String input) {
		element.sendKeys(input);
	}

// **************************************************************************************************************************************
	// Clear The Text And Enter The Input Into The Element
	public void clearTheTextAndEnterInputIntoTheElement(WebElement element, String input) {
		element.clear();
		element.sendKeys(input);
	}

// **************************************************************************************************************************************

	// Convert ArrayList To String
	public String arrayListToString(ArrayList<String> arrayList) {
		// Convert ArrayList to String using toString() method
		String arrayListAsString = arrayList.toString();
		return arrayListAsString;
	}

// **************************************************************************************************************************************
	// Convert String to Boolean
	public static boolean stringToBoolean(String Value) {
		boolean booleanValue = Boolean.parseBoolean(Value);
		return booleanValue;
	}
	
// **************************************************************************************************************************************
   // Click On The Element
	public void clickOnTheElement(WebElement element) {
	 element.click();
	}
	
//**************************************************************************************************************************************
	//User Defined Method To Tap on Element
	public void tap(WebElement element) throws IOException {
		Point sourceLocation=element.getLocation();
		Dimension sourceSize=element.getSize();
		int centerX = sourceLocation.getX() + sourceSize.getWidth() / 2;
		int centerY = sourceLocation.getY() + sourceSize.getHeight() / 2;
		
		PointerInput finger=new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence tap=new Sequence(finger, 1);
		tap.addAction(finger.createPointerMove(Duration.ofMillis(0),PointerInput.Origin.viewport(), centerX, centerY));
		tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
		tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
	    androidDriver.perform(Arrays.asList(tap));
	}
	
//**************************************************************************************************************************************
	//User Defined Method To Swipe Number Time
	public void swipe(int maximumCount) throws IOException {
		//Taking mobile X and y center of screen
		int centerScreenX=androidDriver.manage().window().getSize().getWidth()/2;
		int centerScreenY=androidDriver.manage().window().getSize().getHeight()/2;
		
		//Frome center of the screen swiping 30% upword
		int endY=(int) (androidDriver.manage().window().getSize().getHeight()*0.02);
		
		int maxCount=maximumCount;
		int count=0;
			for(int i = count; i < maxCount; i++) {
			PointerInput finger=new PointerInput(PointerInput.Kind.TOUCH, "finger");
		    Sequence swipe=new Sequence(finger, 1);
		   
				swipe.addAction(finger.createPointerMove(Duration.ZERO,PointerInput.Origin.viewport(), centerScreenX, centerScreenY));
				swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
				swipe.addAction(finger.createPointerMove(Duration.ofMillis(600),PointerInput.Origin.viewport(), centerScreenX, endY));
				swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
				androidDriver.perform(Arrays.asList(swipe));
		}
	}
	
//**************************************************************************************************************************************
	  //Take Screenshot and save it in a file
	public void takeScreenshot() throws IOException {
		TakesScreenshot tsc=(TakesScreenshot)androidDriver;
		File screenshot=tsc.getScreenshotAs(OutputType.FILE);
		File file= new  File(screenshootOfProductListScreen);
		FileUtils.copyFile(screenshot, file);
	}
	
//**************************************************************************************************************************************
    //User Defined Method For Scroll By JavascriptExecutor
    public static void scrollTillElementIsvisable(WebElement element) {
		JavascriptExecutor scroll = (JavascriptExecutor) driver;
		scroll.executeScript("arguments[0].scrollIntoView(true);", element);
    }
    
  //**************************************************************************************************************************************
    //User Defined Method For Scroll By JavascriptExecutor
    public static void WriteJsonDataInToJsonFile(JSONArray jsonArray) {
    	 try (FileWriter file = new FileWriter(jsonFilePath)) {
             file.write(jsonArray.toJSONString());
         }		
         catch (IOException e) {
             e.printStackTrace();
 	    }
    }
    
 //**************************************************************************************************************************************
    //User Defined Method For Scroll By JavascriptExecutor
    public static void pressEnterKeyInKeyboard() {
    	androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.ENTER)); 	    
    }
    
//**************************************************************************************************************************************
    //User Defined Method For Closing The Browser
  	public void closingDriver() {
  	       driver.close();
  	       driver=null;
  	} 
//**************************************************************************************************************************************
  //User Defined Method For Closing The Kuza Application
	public void closingApplication() {
	       androidDriver.quit();
	       androidDriver=null;
	}
}
