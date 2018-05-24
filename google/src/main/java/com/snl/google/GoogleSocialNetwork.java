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
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.snl.core.AccessToken;
import com.snl.core.SocialNetwork;
import com.snl.core.listener.OnLoginListener;

/**
 * Created by Viнt@rь on 16.05.2018
 */
public class GoogleSocialNetwork extends SocialNetwork<AccessToken, ShareContent> {
    public static final int ID = 2;

    private static final int RC_SIGN_IN = 9001;

    private final GoogleSignInClient mSignInClient;

   //private GoogleSignInAccount mAccount;

    public GoogleSocialNetwork(@NonNull Application application, @NonNull GoogleSignInOptions googleSignInOptions) {
        super(application);

        mSignInClient = GoogleSignIn.getClient(application, googleSignInOptions);
    }

    @Override
    public Activity getContext() {
        return (Activity) super.getContext();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            if (isRegistered(Request.LOGIN)) {
                OnLoginListener listener = getListener(Request.LOGIN);
                //try {
                    //mAccount = task.getResult(ApiException.class); // TODO
                    listener.onLoginSuccess(getId());
                //} catch (ApiException e) {
                    //String message = GoogleSignInStatusCodes.getStatusCodeString(e.getStatusCode());
                    //listener.onError(getId(), Request.LOGIN, message, null);
                //}
            }
        }
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public AccessToken getAccessToken() {
/*        String scope = "oauth2:profile email";
        try {
            return new AccessToken(GoogleAuthUtil.getToken(getContext(), mAccount.getAccount(), scope), null);
        } catch (Exception e) {
            e.printStackTrace();
*//*
        } catch (IOException e) {
        } catch (GoogleAuthException e) {
*//*
        }*/
        return null;
    }

    @Override
    public void logout() {
        mSignInClient.signOut();
    }

    @Override
    public void requestLogin(OnLoginListener listener) {
        super.requestLogin(listener);
        getContext().startActivityForResult(mSignInClient.getSignInIntent(), RC_SIGN_IN);
    }
}