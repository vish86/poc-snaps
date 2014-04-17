/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2014, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */
package com.snaplogic.snaps.calendar;

/**
 * Messages
 *
 * @author vish
 */
public class Messages {

    static final String GOOGLE_CAL_DESC = "Provides access to Google Calendar data";
    static final String GOOGLE_CAL_TITLE = "Google Calendar Read Events";
    static final String GOOGLE_OAUTH2_ACCOUNT_TITLE = "Google OAuth2";
    static final String ACCESS_TYPE = "access_type";
    static final String APPROVAL_PROMPT = "approval_prompt";
    static final String TOKEN_TRANSPORT = "token_transport";
    static final String ACCESS_TYPE_LABEL = "Access type";
    static final String ACCESS_TYPE_DESC = "The access type for the token, if offline, " +
            "then it will be persisted in the account";
    static final String APPROVAL_PROMPT_LABEL = "Approval prompt";
    static final String APPROVAL_PROMPT_DESC = "The approval type for the token";
    static final String TOKEN_TRANSPORT_LABEL = "Token transport";
    static final String TOKEN_TRANSPORT_DESC = "Defines how the token is transported, " +
            "e.g. in the header of the response";
    static final String ERR_SECURITY = "Security exception occurred while accessing the " +
            "Google Calendar API";
    static final String ERR_SECURITY_REASON = "Problem accessing Google Calendar API. Reason: %s";
    static final String ERR_SECURITY_RESOLUTION = "Verify that the access token is correct.";
    static final String ERR_IO = "IO Exception occurred while accessing Google Calendar API";
    static final String ERR_IO_REASON = "Problem accessing Google Calendar API. Reason: %s";
    static final String ERR_IO_RESOLUTION = "Check that Google Calendar API is accessible and " +
            "account has sufficient privileges.";
    static final String GOOGLE_CAL_SCOPES="https://www.googleapis.com/auth/calendar";
    static final String NO_DATA_AVAILABLE = "No data available.";
    static final String NO_CALENDAR_SELECTED_FAILED_REASON = "No calendar selected.";
    static final String NO_CALENDAR_SELECTED_FAILED_RESOLUTION = "Select a calendar, " +
            "if no calendars show up, verify the credentials and authenticate again.";
    static final String CALENDAR_EXECUTION_FAILED = "Calendar Query execution failed";
    static final String CALENDAR_EXECUTION_FAILED_RESOLUTION = "Please verify that the " +
            "credentials are correct and you have chosen a calendar you have access to.";


}