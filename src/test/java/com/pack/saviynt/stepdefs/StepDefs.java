package com.pack.saviynt.stepdefs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.pack.saviynt.drivers.DriverBase;
import com.pack.saviynt.utils.AppData;
import com.pack.saviynt.utils.Library;
import com.pack.saviynt.utils.TestDataReader;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * The Class StepDefs.
 */
public class StepDefs {

    /** The logger. */
    public Logger logger = Logger.getLogger(StepDefs.class.getName());

    /** The driver. */
    public static WebDriver driver;

    /** The scenario. */
    public Scenario scenario;

    /**
     * Sets the up.
     * @param scenario the new up
     * @throws Throwable the throwable
     */
    @Before
    public void setUp(Scenario scenario) throws Throwable {
        this.scenario = scenario;
        driver = DriverBase.getDriver();
    }

    /**
     * Navigate to url.
     * @param page the page
     */

    @Given("^I Navigate to (.*) Page$")
    public void navigateToUrl(String page) {
        try {
            logger.info("Navigate to " + page);
            driver.get(AppData.getProperty("appUrl"));
            driver.manage().window().maximize();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Enter dynamic text.
     * @param object the object
     * @param property the property
     */
    @When("^I Enter (.*) as (.*)$")
    public void enterText(String object, String property) {
        try {
            logger.info("I Enter text into " + object);
            if (StringUtils.isEmpty(TestDataReader.getProperty(property))) {
                logger.info("Enter text as " + property);
                Library.enterText(object, property);
            } else {
                logger.info("Enter text as " + TestDataReader.getProperty(property));
                Library.enterText(object, TestDataReader.getProperty(property));
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @When("^I Select (.*) value as (.*)$")
    public void selectValue(String object, String property) {
        try {
            logger.info("I Select value into " + object);
            if (StringUtils.isEmpty(TestDataReader.getProperty(property))) {
                logger.info("I Select value as " + property);
                Library.selectByEnterText(object, property);
            } else {
                logger.info("I Select value as " + TestDataReader.getProperty(property));
                Library.selectByEnterText(object, TestDataReader.getProperty(property));
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @When("^I Switch (.*) value as (.*)$")
    public void switchValue(String object, String property) {
        try {
            if (StringUtils.isEmpty(TestDataReader.getProperty(property))) {
                Library.switchValue(object, property);
            } else {
                Library.switchValue(object, TestDataReader.getProperty(property));
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Given("^I (.*) (.*) checkbox$")
    public void selectCheckBox(String type, String object) {
        try {
            if (StringUtils.equals(type, "Select")) {
                Library.selectCheckBox(object);
            } else {
                Library.unSelectCheckBox(object);
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Click on object.
     * @param object the object
     */

    @When("^I Click on (.*) (.*)$")
    public void clickOnObject(String object, String type) {
        try {
            logger.info("Click on " + object);
            Library.click(object);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Verify.
     */

    @Then("^Verify User is Logged in or not$")
    public void verify() {
        try {
            logger.info("Verify text is display or not");
            String actualValue = Library.getProperty("welcomeName", "Text");
            Assert.assertEquals(actualValue, "Welcome");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Tear down.
     * @param scenario the scenario
     * @throws Throwable the throwable
     */
    @After
    public void tearDown(Scenario scenario) throws Throwable {
        if (scenario.isFailed()) {
            embedScreenshot(scenario);
        }
        DriverBase.closeDriverObjects();
    }

    /**
     * Embed screenshot.
     * @param scenario the scenario
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void embedScreenshot(Scenario scenario) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String screenshotName = AppData.getProperty("screenShotPath") + "/" + scenario.getName() + "_"
                + System.currentTimeMillis() + ".jpg";
        File destination = new File(screenshotName);
        FileUtils.copyFile(source, destination);
        scenario.embed(Files.readAllBytes(destination.toPath()), "image/png");
    }
}
