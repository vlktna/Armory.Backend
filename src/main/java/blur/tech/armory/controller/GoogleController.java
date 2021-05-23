package blur.tech.armory.controller;

import blur.tech.armory.controller.models.response.BookingResponse;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Data
@RestController
@RequestMapping(value = "/google")
@CrossOrigin(origins = {"*"}, allowedHeaders = {"*"})
public class GoogleController {

    private static final String APPLICATION_NAME = "Armory";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private Calendar client;
    private BookingResponse bookingResponse;
    private GoogleClientSecrets clientSecrets;
    private GoogleAuthorizationCodeFlow flow;
    private Credential credential;

    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;

    @RequestMapping(value = "/add-event", method = RequestMethod.GET, params = "code")
    public ResponseEntity<BookingResponse> addEvent(@RequestParam(value = "code") String code) throws IOException {
        addEventToCalendar(code);
        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }

    protected String authorize() throws Exception {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null) {
            GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                    Collections.singleton(CalendarScopes.CALENDAR)).build();
        }
        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
        System.out.println("cal authorizationUrl->" + authorizationUrl);
        return authorizationUrl.build();
    }

    protected void addEventToCalendar(String code) throws IOException {
        TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
        credential = flow.createAndStoreCredential(response, "userID");
        createEvent();
    }

    protected void addEventToCalendarWithAuthToken(String authToken) throws IOException {
        credential =
                new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(authToken);

        try {
            GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            createEvent();

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    private void createEvent() throws IOException {
        client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
        Calendar.Events events = client.events();

        Event event = new Event();

        event.setSummary(bookingResponse.getName());
        event.setLocation("Meeting room №" + bookingResponse.getMeetingRoomID());

        DateTime start = new DateTime(bookingResponse.getStartTime());
        DateTime end = new DateTime(bookingResponse.getEndTime());
        event.setStart(new EventDateTime().setDateTime(start));
        event.setEnd(new EventDateTime().setDateTime(end));

        events.insert("primary", event).execute();

    }
}