package com.snl.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.CallSuper;

import com.google.gson.Gson;
import com.snl.core.listener.OnCheckIsFriendCompleteListener;
import com.snl.core.listener.OnLoginCompleteListener;
import com.snl.core.listener.OnPostingCompleteListener;
import com.snl.core.listener.OnRequestAccessTokenCompleteListener;
import com.snl.core.listener.OnRequestAddFriendCompleteListener;
import com.snl.core.listener.OnRequestDetailedSocialPersonCompleteListener;
import com.snl.core.listener.OnRequestGetFriendsCompleteListener;
import com.snl.core.listener.OnRequestRemoveFriendCompleteListener;
import com.snl.core.listener.OnRequestSocialPersonCompleteListener;
import com.snl.core.listener.OnRequestSocialPersonsCompleteListener;
import com.snl.core.listener.base.SocialNetworkListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Viнt@rь on 28.11.2015
 * {@link T} it`s a access token template
 */
public abstract class SocialNetwork<T> implements Application.ActivityLifecycleCallbacks {

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

    /*** Share bundle constant for message*/
    public static final String BUNDLE_MESSAGE = "message";
    /*** Share bundle constant for link*/
    public static final String BUNDLE_LINK = "link";
    /*** Share bundle constant for friendslist*/
    public static final String DIALOG_FRIENDS = "friends";
    /*** Share bundle constant for title*/
    public static final String BUNDLE_NAME = "name";
    /*** Share bundle constant for application name*/
    public static final String BUNDLE_APP_NAME = "app_name";
    /*** Share bundle constant for caption*/
    public static final String BUNDLE_CAPTION = "caption";
    /*** Share bundle constant for picture*/
    public static final String BUNDLE_PICTURE = "picture";

    protected static final Gson GSON = new Gson();

    protected Map<Request, SocialNetworkListener> mGlobalListeners = new HashMap<>();
    protected Map<Request, SocialNetworkListener> mLocalListeners = new HashMap<>();

    private Context mContext;

    public SocialNetwork(Application application) {
        mContext = application.getApplicationContext();
        application.registerActivityLifecycleCallbacks(this);
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    @CallSuper
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

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with, the resultCode it returned, and any additional data from it. Overrided in chosen social network
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    /**
     * Get id of social network
     * @return Social network ids:<br>
     * 1 - FB<br>
     * 2 - VK<br>
     */
    public abstract int getId();

    /**
     * Check if selected social network connected: true or false
     * @return true if connected, else false
     */
    public abstract boolean isConnected();

    /**
     * Return {@link T} (except GooglePlus)
     * @return {@link T}
     */
    public abstract T getAccessToken();

    /**
     * Logout from social network
     */
    public abstract void logout();

    //////// REQUESTS //////////////
    /**
     * Login to social network using global listener
     */
    public void requestLogin() {
        requestLogin(null);
    }

    /**
     * Login to social network using local listener
     * @param listener listener for login complete
     */
    @CallSuper
    public void requestLogin(OnLoginCompleteListener listener) {
        if (isConnected()) {
            throw new SocialNetworkException("Already connected, check isConnected() method first");
        }

        registerListener(Request.LOGIN, listener);
    }

    /**
     * Get {@link AccessToken} using global listener
     */
    public void requestAccessToken(){
        requestAccessToken(null);
    }

    /**
     * Get {@link AccessToken} using local listener
     * @param listener listener for request {@link AccessToken} complete
     */
    public void requestAccessToken(OnRequestAccessTokenCompleteListener listener) {
        registerListener(Request.ACCESS_TOKEN, listener);
    }

    /**
     * Get {@link SocialPerson} of current user using global listener
     */
    public void requestCurrentPerson() {
        requestCurrentPerson(null);
    }

    /**
     * Get {@link SocialPerson} of current user using local listener
     * @param listener listener for request {@link SocialPerson}
     */
    public void requestCurrentPerson(OnRequestSocialPersonCompleteListener listener) {
        registerListener(Request.PERSON, listener);
    }

    /**
     * Get detailed profile for current user using global listener. Look for detailed persons in social networks packages.
     */
    public void requestDetailedCurrentPerson(){
        requestDetailedSocialPerson(null);
    }

    /**
     * Get detailed profile for current user using global listener. Look for detailed persons in social networks packages.
     * @param listener listener for request detailed social person
     */
    public void requestDetailedCurrentPerson(OnRequestDetailedSocialPersonCompleteListener listener) {
        registerListener(Request.DETAIL_PERSON, listener);
    }

    /**
     * Get {@link SocialPerson} by user id using global listener
     * @param userId user id in social network
     */
    public void requestSocialPerson(String userId) {
        requestSocialPerson(userId, null);
    }

    /**
     * Get {@link SocialPerson} by user id using local listener
     * @param userId user id in social network
     * @param listener listener for request {@link SocialPerson}
     */
    public void requestSocialPerson(String userId, OnRequestSocialPersonCompleteListener listener) {
        registerListener(Request.SOCIAL_PERSON, listener);
    }

    /**
     * Get array list of {@link SocialPerson} by array of user ids using global listener
     * @param userId array of user ids in social network
     */
    public void requestSocialPersons(String[] userId) {
        requestSocialPersons(userId, null);
    }

    /**
     * Get array list of {@link SocialPerson} by array of user ids using local listener
     * @param userId array of user ids in social network
     * @param listener listener for request ArrayList of {@link SocialPerson}
     */
    public void requestSocialPersons(String[] userId, OnRequestSocialPersonsCompleteListener listener) {
        registerListener(Request.SOCIAL_PERSONS, listener);
    }

    /**
     * Get detailed profile for user by id using global listener. Look for detailed persons in social networks packages.
     * @param userId user id in social network
     */
    public void requestDetailedSocialPerson(String userId) {
        requestDetailedSocialPerson(userId, null);
    }

    /**
     * Get detailed profile for user by id using local listener. Look for detailed persons in social networks packages.
     * @param userId user id in social network
     * @param listener listener for request detailed social person
     */
    public void requestDetailedSocialPerson(String userId, OnRequestDetailedSocialPersonCompleteListener listener) {
        registerListener(Request.DETAIL_SOCIAL_PERSON, listener);
    }

    /**
     * Check if user by id is friend of current user using global listener
     * @param userId user id that should be checked as friend of current user
     */
    public void requestCheckIsFriend(String userId) {
        requestCheckIsFriend(userId, null);
    }

    /**
     * Check if user by id is friend of current user using local listener
     * @param userId user id that should be checked as friend of current user
     * @param listener listener for checking friend request
     */
    public void requestCheckIsFriend(String userId, OnCheckIsFriendCompleteListener listener) {
        registerListener(Request.CHECK_IS_FRIEND, listener);
    }
    /**
     * Get current user friends list using global listener
     */
    public void requestGetFriends() {
        requestGetFriends(null);
    }

    /**
     * Get current user friends list using local listener
     * @param listener listener for getting list of current user friends
     */
    public void requestGetFriends(OnRequestGetFriendsCompleteListener listener) {
        registerListener(Request.FRIENDS, listener);
    }

    /**
     * Invite friend by id to current user using global listener
     * @param userId id of user that should be invited
     */
    public void requestAddFriend(String userId) {
        requestAddFriend(userId, null);
    }

    /**
     * Invite friend by id to current user using local listener
     * @param userId id of user that should be invited
     * @param listener listener for invite result
     */
    public void requestAddFriend(String userId, OnRequestAddFriendCompleteListener listener) {
        registerListener(Request.ADD_FRIEND, listener);
    }

    /**
     * Remove friend by id from current user friends using global listener
     * @param userId user id that should be removed from friends
     */
    public void requestRemoveFriend(String userId) {
        requestRemoveFriend(userId, null);
    }

    /**
     * Remove friend by id from current user friends using local listener
     * @param userId user id that should be removed from friends
     * @param listener listener to remove friend request response
     */
    public void requestRemoveFriend(String userId, OnRequestRemoveFriendCompleteListener listener) {
        registerListener(Request.REMOVE_FRIEND, listener);
    }

    /**
     * Post message to social network using global listener
     * @param message message that should be shared
     */
    public void requestPostMessage(String message) {
        requestPostMessage(message, null);
    }

    /**
     * Post message to social network using local listener
     * @param message  message that should be shared
     * @param listener listener for posting request
     */
    public void requestPostMessage(String message, OnPostingCompleteListener listener) {
        registerListener(Request.POST_MESSAGE, listener);
    }

    /**
     * Post photo to social network using global listener
     * @param photo photo that should be shared
     * @param message message that should be shared with photo
     */
    public void requestPostPhoto(File photo, String message) {
        requestPostPhoto(photo, message, null);
    }

    /**
     * Post photo to social network using local listener
     * @param photo photo that should be shared
     * @param message message that should be shared with photo
     * @param listener listener for posting request
     */
    public void requestPostPhoto(File photo, String message, OnPostingCompleteListener listener) {
        registerListener(Request.POST_PHOTO, listener);
    }

    /**
     * Get Share dialog of social network using global listener
     * @param bundle bundle containing information that should be shared(Bundle constants in {@link SocialNetwork})
     */
    public void requestPostDialog(Bundle bundle) {
        requestPostDialog(bundle, null);
    }

    /**
     * Get Share dialog of social network using global listener
     * @param bundle bundle containing information that should be shared(Bundle constants in {@link SocialNetwork})
     * @param listener listener for posting request
     */
    public void requestPostDialog(Bundle bundle, OnPostingCompleteListener listener) {
        registerListener(Request.POST_DIALOG, listener);
    }

    /**
     * Post link with comment to social network using global listener
     * @param bundle bundle containing information that should be shared(Bundle constants in {@link SocialNetwork})
     * @param message message that should be shared with bundle
     */
    public void requestPostLink(Bundle bundle, String message) {
        requestPostLink(bundle, message, null);
    }

    /**
     * Post link with comment to social network using local listener
     * @param bundle bundle containing information that should be shared(Bundle constants in {@link SocialNetwork})
     * @param message message that should be shared with bundle
     * @param listener listener for posting request
     */
    public void requestPostLink(Bundle bundle, String message, OnPostingCompleteListener listener) {
        registerListener(Request.POST_LINK, listener);
    }

    /**
     * Cancel login request
     */
    public void cancelLoginRequest() {
        mLocalListeners.remove(Request.LOGIN);
    }

    /**
     * Cancel {@link AccessToken} request
     */
    public void cancelAccessTokenRequest() {
        mLocalListeners.remove(Request.ACCESS_TOKEN);
    }

    /**
     * Cancel current user {@link SocialPerson} request
     */
    public void cancelGetCurrentPersonRequest() {
        mLocalListeners.remove(Request.PERSON);
    }

    public void cancelGetDetailedCurrentPersonRequest() {
        mLocalListeners.remove(Request.DETAIL_PERSON);
    }

    /**
     * Cancel user by id {@link SocialPerson} request
     */
    public void cancelGetSocialPersonRequest() {
        mLocalListeners.remove(Request.SOCIAL_PERSON);
    }

    /**
     * Cancel users by array of ids request
     */
    public void cancelGetSocialPersonsRequest() {
        mLocalListeners.remove(Request.SOCIAL_PERSONS);
    }

    /**
     * Cancel detailed user request
     */
    public void cancelGetDetailedSocialPersonRequest() {
        mLocalListeners.remove(Request.DETAIL_SOCIAL_PERSON);
    }

    /**
     * Cancel check friend request
     */
    public void cancelCheckIsFriendRequest() {
        mLocalListeners.remove(Request.CHECK_IS_FRIEND);
    }

    /**
     * Cancel friends list request
     */
    public void cancelGetFriendsRequest() {
        mLocalListeners.remove(Request.FRIENDS);
    }

    /**
     * Cancel add friend request
     */
    public void cancelAddFriendRequest() {
        mLocalListeners.remove(Request.ADD_FRIEND);
    }

    /**
     * Cancel remove friend request
     */
    public void cancelRemoveFriendRequest() {
        mLocalListeners.remove(Request.REMOVE_FRIEND);
    }

    /**
     * Cancel post message request
     */
    public void cancelPostMessageRequest() {
        mLocalListeners.remove(Request.POST_MESSAGE);
    }

    /**
     * Cancel post photo request
     */
    public void cancelPostPhotoRequest() {
        mLocalListeners.remove(Request.POST_PHOTO);
    }

    /**
     * Cancel post link request
     */
    public void cancelPostLinkRequest() {
        mLocalListeners.remove(Request.POST_LINK);
    }

    /**
     * Cancel share dialog request
     */
    public void cancelPostDialogRequest() {
        mLocalListeners.remove(Request.POST_DIALOG);
    }

    /**
     * Cancel all requests
     */
    public void cancelAll() {
        mLocalListeners.clear();
    }

    //////////////////// UTIL METHODS ////////////////////
    protected void checkRequestState(AsyncTask request) throws SocialNetworkException {
        if (request != null) {
            throw new SocialNetworkException(request.toString() + "Request is already running");
        }
    }

    protected boolean isRegistered(Request request) {
        return mLocalListeners.get(request) != null;
    }

    protected boolean isRegistered(SocialNetworkListener listener) {
        return mLocalListeners.containsValue(listener);
    }

    protected SocialNetworkListener getListener(Request request) {
        return mLocalListeners.get(request);
    }

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
     * @param listener the callback that will run
     */
    public void setOnLoginCompleteListener(OnLoginCompleteListener listener) {
        mGlobalListeners.put(Request.LOGIN, listener);
    }

    /**
     * Register a callback to be invoked when {@link AccessToken} request complete.
     * @param listener the callback that will run
     */
    public void setOnRequestAccessTokenCompleteListener(OnRequestAccessTokenCompleteListener listener) {
        mGlobalListeners.put(Request.ACCESS_TOKEN, listener);
    }

    /**
     * Register a callback to be invoked when current {@link SocialPerson} request complete.
     * @param listener the callback that will run
     */
    public void setOnRequestCurrentPersonCompleteListener(OnRequestSocialPersonCompleteListener listener) {
        mGlobalListeners.put(Request.PERSON, listener);
    }

    /**
     * Register a callback to be invoked when detailed current person request complete. Look for detailed persons in social networks packages.
     * @param listener the callback that will run
     */
    public void setOnRequestDetailedCurrentPersonCompleteListener(OnRequestDetailedSocialPersonCompleteListener listener) {
        mGlobalListeners.put(Request.DETAIL_PERSON, listener);
    }

    /**
     * Register a callback to be invoked when {@link SocialPerson} by user id request complete.
     * @param listener the callback that will run
     */
    public void setOnRequestSocialPersonCompleteListener(OnRequestSocialPersonCompleteListener listener) {
        mGlobalListeners.put(Request.SOCIAL_PERSON, listener);
    }

    /**
     * Register a callback to be invoked when detailed social person request complete. Look for detailed persons in social networks packages.
     * @param listener the callback that will run
     */
    public void setOnRequestDetailedSocialPersonCompleteListener(OnRequestDetailedSocialPersonCompleteListener listener) {
        mGlobalListeners.put(Request.DETAIL_SOCIAL_PERSON, listener);
    }

    /**
     * Register a callback to be invoked when {@link SocialPerson}s by array of user ids request complete.
     * @param listener the callback that will run
     */
    public void setOnRequestSocialPersonsCompleteListener(OnRequestSocialPersonsCompleteListener listener) {
        mGlobalListeners.put(Request.SOCIAL_PERSONS, listener);
    }

    /**
     * Register a callback to be invoked when check friend request complete.
     * @param listener the callback that will run
     */
    public void setOnCheckIsFriendListener(OnCheckIsFriendCompleteListener listener) {
        mGlobalListeners.put(Request.CHECK_IS_FRIEND, listener);
    }

    /**
     * Register a callback to be invoked when post message request complete.
     * @param listener the callback that will run
     */
    public void setOnPostingMessageCompleteListener(OnPostingCompleteListener listener) {
        mGlobalListeners.put(Request.POST_MESSAGE, listener);
    }

    /**
     * Register a callback to be invoked when get friends list request complete.
     * @param listener the callback that will run
     */
    public void setOnRequestGetFriendsCompleteListener(OnRequestGetFriendsCompleteListener listener) {
        mGlobalListeners.put(Request.FRIENDS, listener);
    }

    /**
     * Register a callback to be invoked when invite friend request complete.
     * @param listener the callback that will run
     */
    public void setOnRequestAddFriendCompleteListener(OnRequestAddFriendCompleteListener listener) {
        mGlobalListeners.put(Request.ADD_FRIEND, listener);
    }

    /**
     * Register a callback to be invoked when remove friend request complete.
     * @param listener the callback that will run
     */
    public void setOnRequestRemoveFriendCompleteListener(OnRequestRemoveFriendCompleteListener listener) {
        mGlobalListeners.put(Request.REMOVE_FRIEND, listener);
    }

    /**
     * Register a callback to be invoked when post photo request complete.
     * @param listener the callback that will run
     */
    public void setOnPostingPhotoCompleteListener(OnPostingCompleteListener listener) {
        mGlobalListeners.put(Request.POST_PHOTO, listener);
    }

    /**
     * Register a callback to be invoked when post link request complete.
     * @param listener the callback that will run
     */
    public void setOnPostingLinkCompleteListener(OnPostingCompleteListener listener) {
        mGlobalListeners.put(Request.POST_LINK, listener);
    }

    /**
     * Register a callback to be invoked when share dialog request complete.
     * @param listener the callback that will run
     */
    public void setOnPostingDialogCompleteListener(OnPostingCompleteListener listener) {
        mGlobalListeners.put(Request.POST_DIALOG, listener);
    }
}
