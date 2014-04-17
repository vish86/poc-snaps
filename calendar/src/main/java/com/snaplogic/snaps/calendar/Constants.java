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
 * Constants
 *
 * @author vish
 */
public class Constants {
    static final String AUTH_ENDPOINT_URL = "https://accounts.google.com/o/oauth2/auth";
    static final String TOKEN_ENDPOINT_URL = "https://accounts.google.com/o/oauth2/token";
    static final String APPLICATION_NAME = "Snaplogic-GoogleCalendar";
    static final String CLIENT_ID_DEFAULT =
            "1011172173150-2guqq15k0mdtv0r2lghfq7v4jskd4di3.apps.googleusercontent.com";
    static final String CLIENT_SECRET_DEFAULT = "BtctuFgV6p2rvSd0fQNbZu2n";
    static final String ACCESS_TYPE_DEFAULT = "offline";
    static final String APPROVAL_PROMPT_DEFAULT = "force";
    static final String TOKEN_TRANSPORT_DEFAULT = "headers";
    static final String CALENDAR_NAME = "Calendar Name";
    static final String CALENDAR_DESC="Choose the Calendar to retrieve Items from";
    static final String ALL = "ALL";
}