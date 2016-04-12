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
import com.snl.core.listener.OnLoginCompleteListener;
import com.snl.core.listener.OnRequestDetailedSocialPersonCompleteListener;
import com.snl.facebook.FacebookPermissions;
import com.snl.facebook.FacebookPerson;
import com.snl.facebook.FacebookSocialNetwork;

/**
 * Created by Viнt@rь on 28.11.2015
 */
public class MainActivity extends AppCompatActivity {
    private SocialNetworkManager mSocialNetworkManager = SocialNetworkManager.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!mSocialNetworkManager.isSocialNetworkExists(FacebookSocialNetwork.ID)) {
            mSocialNetworkManager.addSocialNetwork(new FacebookSocialNetwork(this, FacebookPermissions.getPermissions()));
        }

        Button button = (Button) findViewById(R.id.fb_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(FacebookSocialNetwork.ID);
                if (socialNetwork.isConnected()) {
                    //socialNetwork.logout();
                    mSocialNetworkManager.getSocialNetwork(FacebookSocialNetwork.ID).requestDetailedCurrentPerson(new OnRequestDetailedSocialPersonCompleteListener<FacebookPerson>() {
                        @Override
                        public void onRequestDetailedSocialPersonSuccess(int socialNetworkID, FacebookPerson socialPerson) {
                            Log.d("TEST", "onRequestDetailedSocialPersonSuccess");
                            Log.d("TEST", socialPerson.toString());
                        }

                        @Override
                        public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {

                        }
                    });
                    return;
                }
                mSocialNetworkManager.getSocialNetwork(FacebookSocialNetwork.ID).requestLogin(new OnLoginCompleteListener() {
                    @Override
                    public void onLoginSuccess(int socialNetworkID) {
                        Log.d("TEST", "onLoginSuccess");
                        mSocialNetworkManager.getSocialNetwork(FacebookSocialNetwork.ID).requestDetailedCurrentPerson(new OnRequestDetailedSocialPersonCompleteListener<FacebookPerson>() {
                            @Override
                            public void onRequestDetailedSocialPersonSuccess(int socialNetworkID, FacebookPerson socialPerson) {
                                Log.d("TEST", "onRequestDetailedSocialPersonSuccess");
                                Log.d("TEST", socialPerson.toString());
                            }

                            @Override
                            public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {

                            }
                        });
                    }

                    @Override
                    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
                        Log.d("TEST", "onError" + " " + errorMessage);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSocialNetworkManager.onActivityResult(requestCode, resultCode, data);
    }
}
