package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class MoreUIValidationTest {
    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;
    @BeforeMethod(alwaysRun = true)
    public void setUp()
    {
       playwright = Playwright.create();
       browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
       context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)   // Captures visual screenshots for the timeline
                .setSnapshots(true)     // Captures full DOM snapshots for interactive debugging
                .setSources(true));     // Includes your Java source code lines in the viewer
       page = context.newPage();
       page.navigate("https://rahulshettyacademy.com/loginpagePractise/");
    }
    @AfterMethod
    public void tearDown()
    {
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
        context.close();
        browser.close();
    }
    @Test
    public void ChildWindowHandle()
    {
        Locator blinkingText = page.locator(".blinkingText");
        //Locator targetBlinkingText = blinkingText.filter(new Locator.FilterOptions().setHasText("Free Access to InterviewQues/ResumeAssistance/Material"));
       // targetBlinkingText.click();
        Page newPage = context.waitForPage(()->blinkingText.first().click());
         // new page
        newPage.waitForLoadState();
        String childText = newPage.locator(".red").textContent();
        System.out.println(childText);
        String emailId = childText.split(" ")[4];
        //System.out.println(emailId);
        page.getByLabel("Username:").fill(emailId);
        System.out.println(page.getByLabel("Username:").inputValue());
        page.waitForTimeout(5000);
    }
    @Test(groups = {"smoke"})
    public void UIControl()
    {
        Locator userRdBtn = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("User"));
        userRdBtn.click();
        page.locator("#okayBtn").click();
        Assert.assertTrue(userRdBtn.isChecked());
        Locator checkBoxTerms = page.locator("#terms");
        checkBoxTerms.check();
        Assert.assertTrue(checkBoxTerms.isChecked());
        page.getByRole(AriaRole.COMBOBOX).selectOption("Consultant");
    }
  }

