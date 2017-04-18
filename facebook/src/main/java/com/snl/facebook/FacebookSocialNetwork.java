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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.Utility;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.reflect.TypeToken;
import com.snl.core.SocialNetwork;
import com.snl.core.SocialNetworkException;
import com.snl.core.SocialPerson;
import com.snl.core.listener.OnCheckIsFriendListener;
import com.snl.core.listener.OnLoginListener;
import com.snl.core.listener.OnRequestAddFriendListener;
import com.snl.core.listener.OnRequestRemoveFriendListener;
import com.snl.core.listener.OnShareListener;
import com.snl.core.listener.OnRequestDetailedSocialPersonListener;
import com.snl.core.listener.OnRequestFriendsListener;
import com.snl.core.listener.OnRequestSocialPersonListener;
import com.snl.core.listener.OnRequestSocialPersonsListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

/**
 * Created by Viнt@rь on 28.11.2015
 */
public class FacebookSocialNetwork extends SocialNetwork<AccessToken, ShareContent> {
    public static final int ID = 1;

    private final LoginManager mLoginManager;
    private final CallbackManager mCallbackManager;

    private final Collection<String> mPermissions;

    public FacebookSocialNetwork(@NonNull Application application, @Nullable Collection<String> permissions) {
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
    protected void updateContext(@NonNull Context context) {
        if (!(context instanceof FacebookActivity)) {
            super.updateContext(context);
        }
    }

    @Override
    public Activity getContext() {
        return (Activity) super.getContext();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    @Override
    public boolean isConnected() {
        return getAccessToken() != null && !getAccessToken().isExpired();
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
     * Not supported via Facebook SDK
     */
    @Override
    public void requestSocialPerson(String userId, OnRequestSocialPersonListener listener) {
        throw new SocialNetworkException("requestSocialPerson isn't allowed for FacebookSocialNetwork");
    }

    /**
     * Not supported via Facebook SDK
     */
    @Override
    public void requestSocialPersons(String[] userId, OnRequestSocialPersonsListener listener) {
        throw new SocialNetworkException("requestSocialPersons isn't allowed for FacebookSocialNetwork");
    }

    /**
     * Not supported via Facebook SDK
     */
    @Override
    public void requestDetailedSocialPerson(String userId, OnRequestDetailedSocialPersonListener listener) {
        throw new SocialNetworkException("requestDetailedSocialPerson isn't allowed for FacebookSocialNetwork");
    }

    /**
     * Not supported via Facebook SDK
     */
    @Override
    public void requestCheckIsFriend(String userId, OnCheckIsFriendListener listener) {
        throw new SocialNetworkException("requestCheckIsFriend isn't allowed for FacebookSocialNetwork");
    }

    /**
     * Request current user friends
     *
     * <p>
     *     The Friends API returns only friends that have installed your app.
     * </p>
     * <p>
     *     See Facebook Graph API
     *     <a href="https://developers.facebook.com/docs/graph-api/reference/user/friends">User friends</a>
     *     guide for more details.
     * </p>
     *
     * @param listener listener for getting list of current user friends
     */
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

    /**
     * Not supported via Facebook SDK
     */
    @Override
    public void requestAddFriend(String userID, OnRequestAddFriendListener listener) {
        throw new SocialNetworkException("requestAddFriend isn't allowed for FacebookSocialNetwork");
    }

    /**
     * Not supported via Facebook SDK
     */
    @Override
    public void requestRemoveFriend(String userID, OnRequestRemoveFriendListener listener) {
        throw new SocialNetworkException("requestRemoveFriend isn't allowed for FacebookSocialNetwork");
    }

    /**
     * Request to share custom content to Facebook <code>SocialNetwork</code>
     *
     * <p>
     *     The Share Content is requires installed native Facebook app,
     *     version 7.0 or higher.
     * </p>
     * <p>
     *     See
     *     <a href="https://developers.facebook.com/docs/sharing/android">Share Content</a>
     *     guide for more details.
     * </p>
     *
     * @param shareContent content that should be shared
     */
    @Override
    public void requestShareContent(ShareContent shareContent, OnShareListener listener) {
        super.requestShareContent(shareContent, listener);

        if (!isConnected()) {
            if (isRegistered(listener)) {
                listener.onError(getId(), Request.SHARE_CONTENT, "Please login first", null);
                cancelShareContentRequest();
            }
            return;
        }

        performShareContent(shareContent, listener);
    }

    private void performShareContent(final ShareContent shareContent, final OnShareListener listener) {
        ShareDialog shareDialog = new ShareDialog(getContext());
        shareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                if (isRegistered(listener)) {
                    listener.onShareSuccess(getId());
                    cancelShareContentRequest();
                }
            }

            @Override
            public void onCancel() {
                if (isRegistered(listener)) {
                    listener.onError(getId(), Request.SHARE_CONTENT, null, null);
                    cancelShareContentRequest();
                }
            }

            @Override
            public void onError(FacebookException error) {
                if (isRegistered(listener)) {
                    listener.onError(getId(), Request.SHARE_CONTENT, error.getMessage(), null);
                    cancelShareContentRequest();
                }
            }
        });
        shareDialog.show(shareContent);
    }

    private FacebookPerson parsePerson(JSONObject person) {
        return GSON.fromJson(person.toString(), FacebookPerson.class);
    }

    private List<SocialPerson> parsePersons(JSONArray persons) {
        return GSON.fromJson(persons.toString(), new TypeToken<List<FacebookPerson>>(){}.getType());
    }
}
