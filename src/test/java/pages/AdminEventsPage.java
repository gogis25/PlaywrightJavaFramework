package pages;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventsPage {
    Page page;
    private static final String EVENT_TITLE_INPUT = "#event-title-input";
    private static final String DESCRIPTION_TEXTAREA = "#admin-event-form textarea";
    private static final String CATEGORY = "category";
    private static final String CITY_LABEL = "City";
    private static final String VENUE_LABEL = "Venue";
    private static final String DATE_TIME_LABEL = "Event Date & Time";
    private static final String PRICE_LABEL = "Price ($)";
    private static final String TOTAL_SEATS_LABEL = "#total-seats";
    private static final String ADD_EVENTS = "Add Event";
    private static final String SUCCESS_TOAST = "Event created!";
    public AdminEventsPage(Page page)

    {
        this.page = page;
    }
    public void goTo()
    {
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
    }
    public void createEvent(String Event_Title, String Desc_TextArea, String Category, String City,
                            String Venue, String DateTime, String Price, String Total_Seats)
    {
        page.locator(EVENT_TITLE_INPUT).fill(Event_Title);
        //page.getByPlaceholder("Describe the event…").fill("This is just for testing purpose.");
        page.locator(DESCRIPTION_TEXTAREA).fill(Desc_TextArea);
        page.getByLabel(CATEGORY).selectOption(Category);
        page.getByLabel(CITY_LABEL).fill(City);
        page.getByLabel(VENUE_LABEL).fill(Venue);
        page.getByLabel(DATE_TIME_LABEL).fill(DateTime);
        page.getByLabel(PRICE_LABEL).fill(Price);
        page.locator(TOTAL_SEATS_LABEL).fill(Total_Seats);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(ADD_EVENTS)).click();
        assertThat(page.getByText(SUCCESS_TOAST)).isVisible();  //wait 5 seconds
    }
}
