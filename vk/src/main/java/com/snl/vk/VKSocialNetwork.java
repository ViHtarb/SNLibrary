package com.snl.vk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

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
    public static final int ID = 5;

    private List<String> mPermissions;

    private final VKCallback<VKAccessToken> mLoginCallback = new VKCallback<VKAccessToken>() {
        @Override
        public void onResult(VKAccessToken res) {
            if (mLocalListeners.get(REQUEST_LOGIN) != null) {
                ((OnLoginCompleteListener) mLocalListeners.get(REQUEST_LOGIN)).onLoginSuccess(getId());
                mLocalListeners.remove(REQUEST_LOGIN);
            }
        }

        @Override
        public void onError(VKError error) {
            mLocalListeners.get(REQUEST_LOGIN).onError(getId(), REQUEST_LOGIN, error.toString(), null);
        }
    };

    public VKSocialNetwork(Context context, List<String> permissions) {
        super(context);

        VKSdk.initialize(context.getApplicationContext());

        mPermissions = permissions;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
    public void requestLogin(OnLoginCompleteListener onLoginCompleteListener) {
        super.requestLogin(onLoginCompleteListener);
        VKSdk.login(getContext(), mPermissions.toArray(new String[mPermissions.size()]));
    }
}
