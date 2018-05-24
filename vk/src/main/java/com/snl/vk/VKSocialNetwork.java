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

package com.snl.vk;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.snl.core.SocialNetwork;
import com.snl.core.listener.OnLoginListener;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.model.VKAttachments;

import java.util.List;

/**
 * Created by Viнt@rь on 13.04.2016
 */
public class VKSocialNetwork extends SocialNetwork<VKAccessToken, VKAttachments> {
    public static final int ID = 3;

    private List<String> mPermissions;

    private final VKCallback<VKAccessToken> mLoginCallback = new VKCallback<VKAccessToken>() {
        @Override
        public void onResult(VKAccessToken res) {
            if (isRegistered(Request.LOGIN)) {
                ((OnLoginListener) getListener(Request.LOGIN)).onLoginSuccess(getId());
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
    public void requestLogin(@NonNull OnLoginListener onLoginListener) {
        super.requestLogin(onLoginListener);
        VKSdk.login(getContext(), mPermissions.toArray(new String[0]));
    }
}
