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

    public ManagePage(WebDriver driver, String host) {
        this.driver = driver;
        this.host = host;
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

    public boolean isDtPresentWithText(String text) {
        return isTextPresentInTag(text, dt_locator);
    }

    public boolean isDdPresentWithText(String text) {
        return isTextPresentInTag(text, dd_locator);
    }

    public ManagePage clickOnHrefWithDescendantDtWithText(String text) {
        driver.findElement(By.xpath("//dt[text()='" + text + "']/ancestor::a")).click();
        return this;
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
