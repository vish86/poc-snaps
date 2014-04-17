/*
 * Copyright (c) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.snaplogic.snaps.calendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Events;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Main class for the Calendar API command line sample.
 * Demonstrates how to make an authenticated API call using OAuth 2 helper classes.
 */
public class MainTest {

    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "SnapLogic-GoogleCalendar";
    private static final String CLIENT_ID = "1011172173150-kr7te5v5cb5ot9b6o8hjse132shuigc0@developer.gserviceaccount.com";
    private static final File  SERVICE_ACCOUNT_P12FILE_LOCATION = new File(
            "/Users/vish/Documents/vish_new_workspace/calendar/src/test/resources/" +
                    "b07cc4003dc95d39c0d9b4eee865586eac6a62f4-privatekey.p12");
    private static final ArrayList<String> SCOPES = new ArrayList<String>(
            Arrays.asList("https://www.googleapis.com/auth/calendar"));

    /** Directory to store user credentials. */
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".store/calendar_sample");

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    @SuppressWarnings("unused")
    private static Calendar client;


    public static GoogleCredential createCredentialForServiceAccount(
            HttpTransport transport,
            JsonFactory jsonFactory,
            String CLIENT_ID,
            Collection<String> serviceAccountScopes,
            File SERVICE_ACCOUNT_P12FILE_LOCATION) throws GeneralSecurityException, IOException {
        return new GoogleCredential.Builder().setTransport(transport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(CLIENT_ID)
                .setServiceAccountScopes(serviceAccountScopes)
                .setServiceAccountPrivateKeyFromP12File(SERVICE_ACCOUNT_P12FILE_LOCATION)
                .build();
    }



    public static void main(String[] args) {
        try {
            // initialize the transport
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();


            // authorization
            //Credential credential = new GoogleCredential();

            Credential credential = createCredentialForServiceAccount(httpTransport,
                    JSON_FACTORY, CLIENT_ID, SCOPES, SERVICE_ACCOUNT_P12FILE_LOCATION);

            // set up global Calendar instance
            client = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();



            // Calendar list - list
            String pageToken = null;
            do {
                CalendarList calendarList = client.calendarList().list().setPageToken(pageToken)
                        .execute();
               // System.out.println("all calendars: " + calendarList.toString());
                List<CalendarListEntry> items = calendarList.getItems();

                for (CalendarListEntry calendarListEntry : items) {
                    System.out.println("Calendar list items: " + calendarListEntry.getId());
                    Events feed = client.events().list(calendarListEntry.getId()).execute();
                    //System.out.println("Events: get Description " + feed.getDescription());
                    System.out.println("Events:  get Items ");


                    for(com.google.api.services.calendar.model.Event e: feed.getItems())
                    {
                        System.out.println(e.toPrettyString());
                    }

//                    System.out.println("Events:  get Summary " + feed.getSummary());
//                    System.out.println("Events: get Next Page Token" + feed.getNextPageToken());

                }
                pageToken = calendarList.getNextPageToken();
            } while (pageToken != null);



        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);
    }
}
