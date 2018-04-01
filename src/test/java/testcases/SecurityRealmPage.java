package testcases;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SecurityRealmPage {
    private final WebDriver driver;

    // Page elements templates
    @FindBy(xpath = "//a[text()='Create User']")
    private WebElement createUser;

    public SecurityRealmPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isCreateUserPresent() {
        try {
            createUser.getText();
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public SecurityRealmPage clickOnCreateUser() {
        createUser.click();
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