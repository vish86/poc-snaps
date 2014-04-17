/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2013, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.snaps.calendar;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Events;
import com.google.inject.Inject;
import com.snaplogic.account.api.capabilities.Accounts;
import com.snaplogic.api.ConfigurationException;
import com.snaplogic.api.ExecutionException;
import com.snaplogic.common.properties.Suggestions;
import com.snaplogic.common.properties.builders.PropertyBuilder;
import com.snaplogic.common.properties.builders.SuggestionBuilder;
import com.snaplogic.snap.api.Document;
import com.snaplogic.snap.api.DocumentUtility;
import com.snaplogic.snap.api.OutputViews;
import com.snaplogic.snap.api.PropertyValues;
import com.snaplogic.snap.api.SimpleSnap;
import com.snaplogic.snap.api.SnapCategory;
import com.snaplogic.snap.api.SnapDataException;
import com.snaplogic.snap.api.capabilities.Category;
import com.snaplogic.snap.api.capabilities.Errors;
import com.snaplogic.snap.api.capabilities.General;
import com.snaplogic.snap.api.capabilities.Inputs;
import com.snaplogic.snap.api.capabilities.Outputs;
import com.snaplogic.snap.api.capabilities.Version;
import com.snaplogic.snap.api.capabilities.ViewType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.snaplogic.snaps.calendar.Constants.ALL;
import static com.snaplogic.snaps.calendar.Constants.CALENDAR_DESC;
import static com.snaplogic.snaps.calendar.Constants.CALENDAR_NAME;
import static com.snaplogic.snaps.calendar.Messages.GOOGLE_CAL_DESC;
import static com.snaplogic.snaps.calendar.Messages.GOOGLE_CAL_TITLE;
import static com.snaplogic.snaps.calendar.Messages.NO_CALENDAR_SELECTED_FAILED_REASON;
import static com.snaplogic.snaps.calendar.Messages.NO_CALENDAR_SELECTED_FAILED_RESOLUTION;
import static com.snaplogic.snaps.calendar.Messages.NO_DATA_AVAILABLE;

/**
 * A snap that generates the configured number of documents.
 *
 * @author vish
 */
@General(title = GOOGLE_CAL_TITLE, purpose = GOOGLE_CAL_DESC)
@Inputs(min = 0, max = 0)
@Outputs(min = 1, max = 1, offers = {ViewType.DOCUMENT})
@Errors(min = 0, max = 1, offers = {ViewType.DOCUMENT})
@Version(snap = 1)
@Category(snap = SnapCategory.READ)
@Accounts(provides = GoogleCalOAuthAccount.class)
public class CalendarRead extends SimpleSnap {

    private static Calendar calendarClient;
    private static String calendarName = null;

    // Document utility is the only way to create a document
    // or manipulate the document header
    @Inject
    private DocumentUtility documentUtility;
    @Inject
    private OutputViews outputViews;
    @Inject
    private GoogleCalOAuthAccount account;


    @Override
    public void defineProperties(PropertyBuilder propertyBuilder) {
//        propertyBuilder.describe(CALENDAR_NAME, CALENDAR_DESC)
//                .type(SnapType.STRING).add();
        propertyBuilder.describe(CALENDAR_NAME, CALENDAR_DESC)
                .withSuggestions(new Suggestions() {
                    @Override
                    public void suggest(final SuggestionBuilder suggestionBuilder,
                            final PropertyValues propertyValues) {
                        /**
                         * TODO:  Move get Calendar List to a separate method and reuse at getItems
                         */
                        calendarClient = account.connect();
                        String pageToken = null;
                        List<String> calendarsList = new ArrayList();

                        try {
                        do {
                            CalendarList calendarList = calendarClient.calendarList().list().setPageToken
                                    (pageToken)
                                    .execute();

                                List<CalendarListEntry> items = calendarList.getItems();
                                for (CalendarListEntry calendarListEntry : items) {
                                    calendarsList.add(calendarListEntry.getId());
                                }

                            pageToken = calendarList.getNextPageToken();
                        } while (pageToken != null);
                        }
                        catch (Exception e)
                        {

                        }

                        if (calendarsList != null && !calendarsList.isEmpty())
                        {
                            suggestionBuilder.node(CALENDAR_NAME)
                                    .suggestions(calendarsList.toArray(new String[0]));
                        }

                    }
                })
                .required()
                .add();
    }

    @Override
    public void configure(PropertyValues propertyValues) throws ConfigurationException {
        calendarName = propertyValues.get(CALENDAR_NAME);
    }


    @Override
    public void cleanup() throws ExecutionException {
        // NOOP
    }

    @Override
    protected void process(final Document document, final String inputViewName){
        // Get property names from configure and call getItems w/ calendarName
        if(calendarName != null)
        {
            getItems(calendarName);
        }
        else
        {
            throw new SnapDataException(documentUtility
                    .newDocument(NO_DATA_AVAILABLE), NO_DATA_AVAILABLE)
                    .withReason(NO_CALENDAR_SELECTED_FAILED_REASON)
                    .withResolution(NO_CALENDAR_SELECTED_FAILED_RESOLUTION);
        }
    }



    private void getItems (String calendarName){
        try {
        calendarClient = account.connect();
        String pageToken = null;
        do {
            CalendarList calendarList = calendarClient.calendarList().list().setPageToken
                    (pageToken)
                    .execute();

            if (calendarName.equalsIgnoreCase(ALL))
            {
                List<CalendarListEntry> items = calendarList.getItems();
                for (CalendarListEntry calendarListEntry : items) {
                    //System.out.println("Calendar list items: " + calendarListEntry.getId());
                    Events feed = calendarClient.events().list(calendarListEntry.getId()).execute();
                    //System.out.println("Events: "  + feed.getItems());
                    outputViews.write(documentUtility.newDocument(feed.getItems()));
                }
            }
            else
            {
                Events feed = calendarClient.events().list(calendarName).execute();
                //System.out.println("Events: "  + feed.getItems());
                outputViews.write(documentUtility.newDocument(feed.getItems()));
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);
        }

     catch (IOException e) {
        System.err.println(e.getMessage());
         // THrow snap data exception

    } catch (Throwable t) {
        t.printStackTrace();
    }
    }

}
