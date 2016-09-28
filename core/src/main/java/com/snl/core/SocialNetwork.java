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

package com.snl.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.CallSuper;

import com.google.gson.Gson;
import com.snl.core.listener.OnCheckIsFriendListener;
import com.snl.core.listener.OnLoginListener;
import com.snl.core.listener.OnPostingListener;
import com.snl.core.listener.OnRequestAccessTokenListener;
import com.snl.core.listener.OnRequestAddFriendListener;
import com.snl.core.listener.OnRequestDetailedSocialPersonListener;
import com.snl.core.listener.OnRequestFriendsListener;
import com.snl.core.listener.OnRequestRemoveFriendListener;
import com.snl.core.listener.OnRequestSocialPersonListener;
import com.snl.core.listener.OnRequestSocialPersonsListener;
import com.snl.core.listener.base.SocialNetworkListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of <code>SocialNetwork</code>
 *
 * <p>
 *     The {@link T} it`s a <code>AccessToken</code>
 * </p>
 */
public abstract class SocialNetwork<T> {

    public enum Request {
        LOGIN,
        LOGIN2,
        ACCESS_TOKEN,
        PERSON,
        DETAIL_PERSON,
        SOCIAL_PERSON,
        DETAIL_SOCIAL_PERSON,
        SOCIAL_PERSONS,
        POST_MESSAGE,
        POST_PHOTO,
        POST_LINK,
        POST_DIALOG,
        CHECK_IS_FRIEND,
        FRIENDS,
        ADD_FRIEND,
        REMOVE_FRIEND
    }

    // Share bundle constant for message
    public static final String BUNDLE_MESSAGE = "message";

    // Share bundle constant for link
    public static final String BUNDLE_LINK = "link";

    // Share bundle constant for friendslist
    public static final String DIALOG_FRIENDS = "friends";

    // Share bundle constant for title
    public static final String BUNDLE_NAME = "name";

    // Share bundle constant for application name
    public static final String BUNDLE_APP_NAME = "app_name";

    // Share bundle constant for caption
    public static final String BUNDLE_CAPTION = "caption";

    // Share bundle constant for picture
    public static final String BUNDLE_PICTURE = "picture";

    protected static final Gson GSON = new Gson();

    protected final Map<Request, SocialNetworkListener> mGlobalListeners = new HashMap<>();
    protected final Map<Request, SocialNetworkListener> mLocalListeners = new HashMap<>();

    protected final ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks();

    private Context mContext;

    public SocialNetwork(Application application) {
        mContext = application.getApplicationContext();
        application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with, the resultCode it returned, and any additional data from it. Overrides in chosen social network
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    /**
     * Returns {@link SocialNetwork} id
     *
     * @return id
     */
    public abstract int getId();

    /**
     * Check is {@link SocialNetwork} connected
     *
     * @return Is connected
     */
    public abstract boolean isConnected();

    /**
     * Returns current <code>AccessToken</code>
     *
     * @return access token
     */
    public abstract T getAccessToken();

    /**
     * Logout from <code>SocialNetwork</code>
     */
    public abstract void logout();

    //////// REQUESTS //////////////
    /**
     * Login to <code>SocialNetwork</code> using global listener
     */
    public void requestLogin() {
        requestLogin(null);
    }

    /**
     * Login to <code>SocialNetwork</code>
     */
    @CallSuper
    public void requestLogin(OnLoginListener listener) {
        if (isConnected()) {
            throw new SocialNetworkException("Already connected, check isConnected() method first");
        }

        registerListener(Request.LOGIN, listener);
    }

    /**
     * Request <code>AccessToken</code>
     */
    public void requestAccessToken() {
        requestAccessToken(null);
    }

    /**
     * Request <code>AccessToken</code>
     */
    public void requestAccessToken(OnRequestAccessTokenListener listener) {
        registerListener(Request.ACCESS_TOKEN, listener);
    }

    /**
     * Request current {@link SocialPerson}
     */
    public void requestCurrentPerson() {
        requestCurrentPerson(null);
    }

    /**
     * Request current {@link SocialPerson}
     */
    public void requestCurrentPerson(OnRequestSocialPersonListener listener) {
        registerListener(Request.PERSON, listener);
    }

    /**
     * Request detailed current {@link SocialPerson}
     * Look for detailed persons in <code>SocialNetwork`s</code> packages
     */
    public void requestDetailedCurrentPerson() {
        requestDetailedSocialPerson(null);
    }

    /**
     * Request detailed current {@link SocialPerson}
     * Look for detailed persons in <code>SocialNetwork`s</code> packages
     */
    public void requestDetailedCurrentPerson(OnRequestDetailedSocialPersonListener listener) {
        registerListener(Request.DETAIL_PERSON, listener);
    }

    /**
     * Request {@link SocialPerson} by user id
     *
     * @param userId user id in <code>SocialNetwork</code>
     */
    public void requestSocialPerson(String userId) {
        requestSocialPerson(userId, null);
    }

    /**
     * Request {@link SocialPerson} by user id
     *
     * @param userId user id in <code>SocialNetwork</code>
     */
    public void requestSocialPerson(String userId, OnRequestSocialPersonListener listener) {
        registerListener(Request.SOCIAL_PERSON, listener);
    }

    /**
     * Request {@link SocialPerson}`s list by user ids array
     *
     * @param ids array of user ids in <code>SocialNetwork</code>
     */
    public void requestSocialPersons(String[] ids) {
        requestSocialPersons(ids, null);
    }

    /**
     * Request {@link SocialPerson}`s list by user ids array
     *
     * @param ids array of ids in <code>SocialNetwork</code>
     */
    public void requestSocialPersons(String[] ids, OnRequestSocialPersonsListener listener) {
        registerListener(Request.SOCIAL_PERSONS, listener);
    }

    /**
     * Request detailed {@link SocialPerson} by user id
     * Look for detailed persons in <code>SocialNetwork</code> packages
     *
     * @param userId id in <code>SocialNetwork</code>
     */
    public void requestDetailedSocialPerson(String userId) {
        requestDetailedSocialPerson(userId, null);
    }

    /**
     * Request detailed {@link SocialPerson} by user id
     * Look for detailed persons in <code>SocialNetwork</code> packages.
     *
     * @param userId id in <code>SocialNetwork</code>
     */
    public void requestDetailedSocialPerson(String userId, OnRequestDetailedSocialPersonListener listener) {
        registerListener(Request.DETAIL_SOCIAL_PERSON, listener);
    }

    /**
     * Check user by id is friend of current user
     *
     * @param userId id that should be checked as friend of current user
     */
    public void requestCheckIsFriend(String userId) {
        requestCheckIsFriend(userId, null);
    }

    /**
     * Check user by id is friend of current user
     *
     * @param userId id that should be checked as friend of current user
     */
    public void requestCheckIsFriend(String userId, OnCheckIsFriendListener listener) {
        registerListener(Request.CHECK_IS_FRIEND, listener);
    }

    /**
     * Request current user friends
     */
    public void requestFriends() {
        requestFriends(null);
    }

    /**
     * Request current user friends
     */
    public void requestFriends(OnRequestFriendsListener listener) {
        registerListener(Request.FRIENDS, listener);
    }

    /**
     * Request for user(by id) invitations to friends
     *
     * @param userId id of user that should be invited
     */
    public void requestAddFriend(String userId) {
        requestAddFriend(userId, null);
    }

    /**
     * Request for user(by id) invitations to friends
     *
     * @param userId id of user that should be invited
     */
    public void requestAddFriend(String userId, OnRequestAddFriendListener listener) {
        registerListener(Request.ADD_FRIEND, listener);
    }

    /**
     * Request to remove the user(by id) from friends
     *
     * @param userId id that should be removed from friends
     */
    public void requestRemoveFriend(String userId) {
        requestRemoveFriend(userId, null);
    }

    /**
     * Request to remove the user(by id) from friends
     *
     * @param userId id that should be removed from friends
     */
    public void requestRemoveFriend(String userId, OnRequestRemoveFriendListener listener) {
        registerListener(Request.REMOVE_FRIEND, listener);
    }

    /**
     * Post message to <code>SocialNetwork</code>
     *
     * @param message message that should be shared
     */
    public void requestPostMessage(String message) {
        requestPostMessage(message, null);
    }

    /**
     * Post message to <code>SocialNetwork</code>
     *
     * @param message message that should be shared
     */
    public void requestPostMessage(String message, OnPostingListener listener) {
        registerListener(Request.POST_MESSAGE, listener);
    }

    /**
     * Post photo to <code>SocialNetwork</code>
     *
     * @param photo photo that should be shared
     * @param message message that should be shared with photo
     */
    public void requestPostPhoto(File photo, String message) {
        requestPostPhoto(photo, message, null);
    }

    /**
     * Post photo to <code>SocialNetwork</code>
     *
     * @param photo photo that should be shared
     * @param message message that should be shared with photo
     */
    public void requestPostPhoto(File photo, String message, OnPostingListener listener) {
        registerListener(Request.POST_PHOTO, listener);
    }

    /**
     * Request Share dialog of <code>SocialNetwork</code>
     *
     * @param bundle bundle containing information that should be shared(Bundle constants in {@link SocialNetwork})
     */
    public void requestPostDialog(Bundle bundle) {
        requestPostDialog(bundle, null);
    }

    /**
     * Request Share dialog of <code>SocialNetwork</code>
     *
     * @param bundle bundle containing information that should be shared(Bundle constants in {@link SocialNetwork})
     */
    public void requestPostDialog(Bundle bundle, OnPostingListener listener) {
        registerListener(Request.POST_DIALOG, listener);
    }

    /**
     * Post link with comment to <code>SocialNetwork</code>
     *
     * @param bundle bundle containing information that should be shared(Bundle constants in {@link SocialNetwork})
     * @param message message that should be shared with bundle
     */
    public void requestPostLink(Bundle bundle, String message) {
        requestPostLink(bundle, message, null);
    }

    /**
     * Post link with comment to <code>SocialNetwork</code>
     *
     * @param bundle bundle containing information that should be shared(Bundle constants in {@link SocialNetwork})
     * @param message message that should be shared with bundle
     */
    public void requestPostLink(Bundle bundle, String message, OnPostingListener listener) {
        registerListener(Request.POST_LINK, listener);
    }

    /**
     * Cancel {@link Request#LOGIN} request
     */
    public void cancelLoginRequest() {
        mLocalListeners.remove(Request.LOGIN);
    }

    /**
     * Cancel {@link Request#ACCESS_TOKEN} request
     */
    public void cancelAccessTokenRequest() {
        mLocalListeners.remove(Request.ACCESS_TOKEN);
    }

    /**
     * Cancel {@link Request#PERSON} request
     */
    public void cancelCurrentPersonRequest() {
        mLocalListeners.remove(Request.PERSON);
    }

    /**
     * Cancel {@link Request#DETAIL_PERSON} request
     */
    public void cancelDetailedCurrentPersonRequest() {
        mLocalListeners.remove(Request.DETAIL_PERSON);
    }

    /**
     * Cancel {@link Request#SOCIAL_PERSON} request
     */
    public void cancelSocialPersonRequest() {
        mLocalListeners.remove(Request.SOCIAL_PERSON);
    }

    /**
     * Cancel {@link Request#SOCIAL_PERSONS} request
     */
    public void cancelSocialPersonsRequest() {
        mLocalListeners.remove(Request.SOCIAL_PERSONS);
    }

    /**
     * Cancel {@link Request#DETAIL_SOCIAL_PERSON} request
     */
    public void cancelDetailedSocialPersonRequest() {
        mLocalListeners.remove(Request.DETAIL_SOCIAL_PERSON);
    }

    /**
     * Cancel {@link Request#CHECK_IS_FRIEND} request
     */
    public void cancelCheckIsFriendRequest() {
        mLocalListeners.remove(Request.CHECK_IS_FRIEND);
    }

    /**
     * Cancel {@link Request#FRIENDS} request
     */
    public void cancelFriendsRequest() {
        mLocalListeners.remove(Request.FRIENDS);
    }

    /**
     * Cancel {@link Request#ADD_FRIEND} request
     */
    public void cancelAddFriendRequest() {
        mLocalListeners.remove(Request.ADD_FRIEND);
    }

    /**
     * Cancel {@link Request#REMOVE_FRIEND} request
     */
    public void cancelRemoveFriendRequest() {
        mLocalListeners.remove(Request.REMOVE_FRIEND);
    }

    /**
     * Cancel {@link Request#POST_MESSAGE} request
     */
    public void cancelPostMessageRequest() {
        mLocalListeners.remove(Request.POST_MESSAGE);
    }

    /**
     * Cancel {@link Request#POST_PHOTO} request
     */
    public void cancelPostPhotoRequest() {
        mLocalListeners.remove(Request.POST_PHOTO);
    }

    /**
     * Cancel {@link Request#POST_LINK} request
     */
    public void cancelPostLinkRequest() {
        mLocalListeners.remove(Request.POST_LINK);
    }

    /**
     * Cancel {@link Request#POST_DIALOG} request
     */
    public void cancelPostDialogRequest() {
        mLocalListeners.remove(Request.POST_DIALOG);
    }

    /**
     * Cancel all {@link Request}`s
     */
    public void cancelAll() {
        mLocalListeners.clear();
    }

    //////////////////// UTIL METHODS ////////////////////
    /**
     * Check is {@link Request} registered
     * @return Is {@link Request} registered
     */
    protected boolean isRegistered(Request request) {
        return mLocalListeners.get(request) != null;
    }

    /**
     * Check is {@link SocialNetworkListener} registered
     * @return Is {@link SocialNetworkListener} registered
     */
    protected boolean isRegistered(SocialNetworkListener listener) {
        return mLocalListeners.containsValue(listener);
    }

    /**
     * Get {@link SocialNetworkListener} by {@link Request}
     * @return {@link SocialNetworkListener}
     */
    protected SocialNetworkListener getListener(Request request) {
        return mLocalListeners.get(request);
    }

    /**
     * Register {@link SocialNetworkListener} by {@link Request} as key
     */
    private void registerListener(Request request, SocialNetworkListener listener) {
        if (listener != null) {
            mLocalListeners.put(request, listener);
        } else {
            mLocalListeners.put(request, mGlobalListeners.get(request));
        }
    }

    //////////////////// SETTERS FOR GLOBAL LISTENERS ////////////////////
    /**
     * Register a callback to be invoked when login complete.
     *
     * @param listener the callback that will run
     */
    public void setOnLoginListener(OnLoginListener listener) {
        mGlobalListeners.put(Request.LOGIN, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestAccessToken()} complete.
     *
     * @param listener the callback that will run
     */
    public void setOnRequestAccessTokenListener(OnRequestAccessTokenListener listener) {
        mGlobalListeners.put(Request.ACCESS_TOKEN, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestCurrentPerson()} complete.
     *
     * @param listener the callback that will run
     */
    public void setOnRequestCurrentPersonListener(OnRequestSocialPersonListener listener) {
        mGlobalListeners.put(Request.PERSON, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestDetailedCurrentPerson()} complete.
     * Look for detailed persons in <code>SocialNetwork`s</code> packages.
     *
     * @param listener the callback that will run
     */
    public void setOnRequestDetailedCurrentPersonListener(OnRequestDetailedSocialPersonListener listener) {
        mGlobalListeners.put(Request.DETAIL_PERSON, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestSocialPerson(String)} complete.
     *
     * @param listener the callback that will run
     */
    public void setOnRequestSocialPersonListener(OnRequestSocialPersonListener listener) {
        mGlobalListeners.put(Request.SOCIAL_PERSON, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestDetailedSocialPerson(String)} complete.
     * Look for detailed persons in <code>SocialNetwork`s</code>  packages.
     *
     * @param listener the callback that will run
     */
    public void setOnRequestDetailedSocialPersonListener(OnRequestDetailedSocialPersonListener listener) {
        mGlobalListeners.put(Request.DETAIL_SOCIAL_PERSON, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestSocialPersons(String[])} complete.
     *
     * @param listener the callback that will run
     */
    public void setOnRequestSocialPersonsListener(OnRequestSocialPersonsListener listener) {
        mGlobalListeners.put(Request.SOCIAL_PERSONS, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestCheckIsFriend(String)} complete.
     *
     * @param listener the callback that will run
     */
    public void setOnCheckIsFriendListener(OnCheckIsFriendListener listener) {
        mGlobalListeners.put(Request.CHECK_IS_FRIEND, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestFriends()} complete.
     *
     * @param listener the callback that will run
     */
    public void setOnRequestGetFriendsListener(OnRequestFriendsListener listener) {
        mGlobalListeners.put(Request.FRIENDS, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestAddFriend(String)} complete.
     *
     * @param listener the callback that will run
     */
    public void setOnRequestAddFriendListener(OnRequestAddFriendListener listener) {
        mGlobalListeners.put(Request.ADD_FRIEND, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestRemoveFriend(String)} complete.
     *
     * @param listener the callback that will run
     */
    public void setOnRequestRemoveFriendListener(OnRequestRemoveFriendListener listener) {
        mGlobalListeners.put(Request.REMOVE_FRIEND, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestPostMessage(String)} complete.
     *
     * @param listener the callback that will run
     */
    public void setOnPostingMessageListener(OnPostingListener listener) {
        mGlobalListeners.put(Request.POST_MESSAGE, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestPostPhoto(File, String)} complete.
     *
     * @param listener the callback that will run
     */
    public void setOnPostingPhotoListener(OnPostingListener listener) {
        mGlobalListeners.put(Request.POST_PHOTO, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestPostLink(Bundle, String)} complete.
     *
     * @param listener the callback that will run
     */
    public void setOnPostingLinkListener(OnPostingListener listener) {
        mGlobalListeners.put(Request.POST_LINK, listener);
    }

    /**
     * Register a callback to be invoked when {@link #requestPostDialog(Bundle)} complete.
     *
     * @param listener the callback that will run
     */
    public void setOnPostingDialogListener(OnPostingListener listener) {
        mGlobalListeners.put(Request.POST_DIALOG, listener);
    }

    private final class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            mContext = activity;
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
