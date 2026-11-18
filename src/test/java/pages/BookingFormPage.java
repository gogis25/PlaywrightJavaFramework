package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingFormPage {
    Page page;
    private static final String customer_Name = "#customerName";
    private static final String customer_Email = "#customer-email";
    private static final String customer_Phone = "#phone";

    public BookingFormPage(Page page)
    {
        this.page = page;
    }
    public void fillAndConfirm(String name, String email, String phone)
    {
        page.locator(customer_Name).fill(name);
        page.locator(customer_Email).fill(email);
        page.locator(customer_Phone).fill(phone);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
    }
}
