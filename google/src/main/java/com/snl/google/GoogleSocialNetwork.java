/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2018. Viнt@rь
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

package com.snl.google;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.snl.core.AccessToken;
import com.snl.core.SocialNetwork;
import com.snl.core.listener.OnLoginListener;
import com.snl.core.listener.OnRequestSocialUserListener;

import androidx.annotation.NonNull;

/**
 * Created by Viнt@rь on 16.05.2018
 */
public class GoogleSocialNetwork extends SocialNetwork<AccessToken, ShareContent> {
    public static final int ID = 2;

    private static final int RC_SIGN_IN = 9001;

    private final GoogleSignInClient mSignInClient;

    private GoogleSignInAccount mAccount;
    private AccessToken mAccessToken;

    public GoogleSocialNetwork(@NonNull Application application, @NonNull GoogleSignInOptions googleSignInOptions) {
        super(application);

        mSignInClient = GoogleSignIn.getClient(application, googleSignInOptions);
        mAccount = GoogleSignIn.getLastSignedInAccount(application);
        if (mAccount != null) {
            mAccessToken = new AccessToken(mAccount.getIdToken(), null);
        }
    }

    @Override
    public Activity getContext() {
        return (Activity) super.getContext();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (isRegistered(Request.LOGIN)) {
                OnLoginListener listener = getListener(Request.LOGIN);

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    mAccount = task.getResult(ApiException.class);
                    mAccessToken = new AccessToken(mAccount.getIdToken(), null);

                    listener.onLoginSuccess(getId());
                } catch (ApiException e) {
                    String message = GoogleSignInStatusCodes.getStatusCodeString(e.getStatusCode());
                    listener.onError(getId(), Request.LOGIN, message, null);
                }
                cancelLoginRequest();
            }
        }
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public boolean isConnected() {
        return mAccessToken != null;
    }

    @Override
    public AccessToken getAccessToken() {
        return mAccessToken;
    }

    @Override
    public void logout() {
        mAccount = null;
        mAccessToken = null;
        mSignInClient.signOut();
    }

    @Override
    public void requestLogin(OnLoginListener listener) {
        super.requestLogin(listener);
        getContext().startActivityForResult(mSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    @Override
    public void requestCurrentUser(OnRequestSocialUserListener listener) {
        super.requestCurrentUser(listener);

        if (!isConnected()) {
            if (isRegistered(listener)) {
                listener.onError(getId(), Request.USER, "Please login first", null);
                cancelCurrentUserRequest();
            }
            //return;
        }

        //GoogleUser googleUser = new GoogleUser(mAccount);
        //listener.onRequestSocialUserSuccess(getId(), googleUser);
    }
}