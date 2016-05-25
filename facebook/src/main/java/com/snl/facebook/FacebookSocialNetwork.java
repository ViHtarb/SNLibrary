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
import com.facebook.internal.Utility;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.snl.core.SocialNetwork;
import com.snl.core.SocialPerson;
import com.snl.core.listener.OnLoginCompleteListener;
import com.snl.core.listener.OnRequestDetailedSocialPersonCompleteListener;
import com.snl.core.listener.OnRequestSocialPersonCompleteListener;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Viнt@rь on 28.11.2015
 * // TODO разобраться почему не пашет Profile.getCurrentProfile
 */
public class FacebookSocialNetwork extends SocialNetwork<AccessToken> {
    public static final int ID = 4;

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
    public void requestLogin(OnLoginCompleteListener onLoginCompleteListener) {
        super.requestLogin(onLoginCompleteListener);

        if (isConnected()) {
            if (mLocalListeners.get(REQUEST_LOGIN) != null) {
                mLocalListeners.get(REQUEST_LOGIN).onError(getId(), REQUEST_LOGIN, "Already logged", null);
            }
        } else {
            mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    if (mLocalListeners.get(REQUEST_LOGIN) != null) {
                        ((OnLoginCompleteListener) mLocalListeners.get(REQUEST_LOGIN)).onLoginSuccess(getId());
                        mLocalListeners.remove(REQUEST_LOGIN);
                    }
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {
                    if (mLocalListeners.get(REQUEST_LOGIN) != null) {
                        mLocalListeners.get(REQUEST_LOGIN).onError(getId(), REQUEST_LOGIN, error.getMessage(), null);
                        mLocalListeners.remove(REQUEST_LOGIN);
                    }
                }
            });
            mLoginManager.logInWithReadPermissions(getContext(), mPermissions);
        }
    }

    @Override
    public void requestCurrentPerson(OnRequestSocialPersonCompleteListener onRequestSocialPersonCompleteListener) {
        super.requestCurrentPerson(onRequestSocialPersonCompleteListener);

        if (!isConnected()) {
            if (mLocalListeners.get(REQUEST_GET_CURRENT_PERSON) != null) {
                mLocalListeners.get(REQUEST_GET_CURRENT_PERSON).onError(getId(), REQUEST_GET_PERSON, "Please login first", null);
            }
            return;
        }

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, link, email");

        GraphRequest request = GraphRequest.newMeRequest(getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    if (mLocalListeners.get(REQUEST_GET_CURRENT_PERSON) != null) {
                        mLocalListeners.get(REQUEST_GET_CURRENT_PERSON).onError(getId(), REQUEST_GET_CURRENT_PERSON, response.getError().getErrorMessage(), null);
                    }
                    return;
                }

                if (mLocalListeners.get(REQUEST_GET_CURRENT_PERSON) != null) {
                    SocialPerson person = parsePerson(object);

                    ((OnRequestSocialPersonCompleteListener) mLocalListeners.get(REQUEST_GET_CURRENT_PERSON))
                            .onRequestSocialPersonSuccess(getId(), person);
                }
            }
        });
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void requestDetailedSocialPerson(String userId, OnRequestDetailedSocialPersonCompleteListener onRequestDetailedSocialPersonCompleteListener) {
        super.requestDetailedSocialPerson(userId, onRequestDetailedSocialPersonCompleteListener);

        if (!isConnected()) {
            if (mLocalListeners.get(REQUEST_GET_DETAIL_PERSON) != null) {
                mLocalListeners.get(REQUEST_GET_DETAIL_PERSON).onError(getId(), REQUEST_GET_DETAIL_PERSON, "Please login first", null);
            }
            return;
        }

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, first_name, middle_name, last_name, link, email, gender, birthday, verified");

        GraphRequest request = GraphRequest.newMeRequest(getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    if (mLocalListeners.get(REQUEST_GET_DETAIL_PERSON) != null) {
                        mLocalListeners.get(REQUEST_GET_DETAIL_PERSON).onError(getId(), REQUEST_GET_DETAIL_PERSON, response.getError().getErrorMessage(), null);
                    }
                    return;
                }

                if (mLocalListeners.get(REQUEST_GET_DETAIL_PERSON) != null) {
                    FacebookPerson person = parsePerson(object);

                    ((OnRequestDetailedSocialPersonCompleteListener) mLocalListeners.get(REQUEST_GET_DETAIL_PERSON))
                        .onRequestDetailedSocialPersonSuccess(getId(), person);
                }
            }
        });
        request.setParameters(parameters);
        request.executeAsync();
    }

    private static FacebookPerson parsePerson(JSONObject person) {
        return GSON.fromJson(person.toString(), FacebookPerson.class);
    }
}
