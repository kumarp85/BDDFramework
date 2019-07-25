package com.pack.saviynt.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pack.saviynt.stepdefs.StepDefs;

/**
 * The Class Library.
 */
public class Library {

    /** The logger. */
    static Logger logger = Logger.getLogger(Library.class.getName());

    /**
     * Gets the date.
     * @param format the format
     * @return the date
     */
    public static String getDate(String format) {
        return getDate(format, 0, true);
    }

    /**
     * Gets the date.
     * @param format the format
     * @param appendZero the append zero
     * @return the date
     */
    public static String getDate(String format, Boolean appendZero) {
        return getDate(format, 0, appendZero);
    }

    /**
     * Gets the date.
     * @param format the format
     * @param future the future
     * @return the date
     */
    public static String getDate(String format, int future) {
        return getDate(format, future, true);
    }

    /**
     * Gets the date.
     * @param format the format
     * @param future the future
     * @param appendZero the append zero
     * @return the date
     */
    public static String getDate(String format, int future, Boolean appendZero) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        String[] dayValue;
        if (dateFormat.format(date).contains("/")) {
            dayValue = dateFormat.format(date).split("/");
        } else if (dateFormat.format(date).contains("-")) {
            dayValue = dateFormat.format(date).split("-");
        } else {
            return dateFormat.format(date);
        }

        int day = Integer.parseInt(dayValue[1]) + future;

        if (appendZero == true) {
            if (day < 10) {
                dayValue[1] = "0" + day;
            }
        } else {
            if (Integer.parseInt(dayValue[0]) <= 9) {
                dayValue[0] = dayValue[0].replace("0", "");
            }

            if (Integer.parseInt(dayValue[1]) <= 9) {
                dayValue[1] = dayValue[1].replace("0", "");
            }
        }

        if (format.contains("/")) {
            return dayValue[0] + "/" + dayValue[1] + "/" + dayValue[2];
        } else {
            return dayValue[0] + "-" + dayValue[1] + "-" + dayValue[2];
        }
    }

    /**
     * Click.
     * @param objectName the object name
     * @throws Exception the exception
     */
    public static void click(String objectName) throws Exception {
        getElement(objectName).click();
    }

    /**
     * Enter text.
     * @param objectName the object name
     * @param text the text
     * @throws Exception the exception
     */
    public static void enterText(String objectName, String text) throws Exception {
        getElement(objectName).sendKeys(text);
    }

    /**
     * Dobleclick.
     * @param object the object
     * @throws Exception the exception
     */
    public static void dobleclick(String object) throws Exception {
        Actions action = new Actions(StepDefs.driver);
        action.moveToElement(getElement(object)).build().perform();
        action.doubleClick(getElement(object)).build().perform();
    }

    /**
     * Select text by text.
     * @param object the object
     * @param text the text
     * @throws Exception the exception
     */
    public static void selectTextByText(String object, String text) throws Exception {
        Select select = new Select(getElement(object));
        select.selectByVisibleText(text);
    }

    /**
     * Select text by index.
     * @param object the object
     * @param index the index
     * @throws Exception the exception
     */
    public static void selectTextByIndex(String object, int index) throws Exception {
        Select select = new Select(getElement(object));
        select.selectByIndex(index);
    }

    /**
     * Select by enter text.
     * @param object the object
     * @param property the property
     * @throws Exception the exception
     */
    public static void selectByEnterText(String object, String property) throws Exception {
        WebElement element = getElement(object);
        logger.info("Element found " + property);
        element.sendKeys(property + Keys.ENTER);
    }

    /**
     * Select check box.
     * @param objectName the object name
     * @throws Exception the exception
     */
    public static void selectCheckBox(String objectName) throws Exception {
        if (StringUtils.isEmpty(getProperty(objectName, "class"))) {
            click(objectName);
        }
    }

    /**
     * Un select check box.
     * @param objectName the object name
     * @throws Exception the exception
     */
    public static void unSelectCheckBox(String objectName) throws Exception {
        if (!StringUtils.isEmpty(getProperty(objectName, "class"))) {
            click(objectName);
        }
    }

    /**
     * Switch value.
     * @param object the object
     * @param property the property
     * @throws Exception the exception
     */
    public static void switchValue(String object, String property) throws Exception {
        boolean elementClicked = false;
        List<WebElement> elements = getElements(object);
        for (WebElement element : elements) {
            if (!StringUtils.equalsIgnoreCase(element.getText(), property)) {
                element.click();
                elementClicked = true;
                break;
            }
        }
        if (!elementClicked) {
            throw new Exception("Unable to find the element or select the desired option");
        }
    }

    /**
     * Gets the property.
     * @param objectName the object name
     * @param property the property
     * @return the property
     * @throws Exception the exception
     */
    public static String getProperty(String objectName, String property) throws Exception {
        if (StringUtils.equalsAnyIgnoreCase(property, "Text")) {
            return getElement(objectName).getText();
        } else {
            return getElement(objectName).getAttribute(property);
        }
    }

    /**
     * Verify object value.
     * @param objectName the object name
     * @param propName the prop name
     * @param expectedValue the expected value
     * @return the boolean
     * @throws Exception the exception
     */
    public static Boolean verifyObjectValue(String objectName, String propName, String expectedValue) throws Exception {
        String actValue = getProperty(objectName, propName);
        if (StringUtils.contains(actValue, expectedValue)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the element.
     * @param objectName the object name
     * @return the element
     * @throws Exception the exception
     */
    public static WebElement getElement(String objectName) throws Exception {
        boolean elementFound = true;
        objectName = objectName.trim().toUpperCase();
        String xpath = ObjectReader.getProperty(objectName + "_XPATH");
        String htmlId = ObjectReader.getProperty(objectName + "_HTMLID");
        String cssSelector = ObjectReader.getProperty(objectName + "_CSS_SELECTOR");
        String name = ObjectReader.getProperty(objectName + "_NAME");
        String className = ObjectReader.getProperty(objectName + "_CLASSNAME");
        String linkText = ObjectReader.getProperty(objectName + "_LINKTEXT");
        String partialLinkText = ObjectReader.getProperty(objectName + "_PARTIAL_LINKTEXT");

        if (!StringUtils.isEmpty(xpath) && !StringUtils.isEmpty(htmlId) && !StringUtils.isEmpty(cssSelector)
                && !StringUtils.isEmpty(name) && !StringUtils.isEmpty(className) && !StringUtils.isEmpty(linkText)
                && !StringUtils.isEmpty(partialLinkText)) {
            throw new Exception("None of the Object properties has been specified");
        }

        if (!StringUtils.isEmpty(xpath)) {
            try {
                logger.info("Driver is trying to find the object using Xpath property");
                return waitForElementExist(By.xpath(xpath));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound && !StringUtils.isEmpty(htmlId)) {
            try {
                logger.info("Driver is trying to find the object using Id property");
                return waitForElementExist(By.id(htmlId));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound && !StringUtils.isEmpty(cssSelector)) {
            try {
                logger.info("Driver is trying to find the object using CSS Selector property");
                return waitForElementExist(By.cssSelector(cssSelector));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound && !StringUtils.isEmpty(name)) {
            try {
                logger.info("Driver is trying to find the object using Name property");
                return waitForElementExist(By.name(name));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound && !StringUtils.isEmpty(className)) {
            try {
                logger.info("Driver is trying to find the object using Class Name property");
                return waitForElementExist(By.className(className));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound && !StringUtils.isEmpty(linkText)) {
            try {
                logger.info("Driver is trying to find the object using Link Text property");
                return waitForElementExist(By.linkText(linkText));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound && !StringUtils.isEmpty(partialLinkText)) {
            try {
                logger.info("Driver is trying to find the object using Partial Link Text property");
                return waitForElementExist(By.partialLinkText(partialLinkText));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound) {
            throw new Exception("Object could not be found with any given properties.");
        }
        return null;
    }

    /**
     * Gets the elements.
     * @param objectName the object name
     * @return the elements
     * @throws Exception the exception
     */
    public static List<WebElement> getElements(String objectName) throws Exception {
        boolean elementFound = true;
        objectName = objectName.trim().toUpperCase();
        String xpath = ObjectReader.getProperty(objectName + "_XPATH");
        String htmlId = ObjectReader.getProperty(objectName + "_HTMLID");
        String cssSelector = ObjectReader.getProperty(objectName + "_CSS_SELECTOR");
        String name = ObjectReader.getProperty(objectName + "_NAME");
        String className = ObjectReader.getProperty(objectName + "_CLASSNAME");
        String linkText = ObjectReader.getProperty(objectName + "_LINKTEXT");
        String partialLinkText = ObjectReader.getProperty(objectName + "_PARTIAL_LINKTEXT");

        if (!StringUtils.isEmpty(xpath) && !StringUtils.isEmpty(htmlId) && !StringUtils.isEmpty(cssSelector)
                && !StringUtils.isEmpty(name) && !StringUtils.isEmpty(className) && !StringUtils.isEmpty(linkText)
                && !StringUtils.isEmpty(partialLinkText)) {
            throw new Exception("None of the Object properties has been specified");
        }

        if (!StringUtils.isEmpty(xpath)) {
            try {
                logger.info("Driver is trying to find the object using Xpath property");
                return waitForElementsExist(By.xpath(xpath));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound && !StringUtils.isEmpty(htmlId)) {
            try {
                logger.info("Driver is trying to find the object using Id property");
                return waitForElementsExist(By.id(htmlId));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound && !StringUtils.isEmpty(cssSelector)) {
            try {
                logger.info("Driver is trying to find the object using CSS Selector property");
                return waitForElementsExist(By.cssSelector(cssSelector));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound && !StringUtils.isEmpty(name)) {
            try {
                logger.info("Driver is trying to find the object using Name property");
                return waitForElementsExist(By.name(name));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound && !StringUtils.isEmpty(className)) {
            try {
                logger.info("Driver is trying to find the object using Class Name property");
                return waitForElementsExist(By.className(className));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound && !StringUtils.isEmpty(linkText)) {
            try {
                logger.info("Driver is trying to find the object using Link Text property");
                return waitForElementsExist(By.linkText(linkText));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound && !StringUtils.isEmpty(partialLinkText)) {
            try {
                logger.info("Driver is trying to find the object using Partial Link Text property");
                return waitForElementsExist(By.partialLinkText(partialLinkText));
            } catch (Exception e) {
                elementFound = false;
            }
        }

        if (!elementFound) {
            throw new Exception("Object could not be found with any given properties.");
        }
        return null;
    }

    /**
     * Wait for element exist.
     * @param object the object
     * @return the web element
     * @throws Exception the exception
     */
    public static WebElement waitForElementExist(By object) throws Exception {
        WebDriverWait wait = new WebDriverWait(StepDefs.driver, Long.parseLong(AppData.getProperty("waitLongSeconds")));
        wait.until(ExpectedConditions.presenceOfElementLocated(object));
        return StepDefs.driver.findElement(object);
    }

    /**
     * Wait for element exist.
     * @param object the object
     * @return the web element
     * @throws Exception the exception
     */
    public static List<WebElement> waitForElementsExist(By object) throws Exception {
        WebDriverWait wait = new WebDriverWait(StepDefs.driver, Long.parseLong(AppData.getProperty("waitLongSeconds")));
        wait.until(ExpectedConditions.presenceOfElementLocated(object));
        return StepDefs.driver.findElements(object);
    }

    /**
     * Random string.
     * @param length the length
     * @param stringCase the string case
     * @return the string
     * @throws Exception the exception
     */
    public static String randomString(int length, String... stringCase) throws Exception {
        String rndString = "";
        String letters = "";

        if (stringCase.length > 0) {
            if (StringUtils.equalsIgnoreCase("UCASE", stringCase[0])) {
                letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            } else if (StringUtils.equalsIgnoreCase("LCASE", stringCase[0])) {
                letters = "abcdefghijklmnopqrstuvwxyz";
            } else {
                letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            }
        } else {
            letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        }

        for (int i = 0; i < length; i++) {
            double a = Math.random();
            int x = (int) (a * 100);

            if (letters.length() == 26) {
                if (x > 25 && x < 52) {
                    x = x - 26;
                } else if (x > 51 && x < 78) {
                    x = x - 52;
                } else if (x > 77 && x < 99) {
                    x = x - 78;
                } else {
                    x = 1;
                }
            } else {
                if (x > 51 && x < 78) {
                    x = x - 26;
                } else if (x > 77 && x < 99) {
                    x = x - 52;
                } else {
                    x = 1;
                }
            }

            char letter = letters.charAt(x);
            rndString = rndString + String.valueOf(letter);
        }

        return rndString;
    }

    /**
     * Random number.
     * @param length the length
     * @return the string
     * @throws Exception the exception
     */
    public static String randomNumber(int length) throws Exception {
        String rndNumber = "";
        String numbers = "1234567890";

        for (int i = 0; i < length; i++) {
            double a = Math.random();
            int x = (int) (a * 10);

            if (x > 9) {
                x = x - 10;
            }
            char number = numbers.charAt(x);
            rndNumber = rndNumber + number;
        }
        return rndNumber;
    }
}
