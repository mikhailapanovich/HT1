package testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertTrue;

public class JenkinsTest {
    private String host = "http://localhost:8080";
    private String chromeDriverPath = "d:\\Portable_Programms\\drivers\\chromedriver.exe";
    StringBuffer verificationErrors = new StringBuffer();
    WebDriver driver = null;

    @BeforeClass
    public void beforeClass() throws Exception {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.setCapability("chrome.switches", Arrays.asList("--homepage=about:blank"));
        driver = new ChromeDriver(options);

        // open jenkins, authorize
        MainPage mainPage = new MainPage(driver, host);
        mainPage.goToThisPage().authorize("admin", "admin");
    }

    @AfterClass
    public void afterClass() {
        //driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            Assert.fail(verificationErrorString);
        }
    }

    @Test
    public void tst_newUserLifeCycle() {
        MainPage mainPage = new MainPage(driver, host);
        ManagePage managePage = new ManagePage(driver, host);
        SecurityRealmPage securityRealmPage = PageFactory.initElements(driver, SecurityRealmPage.class);


        // 1 После клика по ссылке «Manage Jenkins» на странице появляется элемент dt с текстом «Manage Users»
        // и элемент dd с текстом «Create/delete/modify users that can log in to this Jenkins».
        mainPage.clickOnHrefWithText("Manage Jenkins");
        assertTrue(managePage.isTextPresentInDt("Manage Users"));
        assertTrue(managePage.isTextPresentInDd("Create/delete/modify users that can log in to this Jenkins"));

        // 2 После клика по ссылке, внутри которой содержится элемент dt с текстом «Manage Users»,
        // становится доступна ссылка «Create User».
        managePage.clickOnHrefWithText("Manage Users");


        // 3 После клика по ссылке «Create User» появляется форма с тремя полями типа text
        // и двумя полями типа password, причём все поля должны быть пустыми.


        // 4 После заполнения полей формы («Username» = «someuser», «Password» = «somepassword»,
        // «Confirm password» = «somepassword», «Full name» = «Some Full Name»,
        // «E-mail address» = «some@addr.dom») и клика по кнопке с надписью «Create User»
        // на странице появляется строка таблицы (элемент tr),
        // в которой есть ячейка (элемент td) с текстом «someuser».


        // 5 После клика по ссылке с атрибутом href равным «user/someuser/delete»
        // появляется текст «Are you sure about deleting the user from Jenkins?».


        // 6 После клика по кнопке с надписью «Yes» на странице отсутствует строка таблицы (элемент tr),
        // с ячейкой (элемент td) с текстом «someuser». На странице отсутствует ссылка
        // с атрибутом href равным «user/someuser/delete».


        // 7 {На той же странице, без выполнения каких бы то ни было действий}.
        // На странице отсутствует ссылка с атрибутом href равным «user/admin/delete».
    }

    @Test(enabled = false)
    public void tst_createUserWithBlankName() {

    }

    @Test(enabled = false)
    public void tst_enableDisableAutoRefreshCycle() {

    }
}