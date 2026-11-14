package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {

    private static final Logger log = LoggerFactory.getLogger(MockWebTest.class);
    Playwright playwright;
        Browser browser;
        BrowserContext context;
        Page page;

        @BeforeMethod
        public void setUp() {
            playwright = Playwright.create();
             browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));  // returns browser instance
            context = browser.newContext();
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)   // Captures visual screenshots for the timeline
                    .setSnapshots(true)     // Captures full DOM snapshots for interactive debugging
                    .setSources(true));     // Includes your Java source code lines in the viewer
            page = context.newPage();
            page.navigate("https://eventhub.rahulshettyacademy.com/login");
        }

        @AfterMethod
        public void tearDown() {
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get("trace.zip")));
            context.close();
            browser.close();
        }

        @Test(description = "create an event and verify if it's booked or not")
        public void DemoTest() {
            page.getByPlaceholder("you@email.com").fill("gagandeep.sharma.011@gmail.com");
            page.getByLabel("Password").fill("Harshulg@1");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("sign in")).click();
            assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

            // Step 1
            page.route("**/api/events**", route -> route.fulfill(
                    new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_cards_6.json"))
            ));
            page.navigate("https://eventhub.rahulshettyacademy.com/events");
            Locator eventCards = page.getByTestId("event-card");
            assertThat(eventCards.first()).isVisible();
            System.out.println("Event cards are present on the webPage :" +eventCards.count());
            Assert.assertEquals(eventCards.count(),6);
            page.waitForTimeout(3000);
            Assert.assertTrue(page.locator(".mx-1").first().isVisible());

            // Step 1
            page.route("**/api/events**", route -> route.fulfill(
                    new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_cards_5.json"))
            ));
            page.navigate("https://eventhub.rahulshettyacademy.com/events");
            Locator eventCards1 = page.getByTestId("event-card");
            assertThat(eventCards1.first()).isVisible();
            System.out.println("Event cards are present on the webPage :" +eventCards1.count());
            Assert.assertEquals(eventCards1.count(),5);
            page.waitForTimeout(3000);
            Assert.assertTrue(page.locator(".mx-1").first().isHidden());

        }
    }
