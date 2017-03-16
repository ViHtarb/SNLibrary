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

import com.snl.core.SocialNetwork;
import com.snl.core.SocialNetworkManager;
import com.snl.core.SocialPerson;
import com.snl.core.listener.OnLoginListener;
import com.snl.core.listener.OnRequestFriendsListener;
import com.snl.facebook.FacebookPermissions;
import com.snl.facebook.FacebookSocialNetwork;

import java.util.List;

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
        }

        Button button = (Button) findViewById(R.id.fb_button);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FacebookSocialNetwork socialNetwork = (FacebookSocialNetwork) SocialNetworkManager.get(FacebookSocialNetwork.ID);

                if (socialNetwork.isConnected()) {
                    //socialNetwork.logout();
/*                    socialNetwork.requestDetailedCurrentPerson(new OnRequestDetailedSocialPersonListener<FacebookPerson>() {
                        @Override
                        public void onRequestDetailedSocialPersonSuccess(int socialNetworkID, FacebookPerson socialPerson) {
                            Log.d("TEST", "onRequestDetailedSocialPersonSuccess");
                            Log.d("TEST", socialPerson.toString());
                        }

                        @Override
                        public void onError(int socialNetworkID, SocialNetwork.Request request, String errorMessage, Object data) {

                        }
                    });*/

 /*                   if (socialNetwork instanceof FacebookSocialNetwork) {
                        ((FacebookSocialNetwork) socialNetwork).requestInvitableFriends(new OnRequestFriendsListener() {
                            @Override
                            public void onRequestFriendsSuccess(int socialNetworkId, List<? extends SocialPerson> socialFriends) {
                                Log.d("TEST", "onRequestFriendsSuccess");
                                for (SocialPerson person : socialFriends) {
                                    Log.d("TEST", person.toString());
                                }
                            }

                            @Override
                            public void onError(int socialNetworkId, SocialNetwork.Request request, String errorMessage, Object data) {
                                Log.d("TEST", "onError");
                            }
                        });
                    }*/
                    socialNetwork.requestFriends(new OnRequestFriendsListener() {

                        @Override
                        public void onRequestFriendsSuccess(int socialNetworkId, List<SocialPerson> socialFriends) {
                            Log.d("TEST", "onRequestFriendsSuccess");
                            Log.d("TEST", "friends list size = " + socialFriends.size());
                        }

                        public void onError(int socialNetworkId, SocialNetwork.Request request, String errorMessage, Object data) {

                        }
                    });
                    return;
                }
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
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialNetworkManager.onActivityResult(requestCode, resultCode, data);
    }
}
