package testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AddUser {
    private final WebDriver driver;

    // Page elements templates
    @FindBy(xpath = "//form[@method='post']")
    private List<WebElement> forms;

    public AddUser(WebDriver driver) {
        this.driver = driver;
    }

    public String getMessageForFormPresent() {
        StringBuilder message = new StringBuilder();
        boolean form_found = false;

        waitForLoad(driver);
        if (forms.isEmpty()) {
            return "There is no any [form] on the page [AddUser]!";
        }
        // present of three <input type="text">
        for (WebElement form : forms) {
            if ((form.findElements(By.xpath("//input[@type='text']")).size() == 3)) {
                form_found = true;
                break;
            }
        }
        if (!form_found) {
            message.append("There is no form with [3] input tags with type [text]!\n");
            form_found = false;
        }

        // present of two <input type="password">
        for (WebElement form : forms) {
            if ((form.findElements(By.xpath("//input[@type='password']")).size() == 2)) {
                form_found = true;
                break;
            }
        }
        if (!form_found) {
            message.append("There is no form with [2] input tags with type [password]!\n");
        }

        form_found = true;
        // check if fields are empty
        for (WebElement form : forms) {
            for (WebElement input : form.findElements(By.xpath("//input[@type='text'] | //input[@type='password']"))) {
                if (!input.getAttribute("value").equals("")) {
                    form_found = false;
                    break;
                }
            }
        }
        if (!form_found) {
            message.append("Some fields in form are not empty!\n");
        }

        return message.toString();
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
