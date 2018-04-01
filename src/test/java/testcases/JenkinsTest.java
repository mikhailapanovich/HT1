package testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class JenkinsTest {
    private String host = "http://localhost:8080";
    private String chromeDriverPath = "d:\\Portable_Programms\\drivers\\chromedriver.exe";
    private WebDriver driver = null;
    private StringBuffer verificationErrors;

    @BeforeClass
    public void beforeClass() throws Exception {
        ChromeOptions options = new ChromeOptions();
        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(chromeDriverPath))
                .usingAnyFreePort()
                .build();
        options.setCapability("chrome.switches", Arrays.asList("--homepage=about:blank"));
        driver = new ChromeDriver(service, options);

        // open jenkins, authorize
        MainPage mainPage = new MainPage(driver, host);
        mainPage.goToThisPage().authorize("admin", "admin");
    }

    @AfterClass
    public void afterClass() {
        //driver.quit();
    }

    @Test
    public void tst_temp() {
        SecurityRealmPage securityRealmPage = PageFactory.initElements(driver, SecurityRealmPage.class);
        securityRealmPage.goToThisPage();
        securityRealmPage.clickOnDeleteUser();
        String value = "Are you sure about deleting the user from Jenkins?";
        assertTrue(securityRealmPage.pageTextContains(value), "Expected to find [" + value + "]");
    }

    @Test
    public void tst_newUserLifeCycle() {
        MainPage mainPage = new MainPage(driver, host);
        ManagePage managePage = new ManagePage(driver, host);
        SecurityRealmPage securityRealmPage = PageFactory.initElements(driver, SecurityRealmPage.class);
        AddUser addUser = PageFactory.initElements(driver, AddUser.class);
        verificationErrors = new StringBuffer();
        String value;

        // 1 После клика по ссылке «Manage Jenkins» на странице появляется элемент dt с текстом «Manage Users»
        // и элемент dd с текстом «Create/delete/modify users that can log in to this Jenkins».
        mainPage.clickOnManageJenkins();
        assertTrue(managePage.isDtPresentWithText("Manage Users"));
        assertTrue(managePage.isDdPresentWithText("Create/delete/modify users that can log in to this Jenkins"));

        // 2 После клика по ссылке, внутри которой содержится элемент dt с текстом «Manage Users»,
        // становится доступна ссылка «Create User».
        managePage.clickOnHrefWithDescendantDtWithText("Manage Users");
        assertTrue(securityRealmPage.isCreateUserPresent(), "\"Create User\" link is not present!") ;

        // 3 После клика по ссылке «Create User» появляется форма с тремя полями типа text
        // и двумя полями типа password, причём все поля должны быть пустыми.
        securityRealmPage.clickOnCreateUser();
        verificationErrors.append(addUser.getMessageForFormPresent());

        // 4 После заполнения полей формы («Username» = «someuser», «Password» = «somepassword»,
        // «Confirm password» = «somepassword», «Full name» = «Some Full Name»,
        // «E-mail address» = «some@addr.dom») и клика по кнопке с надписью «Create User»
        // на странице появляется строка таблицы (элемент tr),
        // в которой есть ячейка (элемент td) с текстом «someuser».
        verificationErrors.append(addUser.sendForm("someuser", "somepassword", "somepassword", "Some Full Name", "some@addr.dom"));
        value = "someuser";
        assertTrue(securityRealmPage.isTableDataPresent(value), "There is no [" + value + "] in table data");

        // 5 После клика по ссылке с атрибутом href равным «user/someuser/delete»
        // появляется текст «Are you sure about deleting the user from Jenkins?».
        securityRealmPage.clickOnDeleteUser();
        value = "Are you sure about deleting the user from Jenkins?";
        assertTrue(securityRealmPage.pageTextContains(value), "Expected to find [" + value + "]");

        // 6 После клика по кнопке с надписью «Yes» на странице отсутствует строка таблицы (элемент tr),
        // с ячейкой (элемент td) с текстом «someuser». На странице отсутствует ссылка
        // с атрибутом href равным «user/someuser/delete».
        securityRealmPage.clickYes();
        value = "someuser";
        assertFalse(securityRealmPage.isTableDataPresent(value), "There is [" + value + "] in table data!");
        assertFalse(securityRealmPage.isDeleteUserPresent(), "There is href with [user/someuser/delete] present!");


        // 7 {На той же странице, без выполнения каких бы то ни было действий}.
        // На странице отсутствует ссылка с атрибутом href равным «user/admin/delete».
        assertFalse(securityRealmPage.isDeleteAdminPresent(), "There is href with [user/admin/delete] present!");


        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            Assert.fail(verificationErrorString);
        }
    }

    @Test(enabled = false)
    public void tst_createUserWithBlankName() {

    }

    @Test(enabled = false)
    public void tst_enableDisableAutoRefreshCycle() {

    }
}
