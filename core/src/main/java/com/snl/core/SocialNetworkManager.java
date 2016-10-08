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

package com.snl.core;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.SparseArray;

/**
 * SocialNetworkManager
 */
public class SocialNetworkManager {
    private SparseArray<SocialNetwork> mSocialNetworks = new SparseArray<>();

    private static SocialNetworkManager sInstance = new SocialNetworkManager();

    public static SocialNetworkManager getInstance() {
        return sInstance;
    }

    private SocialNetworkManager() {
        // not instantiate
    }

    /**
     * Check is {@link SocialNetwork} connected
     *
     * @param id {@link SocialNetwork} id
     * @return Is {@link SocialNetwork} connected
     */
    public boolean isConnected(int id) {
        return get(id).isConnected();
    }

    /**
     * Check is {@link SocialNetwork} registered
     *
     * @param socialNetwork checking {@link SocialNetwork}
     * @return Is {@link SocialNetwork} registered
     */
    public boolean isRegistered(SocialNetwork socialNetwork) {
        return isRegistered(socialNetwork.getId());
    }

    /**
     * Check is {@link SocialNetwork} registered
     *
     * @param id {@link SocialNetwork} id
     * @return Is {@link SocialNetwork} registered
     */
    public boolean isRegistered(int id) {
        return mSocialNetworks.get(id) != null;
    }

    /**
     * Get {@link SocialNetwork} from manager
     *
     * @param id {@link SocialNetwork} id
     * @return {@link SocialNetwork}
     */
    public SocialNetwork get(int id) {
        if (!isRegistered(id)) {
            throw new SocialNetworkException("SocialNetwork with id = " + id + " not registered");
        }
        return mSocialNetworks.get(id);
    }

    /**
     * Register {@link SocialNetwork} in manager
     *
     * @param socialNetwork chosen and setup {@link SocialNetwork}
     */
    public void register(@NonNull SocialNetwork socialNetwork) {
        if (isRegistered(socialNetwork)) {
            throw new SocialNetworkException("SocialNetwork " + socialNetwork.getClass().getName() + "is already registered");
        }
        mSocialNetworks.put(socialNetwork.getId(), socialNetwork);
    }

    /**
     * Logout from {@link SocialNetwork} by id
     *
     * @param id {@link SocialNetwork} id
     */
    public void logout(int id) {
        get(id).logout();
    }

    /**
     * Logout from all registered {@link SocialNetwork}`s
     */
    public void logout() {
        for (int i = 0; i < mSocialNetworks.size(); i++) {
            SocialNetwork socialNetwork = mSocialNetworks.valueAt(i);
            socialNetwork.logout();
        }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with, the resultCode it returned, and any additional data from it. Overrides in chosen social network
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (int i = 0; i < mSocialNetworks.size(); i++) {
            SocialNetwork socialNetwork = mSocialNetworks.valueAt(i);
            socialNetwork.onActivityResult(requestCode, resultCode, data);
        }
    }
}
