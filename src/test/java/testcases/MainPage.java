package testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;


public class MainPage {
    private String host;
    private final WebDriver driver;

    // page elements
    private By usernameLocator = By.id("j_username");
    private By passwordLocator = By.name("j_password");
    private By submitButtonLocator = By.id("yui-gen1-button");
    private By manageJenkinsLocator = By.xpath("//a[text()='Manage Jenkins']");

    public MainPage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.host = baseUrl;
    }

    public MainPage goToThisPage() {
        driver.get(host);
        return this;
    }

    public MainPage authorize(String username, String password) {
        driver.get(host + "/login");
        waitForLoad(driver);
        driver.findElement(usernameLocator).sendKeys(username);
        driver.findElement(passwordLocator).sendKeys(password);
        driver.findElement(submitButtonLocator).click();
        return this;
    }

    public MainPage clickOnManageJenkins() {
        waitForLoad(driver);
        driver.findElement(manageJenkinsLocator).click();
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




