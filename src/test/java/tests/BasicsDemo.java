package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicsDemo {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
    @BeforeMethod
    public void setUp(){
        playwright=Playwright.create();
        //Browser browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));  // returns browser instance
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)   // Captures visual screenshots for the timeline
                .setSnapshots(true)     // Captures full DOM snapshots for interactive debugging
                .setSources(true));     // Includes your Java source code lines in the viewer
        page = context.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
    }
    @AfterMethod
    public void tearDown()
    {
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
        context.close();
        browser.close();
    }
    @Test(description = "create an event and verify if it's booked or not")
    public void DemoTest(){

        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");  //wait 5 seconds
        page.getByPlaceholder("you@email.com").fill("gagandeep.sharma.011@gmail.com");
        page.getByLabel("Password").fill("Harshulg@1");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("sign in")).click();
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        // Step 1

        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
        page.locator("#event-title-input").fill("Gagandeep Sharma Event");
        //page.getByPlaceholder("Describe the event…").fill("This is just for testing purpose.");
        page.locator("#admin-event-form textarea").fill("This is just for testing purpose.");
        page.getByLabel("category").selectOption("Sports");
        page.getByLabel("City").fill("Bareta, Mansa");
        page.getByLabel("Venue").fill("Test: Venue");
        page.getByLabel("Event Date & Time").fill("2026-11-27T23:00");
        page.getByLabel("Price ($)").fill("50");
        page.locator("#total-seats").fill("100");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Event")).click();
        assertThat(page.getByText("Event created!")).isVisible();  //wait 5 seconds

        // Step 2 : Find newly created event on the Event page

        page.locator("#nav-events").click();
        Locator eventCard = page.getByTestId("event-card");
        Locator targetCard = eventCard.filter(new Locator.FilterOptions().setHasText("Gagandeep Sharma Event"));
        assertThat(targetCard).isVisible(); //wait 5 seconds
        String seatsTotal = targetCard.getByText("seats").innerText();
        int seatsNumberBeforeBooking = Integer.parseInt(seatsTotal.split(" ")[0]);
        System.out.println(seatsTotal);
        targetCard.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Book Now")).click();

        //Book the ticket for an event

        page.locator("#customerName").fill("Sundly Sharma");
        page.locator("#customer-email").fill("sundlibaatish@gmail.com");
        page.locator("#phone").fill("9815098150");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingRef = page.locator(".booking-ref").innerText();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();
       // String seatsAvailable = targetCard.getByText("seats").innerText();
      //  System.out.println(seatsAvailable);
        //assertThat(element).hasText("Success");
       // page.waitForTimeout(3000);

        //Verify in Booking History
       Locator bookingcards =  page.locator("#booking-card");
       Locator targetBooking = bookingcards.filter(new Locator.FilterOptions().setHasText(bookingRef));
       assertThat(targetBooking).isVisible();

       //seat count reduction check

        page.locator("#nav-events").click();
        page.waitForTimeout(2000);
        Locator eventCardAfterBooking = page.getByTestId("event-card");
        Locator targetCardAfterBooking = eventCardAfterBooking.filter(new Locator.FilterOptions().setHasText("Gagandeep Sharma Event"));
        String seatsTotalAfterBooking = targetCardAfterBooking.getByText("seats").innerText();
        System.out.println(seatsTotalAfterBooking);

        // AfterBooking Seats < BeforeBooking Seats
        int seatNumberAfterBooking = Integer.parseInt(seatsTotalAfterBooking.split(" ")[0]);

        //compare two numbers

        //Assert.assertTrue(seatsNumberBeforeBooking > seatNumberAfterBooking);
    }

}
