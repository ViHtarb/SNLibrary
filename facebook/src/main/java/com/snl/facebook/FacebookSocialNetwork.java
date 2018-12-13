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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
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
import com.snl.core.SocialUser;
import com.snl.core.listener.OnCheckIsFriendListener;
import com.snl.core.listener.OnLoginListener;
import com.snl.core.listener.OnRequestAddFriendListener;
import com.snl.core.listener.OnRequestDetailedSocialUserListener;
import com.snl.core.listener.OnRequestFriendsListener;
import com.snl.core.listener.OnRequestRemoveFriendListener;
import com.snl.core.listener.OnRequestSocialUserListener;
import com.snl.core.listener.OnRequestSocialUsersListener;
import com.snl.core.listener.OnShareListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Viнt@rь on 28.11.2015
 *
 * Facebook social network implementation
 */
public class FacebookSocialNetwork extends SocialNetwork<AccessToken, ShareContent> {
    public static final int ID = 1;

    private final Collection<String> mPermissions;

    private final LoginManager mLoginManager;
    private final CallbackManager mCallbackManager;

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
    public int getId() {
        return ID;
    }

    @Override
    public boolean isConnected() {
        return getAccessToken() != null && !getAccessToken().isExpired();
    }

    @Override
    public AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    @Override
    public void logout() {
        mLoginManager.logOut();
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
    public void requestCurrentUser(final OnRequestSocialUserListener listener) {
        super.requestCurrentUser(listener);

        if (!isConnected()) {
            if (isRegistered(listener)) {
                listener.onError(getId(), Request.USER, "Please login first", null);
                cancelCurrentUserRequest();
            }
            return;
        }

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, link, email");
        GraphRequest request = GraphRequest.newMeRequest(getAccessToken(), (object, response) -> {
            if (isRegistered(listener)) {
                if (response.getError() == null) {
                    listener.onRequestSocialUserSuccess(getId(), parseUser(object));
                } else {
                    listener.onError(getId(), Request.USER, response.getError().getErrorMessage(), null);
                }
                cancelCurrentUserRequest();
            }
        });
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void requestDetailedCurrentUser(final OnRequestDetailedSocialUserListener listener) {
        super.requestDetailedCurrentUser(listener);

        if (!isConnected()) {
            if (isRegistered(listener)) {
                listener.onError(getId(), Request.DETAIL_USER, "Please login first", null);
                cancelDetailedCurrentUserRequest();
            }
            return;
        }

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, first_name, middle_name, last_name, link, email, gender, birthday, verified");
        GraphRequest request = GraphRequest.newMeRequest(getAccessToken(), (object, response) -> {
            if (isRegistered(listener)) {
                if (response.getError() == null) {
                    listener.onRequestDetailedSocialUserSuccess(getId(), parseUser(object));
                } else {
                    listener.onError(getId(), Request.DETAIL_USER, response.getError().getErrorMessage(), null);
                }
                cancelDetailedCurrentUserRequest();
            }
        });
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Not supported via Facebook SDK
     */
    @Override
    public void requestSocialUser(String userId, OnRequestSocialUserListener listener) {
        throw new SocialNetworkException("requestSocialUser isn't allowed for FacebookSocialNetwork");
    }

    /**
     * Not supported via Facebook SDK
     */
    @Override
    public void requestSocialUsers(String[] userId, OnRequestSocialUsersListener listener) {
        throw new SocialNetworkException("requestSocialUsers isn't allowed for FacebookSocialNetwork");
    }

    /**
     * Not supported via Facebook SDK
     */
    @Override
    public void requestDetailedSocialUser(String userId, OnRequestDetailedSocialUserListener listener) {
        throw new SocialNetworkException("requestDetailedSocialUser isn't allowed for FacebookSocialNetwork");
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
        GraphRequest request = GraphRequest.newMyFriendsRequest(getAccessToken(), (objects, response) -> {
            if (isRegistered(listener)) {
                if (response.getError() == null) {
                    listener.onRequestFriendsSuccess(getId(), parseUsers(objects));
                } else {
                    listener.onError(getId(), Request.FRIENDS, response.getError().getErrorMessage(), null);
                }
                cancelFriendsRequest();
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

    private FacebookUser parseUser(JSONObject user) {
        return GSON.fromJson(user.toString(), FacebookUser.class);
    }

    private List<SocialUser> parseUsers(JSONArray users) {
        return GSON.fromJson(users.toString(), new TypeToken<List<FacebookUser>>(){}.getType());
    }
}
