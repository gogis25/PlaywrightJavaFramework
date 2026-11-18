package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class APITesting {
    @Test
    public void e2eApiTest() {
        //Login

        HashMap<Object, Object> loginPayLoad = new HashMap<>();
        loginPayLoad.put("email", "gagandeep.sharma.011@gmail.com");
        loginPayLoad.put("password", "Harshulg@1");
        Playwright playwright = Playwright.create();
        APIRequestContext apiRequest = playwright.request().newContext();
        APIResponse loginResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(loginPayLoad));
        //Assert.assertTrue(loginResponse.ok());
        System.out.println(loginResponse.text());
        String token = JsonPath.read(loginResponse.text(), "$.token");
        System.out.println("Login successful" + token);

        //Create Event
        HashMap<Object, Object> createEventPayload = new HashMap<>();
        createEventPayload.put("title", "Playwright API Testing");
        createEventPayload.put("description", "This is just for API testing.");
        createEventPayload.put("category", "This is just for API testing.");
        createEventPayload.put("venue", "This is just for API testing.");
        createEventPayload.put("city", "This is just for API testing.");
        createEventPayload.put("eventDate", "2027-11-28T20:52:00.000Z");
        createEventPayload.put("price", "15");
        createEventPayload.put("totalSeats", "150");
        createEventPayload.put("availableSeats", "120");

        APIResponse eventResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + token)
                        .setData(createEventPayload));
        // Correct way: Only alerts you if it fails
        //Assert.assertTrue(eventResponse.ok(), "Event creation failed! Status code: " + eventResponse.status());

        // UNCOMMENT THIS LINE to see the actual error from the server in your console!
        //System.out.println("SERVER ERROR DETAIL: " + eventResponse.text());

        String responseBody = eventResponse.text();
        System.out.println("--- RAW SERVER RESPONSE ---");
        System.out.println(responseBody);
        System.out.println("---------------------------");
        //String eventId = JsonPath.read(eventResponse.text(), "$.data.id").toString();
        int eventId = JsonPath.read(responseBody, "$.data.id");

        System.out.println("Event created and its ID is : " + eventId);

        // Get Event

        APIResponse retrieveEvents = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page", "1")
                .setQueryParam("limit", "12")
                .setHeader("Authorization", "Bearer " + token));
        System.out.println("Testing---------------------------");
        Assert.assertTrue(retrieveEvents.ok(), "Retrieve event success");
        List<Integer> allEvents = JsonPath.read(retrieveEvents.text(), "$.data[*].id");
        Assert.assertTrue(allEvents.contains(eventId));

        // DELETE Event

       APIResponse deleteResponse = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventId,
               RequestOptions.create()
              .setHeader("Authorization", "Bearer " + token));
        //Get Request
        APIResponse deleteretrieveEvents = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create()
                        .setQueryParam("page", "1")
                .setQueryParam("limit", "12").setHeader("Authorization", "Bearer " + token));
        Assert.assertTrue(deleteResponse.ok(),eventId+" got deleted");
        System.out.println("Event deleted and its ID is : " + eventId);

        List<Integer> idsAfterDelete = JsonPath.read(deleteretrieveEvents.text(), "$.data[*].id"); // Get list of only Events IDs
        Assert.assertFalse(idsAfterDelete.contains(eventId));
        }
    }