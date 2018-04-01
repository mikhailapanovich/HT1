package testcases;

import java.util.Collection;
import java.util.Iterator;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SecurityRealmPage {
    private final WebDriver driver;

    // Page elements
    @FindBy(xpath = "//a[text()='Create User']")
    private WebElement createUser;

    @FindBy(xpath = "//body")
    private WebElement body;

    @FindBy(xpath = "//form[@action='/testlab/wt/index.php']")
    private WebElement form;

    @FindBy(name = "name")
    private WebElement username;

    @FindBy(name = "weight")
    private WebElement weight;

    @FindBy(name = "height")
    private WebElement height;

    @FindBy(xpath = "//input[@name='gender'][@value='m']")
    private WebElement gender_m;

    @FindBy(xpath = "//input[@name='gender'][@value='f']")
    private WebElement gender_f;

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement submit_button;

    @FindBy(xpath = "//table/tbody/tr[2]/td[2]")
    private WebElement user_message;

    @FindBy(xpath = "//form/table/tbody/tr/td")
    private WebElement error_message;


    public SecurityRealmPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isHrefPresent(String text) {

        return false;
    }

    // Заполнение имени.
    public SecurityRealmPage setName(String value) {
        username.clear();
        username.sendKeys(value);
        return this;
    }

    // Заполнение веса.
    public SecurityRealmPage setWeight(String value) {
        weight.clear();
        weight.sendKeys(value);
        return this;
    }

    // Заполнение роста.
    public SecurityRealmPage setHeight(String value) {
        height.clear();
        height.sendKeys(value);
        return this;
    }

    // Указание пола.
    public SecurityRealmPage setGender(String value) {
        if (value.equals("m")) {
            gender_m.click();
        } else {
            gender_f.click();
        }
        return this;
    }

    // Заполнение всех полей формы.
    public SecurityRealmPage setFields(String name, String weight, String height, String gender) {
        setName(name);
        setWeight(weight);
        setHeight(height);
        setGender(gender);
        return this;
    }

    // Отправка данных из формы.
    public SecurityRealmPage submitForm() {
        submit_button.click();
        return this;
    }

    // Обёртка для упрощения отправки данных.
    public SecurityRealmPage submitFilledForm(String name, String weight, String height, String gender) {
        setFields(name, weight, height, gender);
        return submitForm();
    }

    // Упрощённый поиск формы.
    public boolean isFormPresent() {
        if (form != null) {
            return true;
        } else {
            return false;
        }
    }

    // Надёжный поиск формы.
    public boolean isFormPresentForReal() {
        // Первое (самое правильное) решение (работает примерно в 30-50% случаев)
        // wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//html/body"), 1));

        // Второе (самое интересное) решение (работает примерно в 20-30% случаев; не работает в 3.3.1)
        // waitForLoad(driver);

        // Третье (самое убогое, почти за гранью запрещённого) решение -- работает в 100% случаев

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Collection<WebElement> forms = driver.findElements(By.tagName("form"));
        if (forms.isEmpty()) {
            return false;
        }

        Iterator<WebElement> i = forms.iterator();
        boolean form_found = false;
        WebElement form = null;

        while (i.hasNext()) {
            form = i.next();
            if ((form.findElement(By.name("name")).getAttribute("type").equalsIgnoreCase("text")) &&
                    (form.findElement(By.name("height")).getAttribute("type").equalsIgnoreCase("text")) &&
                    (form.findElement(By.name("weight")).getAttribute("type").equalsIgnoreCase("text")) &&
                    (form.findElement(By.xpath("//input[@type=\"submit\"]")).getAttribute("value").equalsIgnoreCase("Рассчитать")) &&
                    (form.findElements(By.name("gender")).size() == 2) &&
                    (form.findElements(By.xpath("//input")).size() == 6)) {
                form_found = true;
                break;
            }
        }

        return form_found;
    }

    // Проверка вхождения подстроки в текст страницы.
    public boolean pageTextContains(String search_string) {
        return body.getText().contains(search_string);
    }

    // Проверка вхождения подстроки в пользовательское сообщение.
    public boolean userMessageContains(String search_string) {
        return user_message.getText().contains(search_string);
    }

    // Проверка равенства пользовательского сообщения строке.
    public boolean userMessageEquals(String search_string) {
        return user_message.getText().equals(search_string);
    }

    // Проверка вхождения подстроки в сообщение об ошибке.
    public boolean errorMessageContains(String search_string) {
        return error_message.getText().contains(search_string);
    }

    // Проверка равенства сообщения об ошибке строке.
    public boolean errorMessageEquals(String search_string) {
        return error_message.getText().equals(search_string);
    }

    // Получение значения имени.
    public String getName() {
        return username.getAttribute("value");
    }

    // Получение значения веса.
    public String getWeight() {
        return weight.getAttribute("value");
    }

    // Получение значения роста.
    public String getHeight() {
        return height.getAttribute("value");
    }

    // Получение значения пола.
    public String getGender() {
        if (gender_m.isSelected()) {
            return "m";
        } else if (gender_f.isSelected()) {
            return "f";
        } else {
            return "";
        }
    }

    public String getErrorOnTextAbsence(String search_string) {
        if (!pageTextContains(search_string)) {
            return "No '" + search_string + "' is found inside page text!\n";
        } else {
            return "";
        }
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