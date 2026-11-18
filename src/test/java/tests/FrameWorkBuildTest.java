package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.*;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameWorkBuildTest extends TestBase{

    @AfterMethod
    public void tearDown()
    {
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
        context.close();
        browser.close();
    }
    @Test(groups = {"framework"} ,description = "create an event and verify if it's booked or not")
    public void DemoTest(){
        String event_Title = "Harshul Reetwal";
        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        dashboardPage.waitForEventsToLoad();
        adminEventsPage.goTo();
        // CREATE EVENT
        adminEventsPage.createEvent(
                event_Title,
                "Harshul is organizing a soccer event",
                "Sports",
                "Santa Clara",
                "Levis Stadium",
                "2026-11-27T23:00",
                "205",
                "215");


        // Step 2 : Find newly created event on the Event page
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        eventsPage.waitForEventsToLoad();
        Locator targetCard = eventsPage.findEventCard(event_Title);
        int seatsNumberBeforeBooking = eventsPage.getSeatsCount(targetCard);
        System.out.println(seatsNumberBeforeBooking);
        eventsPage.proceedToBookingCard(targetCard);
        BookingFormPage bookingFormPage =  eventsPage.proceedToBookingCard(targetCard);
        bookingFormPage.fillAndConfirm(
                "Gagandeep Sharma",
                "gagan@gmail.com",
                "9815065111");

        //Book the ticket for an event



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
        Locator targetCardAfterBooking = eventCardAfterBooking.filter(new Locator.FilterOptions().setHasText(event_Title));
        String seatsTotalAfterBooking = targetCardAfterBooking.getByText("seats").innerText();
        System.out.println(seatsTotalAfterBooking);

        // AfterBooking Seats < BeforeBooking Seats
        int seatNumberAfterBooking = Integer.parseInt(seatsTotalAfterBooking.split(" ")[0]);

        //compare two numbers

        //Assert.assertTrue(seatsNumberBeforeBooking > seatNumberAfterBooking);
    }

}
