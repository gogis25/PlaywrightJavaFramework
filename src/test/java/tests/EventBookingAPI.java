package tests;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.annotations.Test;

import java.util.HashMap;

public class EventBookingAPI {

    @Test
    public void BookingDemo()
    {
        HashMap<Object,Object> bookPayload = new HashMap<>();
        bookPayload.put("customerName","Sundly Sharma");
        bookPayload.put("customerEmail","Sundly@gmail.com");
        bookPayload.put("customerPhone","9872895945");
        bookPayload.put("quantity","1");
        bookPayload.put("eventId", "32260");
        Playwright playwright = Playwright.create();
        APIRequestContext apiRequestContext = playwright.request().newContext();
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjE3OTU1LCJlbWFpbCI6ImdhZ2FuZGVlcC5zaGFybWEuMDExQGdtYWlsLmNvbSIsImlhdCI6MTc5MzgxMDYwMCwiZXhwIjoxNzk0NDE1NDAwfQ.PlIzd7TzErFkZ6pEeK956eUanLXcrL6AMiifA1gyLPg";
       //Created booking
        APIResponse bookingResponse = apiRequestContext.post("https://api.eventhub.rahulshettyacademy.com/api/bookings",
                RequestOptions.create().setHeader("Authorization", "Bearer "+token).setData(bookPayload));
        System.out.println(bookingResponse.statusText());
    }
}
