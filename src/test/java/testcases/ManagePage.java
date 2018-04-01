package testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ManagePage {
    private String host;
    private final WebDriver driver;

    private By dt_locator = By.cssSelector("dt");
    private By dd_locator = By.cssSelector("dd");
    private By manageUsers_link = By.xpath("//a/dl/dt[text()='" + "']");

    public ManagePage(WebDriver driver, String host) {
        this.driver = driver;
        this.host = host;
    }

    public ManagePage clickOnHrefWithText(String text) {
        driver.findElement(By.xpath("//a[text()='" + text + "']")).click();
        return this;
    }

    public boolean isTextPresentInTag(String text, By locator) {
        waitForLoad(driver);
        for (WebElement dtElement : driver.findElements(locator)) {
            if (dtElement.getText().equals(text)) {
                return true;
            }
        }
        return false;
    }

    public boolean isTextPresentInDt(String text) {
        return isTextPresentInTag(text, dt_locator);
    }

    public boolean isTextPresentInDd(String text) {
        return isTextPresentInTag(text, dd_locator);
    }

    public void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }
}
