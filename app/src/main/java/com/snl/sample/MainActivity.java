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

package com.snl.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.snl.core.SocialNetwork;
import com.snl.core.SocialNetworkManager;
import com.snl.core.SocialUser;
import com.snl.core.listener.OnLoginListener;
import com.snl.core.listener.OnRequestSocialUserListener;
import com.snl.facebook.FacebookPermissions;
import com.snl.facebook.FacebookSocialNetwork;
import com.snl.google.GoogleSocialNetwork;

/**
 * Created by Viнt@rь on 28.11.2015
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!SocialNetworkManager.isRegistered(FacebookSocialNetwork.ID)) {
            SocialNetworkManager.register(new FacebookSocialNetwork(getApplication(), FacebookPermissions.getPermissions()));

            GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("852514138494-qujmugi7qo7k2f0p8lmc0trni3maq5va.apps.googleusercontent.com")
                    .requestEmail()
                    .requestProfile()
                    .build();

            SocialNetworkManager.register(new GoogleSocialNetwork(getApplication(), signInOptions));
        }

        final SocialNetwork facebookSocialNetwork = SocialNetworkManager.get(FacebookSocialNetwork.ID);
        final SocialNetwork googleSocialNetwork = SocialNetworkManager.get(GoogleSocialNetwork.ID);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSocialNetwork.logout();
                googleSocialNetwork.requestLogin(new OnLoginListener() {
                    @Override
                    public void onLoginSuccess(int socialNetworkId) {
                        Log.d("TEST", "loginSuccess");
                    }

                    @Override
                    public void onError(int socialNetworkId, SocialNetwork.Request request, String errorMessage, Object data) {
                        Log.d("TEST", "onError" + " " + errorMessage);
                    }
                });
/*                if (socialNetwork.isConnected()) {
                    socialNetwork.logout();
                } else {
                    socialNetwork.requestLogin(new OnLoginListener() {
                        @Override
                        public void onLoginSuccess(int socialNetworkID) {
                            Log.d("TEST", "onLoginSuccess");
                        }

                        @Override
                        public void onError(int socialNetworkID, SocialNetwork.Request request, String errorMessage, Object data) {
                            Log.d("TEST", "onError" + " " + errorMessage);
                        }
                    });
                }*/
            }
        });

        Button friendsButton = findViewById(R.id.friends_button);
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSocialNetwork.requestCurrentUser(new OnRequestSocialUserListener() {
                    @Override
                    public void onRequestSocialUserSuccess(int socialNetworkId, SocialUser socialUser) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("user", socialUser);

                        SocialUser user = bundle.getParcelable("user");
                        Log.d("TEST", user.toString());
                    }

                    @Override
                    public void onError(int socialNetworkId, SocialNetwork.Request request, String errorMessage, Object data) {

                    }
                });
                /*facebookSocialNetwork.requestFriends(new OnRequestFriendsListener() {

                    @Override
                    public void onRequestFriendsSuccess(int socialNetworkId, List<SocialUser> socialUsers) {
                        Log.d("TEST", "onRequestFriendsSuccess");
                        Log.d("TEST", "friends list size = " + socialUsers.size());
                    }

                    @Override
                    public void onError(int socialNetworkId, SocialNetwork.Request request, String errorMessage, Object data) {

                    }
                });*/
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialNetworkManager.onActivityResult(requestCode, resultCode, data);
    }
}
