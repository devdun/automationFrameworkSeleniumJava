package utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.core.util.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Devdun.k on 5/22/2018.
 */
public class CommonOperations {
    private WebDriver driver;
    int sleepTime;
    Properties properties = null;

    public CommonOperations(WebDriver driver) {
        this.driver = driver;
    }

    public CommonOperations() {
        this.sleepTime = 1000;
    }

    public CommonOperations(int sleep, int tries) {
        this.sleepTime = sleep;
    }

    public Properties getProperties (String propertiesPath) {
        InputStream input = CommonOperations.class.getClassLoader().getResourceAsStream(propertiesPath + ".properties");
        properties = new Properties();
        try {
            if (input.available() > 0) {
                properties.load(input);
            }
        } catch (Exception e) {
        }
        return this.properties;
    }

    public boolean isPageLoading(WebDriver d) {
        JavascriptExecutor js = (JavascriptExecutor)d;
        String strExec = "return document.readyState!=\'complete\';";
        return ((Boolean)js.executeScript(strExec, new Object[0])).booleanValue();
    }

    public void waitUntilPageLoaded(WebDriver driver) {
        while(this.isPageLoading(driver)) {
            try {
                Thread.sleep((long)this.sleepTime);
            } catch (InterruptedException var3) {
                Thread.currentThread().interrupt();
            }
        }
    }

    //refresh page and wait until page load
    public void refreshPageAndWaitUntilLoad(){
        driver.navigate().refresh();
        this.waitUntilPageLoaded(driver);
    }

    public boolean waitForAnElementDisplayed(WebDriver driver, WebElement element , int tries, int sleepTime) {
        for(int count = 0; count < tries; ++count) {
            try {
                if(element.isDisplayed()) {
                    return true;
                }

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException var9) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception var10) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException var8) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        return false;
    }

    public int randomInt(int min, int max) {
        if(min > max) {
            int rand = min;
            min = max;
            max = rand;
        }

        Random rand1 = new Random();
        int randomNumber = rand1.nextInt(max - min + 1) + min;
        return randomNumber;
    }

    public void waitForSpecificTime(int sleepTime){
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public WebElement waitUntilElementVisible(WebDriver driver, WebElement element, int delay) {
        try{
            WebDriverWait wait = new WebDriverWait(driver, delay);
            return wait.until(ExpectedConditions.visibilityOf(element));
        }catch (NoSuchElementException e){
            throw new RuntimeException("Web element not visible within given time" + element +" Time "+ delay);
        }
    }

    public WebElement waitUntilElementVisible(WebDriver driver, By elementLocator, int delay) {
        try{
            WebDriverWait wait = new WebDriverWait(driver, delay);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
        }catch(NoSuchElementException e){
            throw new RuntimeException("Web element locator not visible within given time" + elementLocator +" Time "+ delay);
        }
    }

    public boolean waitUntilElementInvisible(WebDriver driver, By elementLocator, int delay) {
        try{
            WebDriverWait wait = new WebDriverWait(driver, delay);
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(elementLocator));
        }catch(NoSuchElementException e){
            throw new RuntimeException("Web element locator not In visible within given time" + elementLocator +" Time "+ delay);

        }
    }

    public WebElement waitForElementToBeVisible(By webElementLocator, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(webElementLocator));
    }

    public WebElement waitForElementToBePresence(WebDriver driver, By webElementLocator, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        return wait.until(ExpectedConditions.presenceOfElementLocated(webElementLocator));
    }

    public boolean waitForElementToBeHide(WebDriver driver, By webElementLocator, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(webElementLocator));
    }

    public WebElement waitForElementToBeClickable(WebDriver driver, WebElement element, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForElementToBeClickable(WebDriver driver, By webElementLocator, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        return wait.until(ExpectedConditions.elementToBeClickable(webElementLocator));
    }

    //select the dropdown using "select by visible text"
    public static void dropDownSelectByText(WebElement webElement, String VisibleText){
        Select selObj=new Select(webElement);
        selObj.selectByVisibleText(VisibleText);
    }

    //select the dropdown using "select by index"
    public static void dropDownSelectByIndex(WebElement webElement, int IndexValue){
        Select selObj=new Select(webElement);
        selObj.selectByIndex(IndexValue);
    }

    //select the dropdown using "select by value"
    public static void dropDownSelectByValue(WebElement webElement, String Value){
        Select selObj=new Select(webElement);
        selObj.selectByValue(Value);
    }

    //select radio button
    public void selectRadioButton(){

    }

    //select check boxes
    public void selectCheckBox(){

    }

    //scroll up web page
   public void scrollUp(){
       JavascriptExecutor js = (JavascriptExecutor) driver;
       js.executeScript("window.scrollBy(0,-250)", "");
   }

    //scroll down web page
    public void scrollDown(){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,250)", "");
    }

    //scroll vertical
    public void scrollVertical(){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(250,0)", "");
    }

    /**
     * Select file or image from local machine use robot framework to handle this.
     * This will most suitable since we can use for any platforl like linux or windows.
     **/

    public void selectFileImageFromLocal(String filePath){
        StringSelection stringSelection = new StringSelection(filePath);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        robot.delay(250);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(150);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public void moveToNewOpenedWindow(){
        // Storing parent window reference into a String Variable
        String parent = driver.getWindowHandle();

        // Set S1 will store number of windows opened by Webdriver
        Set<String> s1 = driver.getWindowHandles();

        // Now we will iterate using Iterator
        Iterator<String> I1 = s1.iterator();

        while (I1.hasNext()) {
            // Create reference for Window B
            String child_window = I1.next();

            // Here we will compare if parent window is not equal to child window then we will close
            if (!parent.equals(child_window)) {
                // Move Focus from Window A to Window B. Window B is active now
                driver.switchTo().window(child_window);
                System.out.println(driver.switchTo().window(child_window).getTitle());
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void takeScreenShotOnFailure(String fileName) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
        String testOutputPath = System.getProperty("user.dir").concat(File.separator).concat("output").concat(File.separator).concat("manualScreenshots");
        String filePath = testOutputPath.concat(File.separator).concat(dateFormat.format(date));
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(filePath + fileName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
