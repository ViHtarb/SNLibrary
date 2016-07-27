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
import android.util.SparseArray;

/**
 * Social network implementations manager
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
     * Get social network by id
     *
     * @param id Social network id
     * @return Social network implementation extends {@link SocialNetwork}
     * @throws SocialNetworkException
     */
    public SocialNetwork getSocialNetwork(int id) throws SocialNetworkException {
        if (!isExists(id)) {
            throw new SocialNetworkException("Social network with id = " + id + " not found");
        }

        return mSocialNetworks.get(id);
    }

    /**
     * Check is exists social network in manager by id
     *
     * @param id Social network id
     * @return Is social network exists
     */
    public boolean isExists(int id) {
        return mSocialNetworks.get(id) != null;
    }

    /**
     * @deprecated Use {@link #isExists(int)} instead.
     */
    @Deprecated
    public boolean isSocialNetworkExists(int id) {
        return isExists(id);
    }

    /**
     * Add social networks to manager
     *
     * @param socialNetwork chosen and setuped social network
     */
    public void addSocialNetwork(SocialNetwork socialNetwork) {
        if (isExists(socialNetwork.getId())) {
            throw new SocialNetworkException("Social network with id = " + socialNetwork.getId() + " already exists");
        }
        mSocialNetworks.put(socialNetwork.getId(), socialNetwork);
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with, the resultCode it returned, and any additional data from it. Overrides in chosen social network
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (int i = 0; i < mSocialNetworks.size(); ) {
            SocialNetwork socialNetwork = mSocialNetworks.valueAt(i);
            socialNetwork.onActivityResult(requestCode, resultCode, data);
        }
    }
}
