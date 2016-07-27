/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2016. Viнt@rь
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2016. Viнt@rь
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.snl.facebook;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.Utility;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.reflect.TypeToken;
import com.snl.core.SocialNetwork;
import com.snl.core.SocialNetworkException;
import com.snl.core.listener.OnCheckIsFriendListener;
import com.snl.core.listener.OnLoginListener;
import com.snl.core.listener.OnRequestDetailedSocialPersonListener;
import com.snl.core.listener.OnRequestFriendsListener;
import com.snl.core.listener.OnRequestSocialPersonListener;
import com.snl.core.listener.OnRequestSocialPersonsListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Viнt@rь on 28.11.2015
 */
public class FacebookSocialNetwork extends SocialNetwork<AccessToken> {
    public static final int ID = 1;

    private LoginManager mLoginManager;
    private CallbackManager mCallbackManager;

    private List<String> mPermissions;

    public FacebookSocialNetwork(Application application, List<String> permissions) {
        super(application);
        String applicationId = Utility.getMetadataApplicationId(application);

        if (applicationId == null) {
            throw new IllegalStateException("applicationId can't be null\n" +
                    "Please check https://developers.facebook.com/docs/android/getting-started/");
        }
        AppEventsLogger.activateApp(application, applicationId);

        mPermissions = permissions;

        mLoginManager = LoginManager.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Activity getContext() {
        return (Activity) super.getContext();
    }

    @Override
    public AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    @Override
    public boolean isConnected() {
        return getAccessToken() != null;
    }

    @Override
    public void logout() {
        mLoginManager.logOut();
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public void requestLogin(final OnLoginListener listener) {
        super.requestLogin(listener);

        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (isRegistered(listener)) {
                    listener.onLoginSuccess(getId());
                    cancelLoginRequest();
                }
            }

            @Override
            public void onCancel() {
                if (isRegistered(listener)) {
                    listener.onError(getId(), Request.LOGIN, null, null);
                    cancelLoginRequest();
                }
            }

            @Override
            public void onError(FacebookException error) {
                if (isRegistered(listener)) {
                    listener.onError(getId(), Request.LOGIN, error.getMessage(), null);
                    cancelLoginRequest();
                }
            }
        });
        mLoginManager.logInWithReadPermissions(getContext(), mPermissions);
    }

    @Override
    public void requestCurrentPerson(final OnRequestSocialPersonListener listener) {
        super.requestCurrentPerson(listener);

        if (!isConnected()) {
            if (isRegistered(listener)) {
                listener.onError(getId(), Request.PERSON, "Please login first", null);
                cancelCurrentPersonRequest();
            }
            return;
        }

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, link, email");
        GraphRequest request = GraphRequest.newMeRequest(getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (isRegistered(listener)) {
                    if (response.getError() == null) {
                        listener.onRequestSocialPersonSuccess(getId(), parsePerson(object));
                    } else {
                        listener.onError(getId(), Request.PERSON, response.getError().getErrorMessage(), null);
                    }
                    cancelCurrentPersonRequest();
                }
            }
        });
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void requestDetailedCurrentPerson(final OnRequestDetailedSocialPersonListener listener) {
        super.requestDetailedCurrentPerson(listener);

        if (!isConnected()) {
            if (isRegistered(listener)) {
                listener.onError(getId(), Request.DETAIL_PERSON, "Please login first", null);
                cancelDetailedCurrentPersonRequest();
            }
            return;
        }

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, first_name, middle_name, last_name, link, email, gender, birthday, verified");
        GraphRequest request = GraphRequest.newMeRequest(getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (isRegistered(listener)) {
                    if (response.getError() == null) {
                        listener.onRequestDetailedSocialPersonSuccess(getId(), parsePerson(object));
                    } else {
                        listener.onError(getId(), Request.DETAIL_PERSON, response.getError().getErrorMessage(), null);
                    }
                    cancelDetailedCurrentPersonRequest();
                }
            }
        });
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Not supported via Facebook sdk.
     *
     * @param userId   user id in social network
     * @param listener listener for request {@link com.snl.core.SocialPerson}
     * @throws SocialNetworkException
     */
    @Override
    public void requestSocialPerson(String userId, OnRequestSocialPersonListener listener) {
        throw new SocialNetworkException("requestSocialPerson isn't allowed for FacebookSocialNetwork");
    }

    /**
     * Not supported via Facebook sdk.
     *
     * @param userId   array of user ids in social network
     * @param listener listener for request ArrayList of {@link com.snl.core.SocialPerson}
     * @throws SocialNetworkException
     */
    @Override
    public void requestSocialPersons(String[] userId, OnRequestSocialPersonsListener listener) {
        throw new SocialNetworkException("requestSocialPersons isn't allowed for FacebookSocialNetwork");
    }

    /**
     * Not supported via Facebook sdk.
     *
     * @param userId   user id in social network
     * @param listener listener for request {@link FacebookPerson}
     * @throws SocialNetworkException
     */
    @Override
    public void requestDetailedSocialPerson(String userId, OnRequestDetailedSocialPersonListener listener) {
        throw new SocialNetworkException("requestDetailedSocialPerson isn't allowed for FacebookSocialNetwork");
    }

    /**
     * Not supported via Facebook sdk
     *
     * @param userId   user id that should be checked as friend of current user
     * @param listener listener for checking friend request
     */
    @Override
    public void requestCheckIsFriend(String userId, OnCheckIsFriendListener listener) {
        throw new SocialNetworkException("requestCheckIsFriend isn't allowed for FacebookSocialNetwork");
    }

    @Override
    public void requestFriends(final OnRequestFriendsListener listener) {
        super.requestFriends(listener);

        if (!isConnected()) {
            if (isRegistered(listener)) {
                listener.onError(getId(), Request.FRIENDS, "Please login first", null);
                cancelFriendsRequest();
            }
            return;
        }

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, link, email");
        GraphRequest request = GraphRequest.newMyFriendsRequest(getAccessToken(), new GraphRequest.GraphJSONArrayCallback() {
            @Override
            public void onCompleted(JSONArray objects, GraphResponse response) {
                if (isRegistered(listener)) {
                    if (response.getError() == null) {
                        listener.onRequestFriendsSuccess(getId(), parsePersons(objects));
                    } else {
                        listener.onError(getId(), Request.FRIENDS, response.getError().getErrorMessage(), null);
                    }
                    cancelFriendsRequest();
                }
            }
        });
        request.setParameters(parameters);
        request.executeAsync();
    }

    private static FacebookPerson parsePerson(JSONObject person) {
        return GSON.fromJson(person.toString(), FacebookPerson.class);
    }

    private static List<FacebookPerson> parsePersons(JSONArray persons) {
        return GSON.fromJson(persons.toString(), new TypeToken<List<FacebookPerson>>(){}.getType());
    }
}
