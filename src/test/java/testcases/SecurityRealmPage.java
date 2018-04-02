package testcases;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SecurityRealmPage {
    private final WebDriver driver;
    private String host = "http://localhost:8080";

    // Page elements templates
    @FindBy(xpath = "//body")
    private WebElement body;

    @FindBy(xpath = "//a[text()='Create User']")
    private WebElement createUser;

    @FindBy(xpath = "//a[@href='user/someuser/delete']")
    private WebElement deleteUser;

    @FindBy(xpath = "//a[@href='user/admin/delete']")
    private WebElement deleteAdmin;

    @FindBy(xpath = "//button[@id='yui-gen1-button']")
    private WebElement confirmDeleteUser;

    public SecurityRealmPage(WebDriver driver) {
        this.driver = driver;
    }

    public SecurityRealmPage goToThisPage() {
        driver.get(host + "/securityRealm");
        return this;
    }

    public SecurityRealmPage goToDeleteUserPage() {
        driver.get(host + "/securityRealm/user/123/delete");
        return this;
    }

    public String getButtonColor() {
        return confirmDeleteUser.getCssValue("background-color");
    }

    public String errorMessageButtonColor() {
        if (!getButtonColor().equals("rgba(75, 117, 139, 1)")) {
            return "The Button color is not equal to #4b758b!";
        }
        return "";
    }

    public boolean isCreateUserPresent() {
        try {
            createUser.getText();
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public boolean isDeleteUserPresent() {
        try {
            deleteAdmin.getText();
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public boolean isDeleteAdminPresent() {
        try {
            deleteAdmin.getText();
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public SecurityRealmPage clickOnCreateUser() {
        createUser.click();
        return this;
    }

    public SecurityRealmPage clickOnDeleteUser() {
        deleteUser.click();
        return this;
    }

    public SecurityRealmPage clickYes() {
        confirmDeleteUser.click();
        return this;
    }

    public boolean pageTextContains(String search_string) {
        return body.getText().contains(search_string);
    }

    public boolean isTableDataPresent(String text) {
        waitForLoad();
        try {
            driver.findElement(By.xpath("//tr/td/*[text()='" + text + "']")).getText();
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void waitForLoad() {
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