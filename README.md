                                    Test Automation Framework for Scintillate Assignment


Setup Instructions

1. Prerequisites:

   *Java Development Kit (JDK) 17 or higher
   *Maven
   *IDE (Eclipse, IntelliJ IDEA, etc.)
   *Chrome, Firefox, Edge, or Internet Explorer for WebDriver setup (depending on browser testing needs)
   *Android SDK for Appium tests on Android devices
   *Steps to Setup

2. Import the project into your IDE

   *Open Eclipse/IntelliJ IDEA
   *Import the Maven project from the cloned directory

3. Install dependencies

   * Maven will automatically download dependencies listed in pom.xml

4.Setup WebDriver executables

    * Download WebDriver executables (ChromeDriver, GeckoDriver, etc.) and place them in your system's PATH or update TestBase.java to specify their    location.

5.Configure test data.

    * Update Scintillate_TestCase_And_TestData.xlsx with relevant test cases and data.

6. Run tests

    * Execute test cases using TestNG or your IDE's test runner.

7. Usage

    * Modify test cases in com/TestScripts based on your requirements.
    * Extend or modify base classes (TestBase.java) for additional functionalities or customizations.

8. Logging

    * Log files are generated in logs/ directory.

9. Reporting

    * Extent Reports are used for test reporting and can be found in test-output/ directory.

10. Troubleshooting

    * Ensure all dependencies are correctly configured.
    * Check WebDriver configurations in initializeDriver() method of TestBase.java.

11. Additional Notes

    * Appium Server Port: The tests assume that the Appium server is running on port 4723. Please ensure no other service is occupying this port before starting Appium.

    * Amazon APK Version: Make sure to install the correct version of the Amazon APK from the Play Store. This ensures compatibility and prevents issues during test execution.

12.Requirements


selenium==4.17.0
java-client==9.1.0
testng==7.9.0
poi-ooxml==5.2.2
poi==5.2.2
log4j==1.2.17
extentreports==3.0.6
json-simple==1.1.1
tess4j==4.5.4

13. Contributors
     
Manoj B
