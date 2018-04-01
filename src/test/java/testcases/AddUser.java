package testcases;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AddUser {
    private final WebDriver driver;

    // Page elements templates
    @FindBy(xpath = "//form[@method='post']")
    private List<WebElement> forms;

    @FindBy(xpath = "//button[@type='button']")
    private WebElement submit_button;

    @FindBy(name = "username")
    private WebElement username;

    @FindBy(name = "password1")
    private WebElement password1;

    @FindBy(name = "password2")
    private WebElement password2;

    @FindBy(name = "fullname")
    private WebElement fullname;

    @FindBy(name = "email")
    private WebElement email;


    public AddUser(WebDriver driver) {
        this.driver = driver;
    }

    public String sendForm(String username, String password, String confirmPassword, String fullname, String email) {
        StringBuilder message = new StringBuilder();
        message.append(setUsername(username));
        message.append(setPassword(password));
        message.append(confrimPassword(confirmPassword));
        message.append(setFullname(fullname));
        message.append(setEmail(email));
        message.append(submitForm());
        return message.toString();
    }

    public String submitForm() {
        try {
            submit_button.click();
        } catch (NoSuchElementException e) {
            return "Cannot submit form: there is no such element present!\n";
        }
        return "";
    }

    public String setUsername(String value) {
        try {
            username.sendKeys(value);
        } catch (NoSuchElementException e) {
            return "Cannot set username: there is no such element present!\n";
        }
        return "";
    }

    public String setFullname(String value) {
        try {
            fullname.sendKeys(value);
        } catch (NoSuchElementException e) {
            return "Cannot set fullname: there is no such element present\n";
        }
        return "";
    }

    public String setPassword(String value) {
        try {
            password1.sendKeys(value);
        } catch (NoSuchElementException e) {
            return "Cannot set username: there is no such element present\n";
        }
        return "";
    }

    public String confrimPassword(String value) {
        try {
            password2.sendKeys(value);
        } catch (NoSuchElementException e) {
            return "Cannot set password: there is no such element present\n";
        }
        return "";
    }

    public String setEmail(String value) {
        try {
            email.sendKeys(value);
        } catch (NoSuchElementException e) {
            return "Cannot set email: there is no such element present\n";
        }
        return "";
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
