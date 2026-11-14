package tests;

import com.microsoft.playwright.*;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestBase {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
    String base_url;
    @BeforeMethod
    public void setUp() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);
        //mvn test -PRegression -Dbrpwser = chrome
        String browserName = System.getProperty("browser")!=null ? System.getProperty("browser"):prop.getProperty("browser");
        String envName = System.getProperty("env")!=null ? System.getProperty("env"):prop.getProperty("env");
        //String browserName = prop.getProperty("browser");
        playwright=Playwright.create();

        BrowserType type = switch (browserName) {
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            default -> playwright.chromium();
        };
        browser = type.launch((new BrowserType.LaunchOptions().setHeadless(true)));

        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)   // Captures visual screenshots for the timeline
                .setSnapshots(true)     // Captures full DOM snapshots for interactive debugging
                .setSources(true));     // Includes your Java source code lines in the viewer
        page = context.newPage();
        base_url = prop.getProperty(envName+".baseurl");

    }
}
