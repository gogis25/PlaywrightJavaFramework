package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {
    Page page;
    String base_url;
    private static final String email_PlaceHolder = "you@email.com";
    private static final String password_Label = "Password";
    public LoginPage(Page page, String base_url)
    {
        this.page = page;
        this.base_url = base_url;
    }
    public DashboardPage loginToApplication() {

        page.navigate(base_url);
        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");  //wait 5 seconds
        page.getByPlaceholder(email_PlaceHolder).fill("gagandeep.sharma.011@gmail.com");
        page.getByLabel(password_Label).fill("Harshulg@1");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("sign in")).click();

        DashboardPage dashboardPage = new DashboardPage(page);
        return dashboardPage;
    }


}