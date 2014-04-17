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

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.common.collect.ImmutableList;
import com.snaplogic.account.api.AccountType;
import com.snaplogic.account.api.capabilities.AccountCategory;
import com.snaplogic.api.ExecutionException;
import com.snaplogic.common.properties.SnapProperty;
import com.snaplogic.common.properties.builders.PropertyBuilder;
import com.snaplogic.snap.api.account.oauth2.OAuth2Account;
import com.snaplogic.snap.api.capabilities.General;
import com.snaplogic.snap.api.capabilities.Version;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.snaplogic.snaps.calendar.Constants.ACCESS_TYPE_DEFAULT;
import static com.snaplogic.snaps.calendar.Constants.APPLICATION_NAME;
import static com.snaplogic.snaps.calendar.Constants.APPROVAL_PROMPT_DEFAULT;
import static com.snaplogic.snaps.calendar.Constants.AUTH_ENDPOINT_URL;
import static com.snaplogic.snaps.calendar.Constants.CLIENT_ID_DEFAULT;
import static com.snaplogic.snaps.calendar.Constants.CLIENT_SECRET_DEFAULT;
import static com.snaplogic.snaps.calendar.Constants.TOKEN_ENDPOINT_URL;
import static com.snaplogic.snaps.calendar.Constants.TOKEN_TRANSPORT_DEFAULT;
import static com.snaplogic.snaps.calendar.Messages.*;



/**
 * The GoogleCalOAuthAccount that makes the authenticated request using the access token
 * of the {@link OAuth2Account} via Google's OAuth API's.
 *
 * @author vish
 */
@General(title = GOOGLE_OAUTH2_ACCOUNT_TITLE)
@Version(snap = 1)
@AccountCategory(type = AccountType.OAUTH2)
public class GoogleCalOAuthAccount extends OAuth2Account<Calendar> {
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    @Override
    public List<SnapProperty> defineAuthEndpointProperties(final PropertyBuilder propertyBuilder) {
        return new ImmutableList
                .Builder<SnapProperty>()
                .add(propertyBuilder.describe(ACCESS_TYPE, ACCESS_TYPE_LABEL, ACCESS_TYPE_DESC)
                        .makeReadOnly()
                        .defaultValue(ACCESS_TYPE_DEFAULT)
                        .build())
                .add(propertyBuilder.describe(APPROVAL_PROMPT, APPROVAL_PROMPT_LABEL,
                        APPROVAL_PROMPT_DESC)
                        .makeReadOnly()
                        .defaultValue(APPROVAL_PROMPT_DEFAULT)
                        .build())
                .build();
    }

    @Override
    public List<SnapProperty> defineTokenEndpointProperties(final PropertyBuilder
            propertyBuilder) {
        return new ImmutableList
                .Builder<SnapProperty>()
                .add(propertyBuilder.describe(TOKEN_TRANSPORT, TOKEN_TRANSPORT_LABEL,
                        TOKEN_TRANSPORT_DESC)
                        .makeReadOnly()
                        .defaultValue(TOKEN_TRANSPORT_DEFAULT)
                        .build())
                .build();
    }



    @Override
    public Calendar connect() throws ExecutionException {
        Credential credential = new GoogleCredential();
        credential.setAccessToken(getAccessToken());
        try {
            return new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

/**
 ** TODO: Build Calendar instance using Google's OAuth Service Account in the future.
 */

//            return new GoogleCredential.Builder().setTransport(GoogleNetHttpTransport.newTrustedTransport())
//                    .setJsonFactory(JSON_FACTORY)
//                    .setServiceAccountId(CLIENT_ID)
//                    .setServiceAccountScopes(serviceAccountScopes)
//                    .setServiceAccountPrivateKeyFromP12File(new
//                            java.io.File(SERVICE_ACCOUNT_P12FILE_LOCATION))
//                    .build();

        } catch (GeneralSecurityException e) {
            throw new ExecutionException(e, ERR_SECURITY)
                    .withReason(String.format(ERR_SECURITY_REASON, e.getLocalizedMessage()))
                    .withResolution(ERR_SECURITY_RESOLUTION);
        } catch (IOException e) {
            throw new ExecutionException(e, ERR_IO)
                    .withReason(String.format(ERR_IO_REASON, e.getLocalizedMessage()))
                    .withResolution(ERR_IO_RESOLUTION);
        }
    }

    @Override
    protected String setAuthorizationEndpoint() {
        return AUTH_ENDPOINT_URL;
    }

    @Override
    protected String setTokenEndpoint() {
        return TOKEN_ENDPOINT_URL;
    }

    @Override
    public void disconnect() throws ExecutionException {
        //NO OP
    }

    @Override
    protected String setDefaultScope() {
        return GOOGLE_CAL_SCOPES;
    }

    @Override
    protected String setDefaultClientId() {
        return CLIENT_ID_DEFAULT;
    }

    @Override
    protected String setDefaultClientSecret() {
        return CLIENT_SECRET_DEFAULT;
    }
}