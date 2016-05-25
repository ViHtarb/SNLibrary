package com.snl.vk;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.snl.core.SocialNetwork;
import com.snl.core.listener.OnLoginCompleteListener;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.List;

/**
 * Created by Viнt@rь on 13.04.2016
 */
public class VKSocialNetwork extends SocialNetwork<VKAccessToken> {
    public static final int ID = 2;

    private List<String> mPermissions;

    private final VKCallback<VKAccessToken> mLoginCallback = new VKCallback<VKAccessToken>() {
        @Override
        public void onResult(VKAccessToken res) {
            if (isRegistered(Request.LOGIN)) {
                ((OnLoginCompleteListener) getListener(Request.LOGIN)).onLoginSuccess(getId());
                cancelLoginRequest();
            }
        }

        @Override
        public void onError(VKError error) {
            if (isRegistered(Request.LOGIN)) {
                getListener(Request.LOGIN).onError(getId(), Request.LOGIN, error.toString(), null);
                cancelLoginRequest();
            }
        }
    };

    public VKSocialNetwork(Application application, List<String> permissions) {
        super(application);
        VKSdk.initialize(application);

        mPermissions = permissions;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKSdk.onActivityResult(requestCode, resultCode, data, mLoginCallback);
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public boolean isConnected() {
        return VKSdk.isLoggedIn();
    }

    @Override
    public Activity getContext() {
        return (Activity) super.getContext();
    }

    @Override
    public VKAccessToken getAccessToken() {
        return VKAccessToken.currentToken();
    }

    @Override
    public void logout() {
        VKSdk.logout();
    }

    @Override
    public void requestLogin(@NonNull OnLoginCompleteListener onLoginCompleteListener) {
        super.requestLogin(onLoginCompleteListener);
        VKSdk.login(getContext(), mPermissions.toArray(new String[mPermissions.size()]));
    }
}
