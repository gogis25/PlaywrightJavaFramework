package tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.DataProviderUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameWorkBuildDataDrivenTest extends TestBase{

    @AfterMethod
    public void tearDown()
    {
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
        context.close();
        browser.close();
    }
    @DataProvider(name = "eventBookingData")
    public Object[][] eventBookingData() throws IOException {
       return DataProviderUtils.getJsonDataToMap("/src/test/resources/eventBookingData.json");
    }
    @Test(groups = {"framework"} ,dataProvider = "eventBookingData")
    public void DemoTest(HashMap<String,String> data){
        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        dashboardPage.waitForEventsToLoad();
        adminEventsPage.goTo();

        // CREATE EVENT
        adminEventsPage.createEvent
                (
                data.get("titlePrefix"),
                data.get("description"),
                data.get("category"),
                data.get("city"),
                data.get("venue"),
                data.get("DateTime"),
                data.get("price"),
                data.get("totalSeats")
                );


        // Step 2 : Find newly created event on the Event page
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        eventsPage.waitForEventsToLoad();
        Locator targetCard = eventsPage.findEventCard(data.get("titlePrefix"));
        int seatsNumberBeforeBooking = eventsPage.getSeatsCount(targetCard);
        System.out.println(seatsNumberBeforeBooking);
        eventsPage.proceedToBookingCard(targetCard);
        BookingFormPage bookingFormPage =  eventsPage.proceedToBookingCard(targetCard);
        bookingFormPage.fillAndConfirm(
                data.get("fullName"),
                data.get("email"),
                data.get("phone"));

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
        Locator targetCardAfterBooking = eventCardAfterBooking.filter(new Locator.FilterOptions().setHasText(data.get("titlePrefix")));
        String seatsTotalAfterBooking = targetCardAfterBooking.getByText("seats").innerText();
        System.out.println(seatsTotalAfterBooking);

        // AfterBooking Seats < BeforeBooking Seats
        int seatNumberAfterBooking = Integer.parseInt(seatsTotalAfterBooking.split(" ")[0]);

        //compare two numbers

        //Assert.assertTrue(seatsNumberBeforeBooking > seatNumberAfterBooking);
    }

}
