package com.TestScripts;

import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.Utils.TestBase;

import net.sourceforge.tess4j.TesseractException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TS003_CompareDataAndGenerateReport extends TestBase{


    @BeforeTest
    public void setup() throws IOException, InterruptedException, TesseractException {
    	
        // Configuring Logger
        logger = Logger.getLogger("Compare Data and Generate Report");
        PropertyConfigurator.configure("log4j.properties");
        //execute Both Web And Andriod to compare The results
    }
    

    @Test(dependsOnMethods = {
            "com.TestScripts.TS001_GetTheSearchedHindiBookNamePriceRatingInSeleniumWedAutomation.get_All_The_BooksName_Price_Review_In_Product_List_Page",
            "com.TestScripts.TS002_GetTheDataOfProductListPageFromScreenshotUsingAppium.GetTheScreenShotOfTheProductListPage"
    })
    public void compareData() throws IOException {
    	TS001KeyValue = getDataFromExcel(excelPath, TestDataSheetName, uniqueDataTS001, isTableHorizontal);
		String searchedBooksNameBooksPriceBooksReview = fetchDatFromMap(TS001KeyValue, "Searched_BooksName_BooksPrice_BooksReview");
    	TS002KeyValue = getDataFromExcel(excelPath, TestDataSheetName, uniqueDataTS002, isTableHorizontal);
		String imageData = fetchDatFromMap(TS002KeyValue, "Image_Data");
    	
		System.out.println(searchedBooksNameBooksPriceBooksReview);
		System.out.println(imageData);
		
        // Parse data into maps
        Map<String, String> seleniumData = parseData(searchedBooksNameBooksPriceBooksReview);
        Map<String, String> ocrData = parseData(imageData);

        // Generate comparison report
        generateComparisonReport(seleniumData, ocrData);
    }

    private Map<String, String> parseData(String dataString) {
        Map<String, String> dataMap = new HashMap<>();
        // Split dataString into individual entries
        String[] entries = dataString.split(",");
        for (String entry : entries) {
            // Split each entry into key-value pairs
            String[] keyValue = entry.split("=");
            if (keyValue.length == 2) {
                dataMap.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return dataMap;
    }

    private void generateComparisonReport(Map<String, String> seleniumData, Map<String, String> ocrData) {
        // Create a StringBuilder for report content
        StringBuilder reportContent = new StringBuilder();

        // Append header to report
        reportContent.append("Comparison Report:\n");
        reportContent.append("Book Name, Selenium Data, OCR Data, Status\n");

        // Initialize counters for found and not found data
        int foundCount = 0;
        int notFoundCount = 0;

        // Iterate over selenium data and compare with OCR data
        for (String bookName : seleniumData.keySet()) {
            String seleniumInfo = seleniumData.get(bookName);
            String ocrInfo = ocrData.get(bookName);
            String status;

            if (ocrInfo != null) {
                status = seleniumInfo.equals(ocrInfo) ? "Match" : "Mismatch";
                foundCount++;
            } else {
                status = "Not found in OCR Data";
                notFoundCount++;
            }

            // Append each comparison result to report
            reportContent.append(bookName).append(", ")
                    .append(seleniumInfo).append(", ")
                    .append(ocrInfo != null ? ocrInfo : "N/A").append(", ")
                    .append(status).append("\n");
        }

        // Print report to console
        System.out.println(reportContent.toString());

        // Save report to file (you can change the file path as per your requirement)
        String filePath = "comparison_report.txt";
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(filePath), reportContent.toString().getBytes());
            logger.info("Comparison report saved to file: " + filePath);
        } catch (IOException e) {
            logger.error("Failed to save comparison report to file: " + e.getMessage());
        }

        // Print summary
        logger.info("Comparison summary:");
        logger.info("Total entries found: " + foundCount);
        logger.info("Total entries not found: " + notFoundCount);
    }

    @AfterTest
    public void teardown() {
        logger.info("Comparison test completed.");
    }
}
