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
import com.snl.core.SocialNetwork;
import com.snl.core.SocialNetworkException;
import com.snl.core.listener.OnLoginCompleteListener;
import com.snl.core.listener.OnRequestDetailedSocialPersonCompleteListener;
import com.snl.core.listener.OnRequestSocialPersonCompleteListener;
import com.snl.core.listener.OnRequestSocialPersonsCompleteListener;

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
    public void requestLogin(final OnLoginCompleteListener listener) {
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
    public void requestCurrentPerson(final OnRequestSocialPersonCompleteListener listener) {
        super.requestCurrentPerson(listener);

        if (!isConnected()) {
            if (isRegistered(listener)) {
                listener.onError(getId(), Request.PERSON, "Please login first", null);
                cancelGetCurrentPersonRequest();
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
                    cancelGetCurrentPersonRequest();
                }
            }
        });
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void requestDetailedCurrentPerson(final OnRequestDetailedSocialPersonCompleteListener listener) {
        super.requestDetailedCurrentPerson(listener);

        if (!isConnected()) {
            if (isRegistered(listener)) {
                listener.onError(getId(), Request.DETAIL_PERSON, "Please login first", null);
                cancelGetDetailedCurrentPersonRequest();
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
                    cancelGetDetailedCurrentPersonRequest();
                }
            }
        });
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Not supported via Facebook sdk.
     * @throws SocialNetworkException
     * @param userId user id in social network
     * @param listener listener for request {@link com.snl.core.SocialPerson}
     */
    @Override
    public void requestSocialPerson(String userId, OnRequestSocialPersonCompleteListener listener) {
        throw new SocialNetworkException("requestSocialPerson isn't allowed for FacebookSocialNetwork");
    }

    /**
     * Not supported via Facebook sdk.
     * @throws SocialNetworkException
     * @param userId array of user ids in social network
     * @param listener listener for request ArrayList of {@link com.snl.core.SocialPerson}
     */
    @Override
    public void requestSocialPersons(String[] userId, OnRequestSocialPersonsCompleteListener listener) {
        throw new SocialNetworkException("requestSocialPersons isn't allowed for FacebookSocialNetwork");
    }

    /**
     * Not supported via Facebook sdk.
     * @throws SocialNetworkException
     * @param userId user id in social network
     * @param listener listener for request {@link FacebookPerson}
     */
    @Override
    public void requestDetailedSocialPerson(String userId, OnRequestDetailedSocialPersonCompleteListener listener) {
        throw new SocialNetworkException("requestDetailedSocialPerson isn't allowed for FacebookSocialNetwork");
    }

    private static FacebookPerson parsePerson(JSONObject person) {
        return GSON.fromJson(person.toString(), FacebookPerson.class);
    }
}
