package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage {

    Page page;
    private static final String EVENTS_CARDS = "event-card";
    public EventsPage(Page page)
    {
        this.page = page;
    }
    public void goTo()
    {
        page.locator("#nav-events").click();
    }
    public Locator waitForEventsToLoad()
    {
        Locator eventCard = page.getByTestId(EVENTS_CARDS);
        assertThat(eventCard.first()).isVisible();
        return eventCard;

    }
    public Locator findEventCard(String titleCard)
    {
        Locator eventCard = waitForEventsToLoad();
        Locator targetCard = eventCard.filter(new Locator.FilterOptions().setHasText(titleCard));
        assertThat(targetCard).isVisible();
        return targetCard;
    }
    public int getSeatsCount(Locator targetCard)
    {
        String seatsTotal = targetCard.getByText("seats").innerText();
        return Integer.parseInt(seatsTotal.split(" ")[0]);
    }
    public BookingFormPage proceedToBookingCard(Locator targetCard)
    {
        //Locator targetCard = findEventCard(titleCard);
        targetCard.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Book Now")).click();
        return new BookingFormPage(page);
    }
}
