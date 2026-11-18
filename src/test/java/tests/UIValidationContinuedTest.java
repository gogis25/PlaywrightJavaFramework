package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class UIValidationContinuedTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
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
        page.setDefaultTimeout(12000);
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
    }
    @Test(groups = {"smoke"})
    public void popupValidations()
    {
       Locator hideShow = page.getByPlaceholder("Hide/Show Example");
       Assert.assertTrue(hideShow.isVisible());
       page.locator("#hide-textbox").click();
       Assert.assertTrue(hideShow.isHidden());
       page.locator("#name").fill("Gagandeep Sharma Reetwal");
       page.onDialog(dialog -> dialog.accept());
       page.locator("#alertbtn").click();
       page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Mouse Hover")).hover();
       //page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Reload")).click();
      FrameLocator framePages = page.frameLocator("#courses-iframe");
      framePages.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Learning Paths")).click();
     String text =  framePages.locator(".inner-box h1").innerText();
      System.out.println(text);
    }
    @Test
    public void screenShotTest()
    {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("PageScreenShot.png")));
        Locator displayedEditBox = page.getByPlaceholder("Hide/Show Example");
        displayedEditBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("editBoxScreenShot.png")));
        page.locator("#hide-textbox").click();
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("PagePostScreenShot.png")));
    }
}
