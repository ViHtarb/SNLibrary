package com.snl.core;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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
public abstract class SocialNetwork<T> {

    /*** Used to check is login request in progress*/
    public static final String REQUEST_LOGIN = "SocialNetwork.REQUEST_LOGIN";
    /*** Used to check is login request in progress for social networks with OAuth*/
    public static final String REQUEST_LOGIN2 = "SocialNetwork.REQUEST_LOGIN2";
    /*** Used to check is access token request in progress*/
    public static final String REQUEST_ACCESS_TOKEN = "SocialNetwork.REQUEST_ACCESS_TOKEN";
    /*** Used to check is get detailed person request in progress*/
    public static final String REQUEST_GET_DETAIL_PERSON = "SocialNetwork.REQUEST_GET_DETAIL_PERSON";
    /*** Used to check is get person request in progress*/
    public static final String REQUEST_GET_PERSON = "SocialNetwork.REQUEST_GET_PERSON";
    /*** Used to check is get persons request in progress*/
    public static final String REQUEST_GET_PERSONS = "SocialNetwork.REQUEST_GET_PERSONS";
    /*** Used to check is get current person request in progress*/
    public static final String REQUEST_GET_CURRENT_PERSON = "SocialNetwork.REQUEST_GET_CURRENT_PERSON";
    /*** Used to check is post message request in progress*/
    public static final String REQUEST_POST_MESSAGE = "SocialNetwork.REQUEST_POST_MESSAGE";
    /*** Used to check is post photo request in progress*/
    public static final String REQUEST_POST_PHOTO = "SocialNetwork.REQUEST_POST_PHOTO";
    /*** Used to check is post link request in progress*/
    public static final String REQUEST_POST_LINK = "SocialNetwork.REQUEST_POST_LINK";
    /*** Used to check is post dialog request in progress*/
    public static final String REQUEST_POST_DIALOG = "SocialNetwork.REQUEST_POST_DIALOG";
    /*** Used to check is checking friend request in progress*/
    public static final String REQUEST_CHECK_IS_FRIEND = "SocialNetwork.REQUEST_CHECK_IS_FRIEND";
    /*** Used to check is get friends list request in progress*/
    public static final String REQUEST_GET_FRIENDS = "SocialNetwork.REQUEST_GET_FRIENDS";
    /*** Used to check is add friend request in progress*/
    public static final String REQUEST_ADD_FRIEND = "SocialNetwork.REQUEST_ADD_FRIEND";
    /*** Used to check is remove friend request in progress*/
    public static final String REQUEST_REMOVE_FRIEND = "SocialNetwork.REQUEST_REMOVE_FRIEND";

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

    /*** Shared preferences name */
    //private static final String SHARED_PREFERENCES_NAME = "social_networks";
    //protected SharedPreferences mSharedPreferences;

    protected Map<String, SocialNetworkListener> mGlobalListeners = new HashMap<>();
    protected Map<String, SocialNetworkListener> mLocalListeners = new HashMap<>();

    protected Map<String, String> test = new HashMap<>();

    private Context mContext;

    public SocialNetwork(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
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
     * 1 - Twitter<br>
     * 2 - LinkedIn<br>
     * 3 - Google Plus<br>
     * 4 - Facebook<br>
     * 5 - Vkontakte<br>
     * 6 - Odnoklassniki<br>
     * 7 - Instagram<br>
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
     * @param onLoginCompleteListener listener for login complete
     */
    public void requestLogin(OnLoginCompleteListener onLoginCompleteListener) {
        if (isConnected()) {
            throw new SocialNetworkException("Already connected, please check isConnected() method");
        }

        registerListener(REQUEST_LOGIN, onLoginCompleteListener);
    }

    /**
     * Get {@link AccessToken} using global listener
     */
    public void requestAccessToken(){
        requestAccessToken(null);
    }

    /**
     * Get {@link AccessToken} using local listener
     * @param onRequestAccessTokenCompleteListener listener for request {@link AccessToken} complete
     */
    public void requestAccessToken(OnRequestAccessTokenCompleteListener onRequestAccessTokenCompleteListener) {
        registerListener(REQUEST_ACCESS_TOKEN, onRequestAccessTokenCompleteListener);
    }

    /**
     * Get {@link SocialPerson} of current user using global listener
     */
    public void requestCurrentPerson() {
        requestCurrentPerson(null);
    }

    /**
     * Get {@link SocialPerson} of current user using local listener
     * @param onRequestSocialPersonCompleteListener listener for request {@link SocialPerson}
     */
    public void requestCurrentPerson(OnRequestSocialPersonCompleteListener onRequestSocialPersonCompleteListener) {
        registerListener(REQUEST_GET_CURRENT_PERSON, onRequestSocialPersonCompleteListener);
    }

    /**
     * Get {@link SocialPerson} by user id using global listener
     * @param userID user id in social network
     */
    public void requestSocialPerson(String userID) {
        requestSocialPerson(userID, null);
    }

    /**
     * Get {@link SocialPerson} by user id using local listener
     * @param userID user id in social network
     * @param onRequestSocialPersonCompleteListener listener for request {@link SocialPerson}
     */
    public void requestSocialPerson(String userID, OnRequestSocialPersonCompleteListener onRequestSocialPersonCompleteListener) {
        registerListener(REQUEST_GET_PERSON, onRequestSocialPersonCompleteListener);
    }

    /**
     * Get arraylist of {@link SocialPerson} by array of user ids using global listener
     * @param userID array of user ids in social network
     */
    public void requestSocialPersons(String[] userID) {
        requestSocialPersons(userID, null);
    }

    /**
     * Get arraylist of {@link SocialPerson} by array of user ids using local listener
     * @param userID array of user ids in social network
     * @param onRequestSocialPersonsCompleteListener listener for request ArrayList of {@link SocialPerson}
     */
    public void requestSocialPersons(String[] userID, OnRequestSocialPersonsCompleteListener onRequestSocialPersonsCompleteListener) {
        registerListener(REQUEST_GET_PERSONS, onRequestSocialPersonsCompleteListener);
    }

    /**
     * Get detailed profile for user by id using global listener. Look for detailed persons in social networks packages.
     * @param userID user id in social network
     */
    public void requestDetailedSocialPerson(String userID) {
        requestDetailedSocialPerson(userID, null);
    }

    /**
     * Get detailed profile for user by id using local listener. Look for detailed persons in social networks packages.
     * @param userID user id in social network
     * @param onRequestDetailedSocialPersonCompleteListener listener for request detailed social person
     */
    public void requestDetailedSocialPerson(String userID, OnRequestDetailedSocialPersonCompleteListener onRequestDetailedSocialPersonCompleteListener) {
        registerListener(REQUEST_GET_DETAIL_PERSON, onRequestDetailedSocialPersonCompleteListener);
    }

    /**
     * Get detailed profile for current user using global listener. Look for detailed persons in social networks packages.
     */
    public void requestDetailedCurrentPerson(){
        requestDetailedSocialPerson(null);
    }

    /**
     * Get detailed profile for current user using global listener. Look for detailed persons in social networks packages.
     * @param onRequestDetailedSocialPersonCompleteListener listener for request detailed social person
     */
    public void requestDetailedCurrentPerson(OnRequestDetailedSocialPersonCompleteListener onRequestDetailedSocialPersonCompleteListener) {
        requestDetailedSocialPerson(null, onRequestDetailedSocialPersonCompleteListener);
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
     * @param onPostingCompleteListener listener for posting request
     */
    public void requestPostMessage(String message, OnPostingCompleteListener onPostingCompleteListener) {
        registerListener(REQUEST_POST_MESSAGE, onPostingCompleteListener);
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
     * @param onPostingCompleteListener listener for posting request
     */
    public void requestPostPhoto(File photo, String message, OnPostingCompleteListener onPostingCompleteListener) {
        registerListener(REQUEST_POST_PHOTO, onPostingCompleteListener);
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
     * @param onPostingCompleteListener listener for posting request
     */
    public void requestPostDialog(Bundle bundle, OnPostingCompleteListener onPostingCompleteListener) {
        registerListener(REQUEST_POST_DIALOG, onPostingCompleteListener);
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
     * @param onPostingCompleteListener listener for posting request
     */
    public void requestPostLink(Bundle bundle, String message, OnPostingCompleteListener onPostingCompleteListener) {
        registerListener(REQUEST_POST_LINK, onPostingCompleteListener);
    }

    /**
     * Check if user by id is friend of current user using global listener
     * @param userID user id that should be checked as friend of current user
     */
    public void requestCheckIsFriend(String userID) {
        requestCheckIsFriend(userID, null);
    }

    /**
     * Check if user by id is friend of current user using local listener
     * @param userID user id that should be checked as friend of current user
     * @param onCheckIsFriendCompleteListener listener for checking friend request
     */
    public void requestCheckIsFriend(String userID, OnCheckIsFriendCompleteListener onCheckIsFriendCompleteListener) {
        registerListener(REQUEST_CHECK_IS_FRIEND, onCheckIsFriendCompleteListener);
    }
    /**
     * Get current user friends list using global listener
     */
    public void requestGetFriends() {
        requestGetFriends(null);
    }

    /**
     * Get current user friends list using local listener
     * @param onRequestGetFriendsCompleteListener listener for getting list of current user friends
     */
    public void requestGetFriends(OnRequestGetFriendsCompleteListener onRequestGetFriendsCompleteListener) {
        registerListener(REQUEST_GET_FRIENDS, onRequestGetFriendsCompleteListener);
    }

    /**
     * Invite friend by id to current user using global listener
     * @param userID id of user that should be invited
     */
    public void requestAddFriend(String userID) {
        requestAddFriend(userID, null);
    }

    /**
     * Invite friend by id to current user using local listener
     * @param userID id of user that should be invited
     * @param onRequestAddFriendCompleteListener listener for invite result
     */
    public void requestAddFriend(String userID, OnRequestAddFriendCompleteListener onRequestAddFriendCompleteListener) {
        registerListener(REQUEST_ADD_FRIEND, onRequestAddFriendCompleteListener);
    }

    /**
     * Remove friend by id from current user friends using global listener
     * @param userID user id that should be removed from friends
     */
    public void requestRemoveFriend(String userID) {
        requestRemoveFriend(userID, null);
    }

    /**
     * Remove friend by id from current user friends using local listener
     * @param userID user id that should be removed from friends
     * @param onRequestRemoveFriendCompleteListener listener to remove friend request response
     */
    public void requestRemoveFriend(String userID, OnRequestRemoveFriendCompleteListener onRequestRemoveFriendCompleteListener) {
        registerListener(REQUEST_REMOVE_FRIEND, onRequestRemoveFriendCompleteListener);
    }

    /**
     * Cancel login request
     */
    public void cancelLoginRequest() {
        mLocalListeners.remove(REQUEST_LOGIN);
    }

    /**
     * Cancel {@link AccessToken} request
     */
    public void cancelAccessTokenRequest() {
        mLocalListeners.remove(REQUEST_ACCESS_TOKEN);
    }

    /**
     * Cancel current user {@link SocialPerson} request
     */
    public void cancelGetCurrentPersonRequest() {
        mLocalListeners.remove(REQUEST_GET_CURRENT_PERSON);
    }

    /**
     * Cancel user by id {@link SocialPerson} request
     */
    public void cancelGetSocialPersonRequest() {
        mLocalListeners.remove(REQUEST_GET_PERSON);
    }

    /**
     * Cancel users by array of ids request
     */
    public void cancelGetSocialPersonsRequest() {
        mLocalListeners.remove(REQUEST_GET_PERSONS);
    }

    /**
     * Cancel detailed user request
     */
    public void cancelGetDetailedSocialRequest() {
        mLocalListeners.remove(REQUEST_GET_DETAIL_PERSON);
    }

    /**
     * Cancel post message request
     */
    public void cancelPostMessageRequest() {
        mLocalListeners.remove(REQUEST_POST_MESSAGE);
    }

    /**
     * Cancel post photo request
     */
    public void cancelPostPhotoRequest() {
        mLocalListeners.remove(REQUEST_POST_PHOTO);
    }

    /**
     * Cancel post link request
     */
    public void cancelPostLinkRequest() {
        mLocalListeners.remove(REQUEST_POST_LINK);
    }

    /**
     * Cancel share dialog request
     */
    public void cancelPostDialogRequest() {
        mLocalListeners.remove(REQUEST_POST_DIALOG);
    }

    /**
     * Cancel check friend request
     */
    public void cancelCheckIsFriendRequest() {
        mLocalListeners.remove(REQUEST_CHECK_IS_FRIEND);
    }

    /**
     * Cancel friends list request
     */
    public void cancelGetFriendsRequest() {
        mLocalListeners.remove(REQUEST_GET_FRIENDS);
    }

    /**
     * Cancel add friend request
     */
    public void cancelAddFriendRequest() {
        mLocalListeners.remove(REQUEST_ADD_FRIEND);
    }

    /**
     * Cancel remove friend request
     */
    public void cancelRemoveFriendRequest() {
        mLocalListeners.remove(REQUEST_REMOVE_FRIEND);
    }

    /**
     * Cancel all requests
     */
    public void cancelAll() {
        Log.d("SocialNetwork", this + ":SocialNetwork.cancelAll()");

        // we need to call all, because in implementations we can possible do aditional work in specific methods
        cancelLoginRequest();
        cancelAccessTokenRequest();
        cancelGetCurrentPersonRequest();
        cancelGetSocialPersonRequest();
        cancelGetSocialPersonsRequest();
        cancelGetDetailedSocialRequest();
        cancelPostMessageRequest();
        cancelPostPhotoRequest();
        cancelPostLinkRequest();
        cancelPostDialogRequest();
        cancelGetFriendsRequest();
        cancelCheckIsFriendRequest();
        cancelAddFriendRequest();
        cancelRemoveFriendRequest();

        // remove all local listeners
        mLocalListeners = new HashMap<>();
    }

    //////////////////// UTIL METHODS ////////////////////

    protected void checkRequestState(AsyncTask request) throws SocialNetworkException {
        if (request != null) {
            throw new SocialNetworkException(request.toString() + "Request is already running");
        }
    }

    private void registerListener(String listenerID, SocialNetworkListener socialNetworkListener) {
        if (socialNetworkListener != null) {
            mLocalListeners.put(listenerID, socialNetworkListener);
        } else {
            mLocalListeners.put(listenerID, mGlobalListeners.get(listenerID));
        }
    }

    //////////////////// SETTERS FOR GLOBAL LISTENERS ////////////////////

    /**
     * Register a callback to be invoked when login complete.
     * @param onLoginCompleteListener the callback that will run
     */
    public void setOnLoginCompleteListener(OnLoginCompleteListener onLoginCompleteListener) {
        mGlobalListeners.put(REQUEST_LOGIN, onLoginCompleteListener);
    }

    /**
     * Register a callback to be invoked when {@link AccessToken} request complete.
     * @param onRequestAccessTokenCompleteListener the callback that will run
     */
    public void setOnRequestAccessTokenCompleteListener(OnRequestAccessTokenCompleteListener onRequestAccessTokenCompleteListener) {
        mGlobalListeners.put(REQUEST_ACCESS_TOKEN, onRequestAccessTokenCompleteListener);
    }

    /**
     * Register a callback to be invoked when current {@link SocialPerson} request complete.
     * @param onRequestCurrentPersonCompleteListener the callback that will run
     */
    public void setOnRequestCurrentPersonCompleteListener(OnRequestSocialPersonCompleteListener onRequestCurrentPersonCompleteListener) {
        mGlobalListeners.put(REQUEST_GET_CURRENT_PERSON, onRequestCurrentPersonCompleteListener);
    }

    /**
     * Register a callback to be invoked when {@link SocialPerson} by user id request complete.
     * @param onRequestSocialPersonCompleteListener the callback that will run
     */
    public void setOnRequestSocialPersonCompleteListener(OnRequestSocialPersonCompleteListener onRequestSocialPersonCompleteListener) {
        mGlobalListeners.put(REQUEST_GET_PERSON, onRequestSocialPersonCompleteListener);
    }

    /**
     * Register a callback to be invoked when detailed social person request complete. Look for detailed persons in social networks packages.
     * @param onRequestDetailedSocialPersonCompleteListener the callback that will run
     */
    public void setOnRequestDetailedSocialPersonCompleteListener(OnRequestDetailedSocialPersonCompleteListener onRequestDetailedSocialPersonCompleteListener) {
        mGlobalListeners.put(REQUEST_GET_DETAIL_PERSON, onRequestDetailedSocialPersonCompleteListener);
    }

    /**
     * Register a callback to be invoked when {@link SocialPerson}s by array of user ids request complete.
     * @param onRequestSocialPersonsCompleteListener the callback that will run
     */
    public void setOnRequestSocialPersonsCompleteListener(OnRequestSocialPersonsCompleteListener onRequestSocialPersonsCompleteListener) {
        mGlobalListeners.put(REQUEST_GET_PERSONS, onRequestSocialPersonsCompleteListener);
    }

    /**
     * Register a callback to be invoked when check friend request complete.
     * @param onCheckIsFriendListener the callback that will run
     */
    public void setOnCheckIsFriendListener(OnCheckIsFriendCompleteListener onCheckIsFriendListener) {
        mGlobalListeners.put(REQUEST_CHECK_IS_FRIEND, onCheckIsFriendListener);
    }

    /**
     * Register a callback to be invoked when post message request complete.
     * @param onPostingCompleteListener the callback that will run
     */
    public void setOnPostingMessageCompleteListener(OnPostingCompleteListener onPostingCompleteListener) {
        mGlobalListeners.put(REQUEST_POST_MESSAGE, onPostingCompleteListener);
    }

    /**
     * Register a callback to be invoked when post photo request complete.
     * @param onPostingCompleteListener the callback that will run
     */
    public void setOnPostingPhotoCompleteListener(OnPostingCompleteListener onPostingCompleteListener) {
        mGlobalListeners.put(REQUEST_POST_PHOTO, onPostingCompleteListener);
    }

    /**
     * Register a callback to be invoked when post link request complete.
     * @param onPostingCompleteListener the callback that will run
     */
    public void setOnPostingLinkCompleteListener(OnPostingCompleteListener onPostingCompleteListener) {
        mGlobalListeners.put(REQUEST_POST_LINK, onPostingCompleteListener);
    }

    /**
     * Register a callback to be invoked when share dialog request complete.
     * @param onPostingCompleteListener the callback that will run
     */
    public void setOnPostingDialogCompleteListener(OnPostingCompleteListener onPostingCompleteListener) {
        mGlobalListeners.put(REQUEST_POST_DIALOG, onPostingCompleteListener);
    }

    /**
     * Register a callback to be invoked when get friends list request complete.
     * @param onRequestGetFriendsCompleteListener the callback that will run
     */
    public void setOnRequestGetFriendsCompleteListener(OnRequestGetFriendsCompleteListener onRequestGetFriendsCompleteListener) {
        mGlobalListeners.put(REQUEST_GET_FRIENDS, onRequestGetFriendsCompleteListener);
    }

    /**
     * Register a callback to be invoked when invite friend request complete.
     * @param onRequestAddFriendCompleteListener the callback that will run
     */
    public void setOnRequestAddFriendCompleteListener(OnRequestAddFriendCompleteListener onRequestAddFriendCompleteListener) {
        mGlobalListeners.put(REQUEST_ADD_FRIEND, onRequestAddFriendCompleteListener);
    }

    /**
     * Register a callback to be invoked when remove friend request complete.
     * @param onRequestRemoveFriendCompleteListener the callback that will run
     */
    public void setOnRequestRemoveFriendCompleteListener(OnRequestRemoveFriendCompleteListener onRequestRemoveFriendCompleteListener) {
        mGlobalListeners.put(REQUEST_REMOVE_FRIEND, onRequestRemoveFriendCompleteListener);
    }
}
