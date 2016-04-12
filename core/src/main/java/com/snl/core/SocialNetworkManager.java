package com.snl.core;

import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Viнt@rь on 28.11.2015
 */
public class SocialNetworkManager {
    private static final String TAG = SocialNetworkManager.class.getSimpleName();

    private Map<Integer, SocialNetwork> mSocialNetworks = new HashMap<>();

    private static SocialNetworkManager sInstance = new SocialNetworkManager();
    public static SocialNetworkManager getInstance() {
        return sInstance;
    }

    private SocialNetworkManager() {
    }

    /**
     * Get social network by id
     * @param id social network id(1 - Twitter, 2 - LinkedIn, 3 - Google Plus, 4 - Facebook, 5 - Vkontakte, 6 - Odnoklassniki, 7 - Instagram)
     * @return {@link SocialNetwork} class(FacebookSocialNetwork, TwitterSocialNetwork, LinkedInSocialNetwork, GooglePlusSocialNetwork, InstagramSocialNetwork, VkSocialNetwork, OkSocialNetwork)
     * @throws SocialNetworkException
     */
    public SocialNetwork getSocialNetwork(int id) throws SocialNetworkException {
        if (!mSocialNetworks.containsKey(id)) {
            throw new SocialNetworkException("Social network with id = " + id + " not found");
        }

        return mSocialNetworks.get(id);
    }

    public boolean isSocialNetworkExists(int id) {
        return mSocialNetworks.get(id) != null;
    }

    /**
     * Add social networks to manager
     * @param socialNetwork chosen and setuped social network
     */
    public void addSocialNetwork(SocialNetwork socialNetwork) {
        if (isSocialNetworkExists(socialNetwork.getId())) {
            throw new SocialNetworkException("Social network with id = " + socialNetwork.getId() + " already exists");
        }
        mSocialNetworks.put(socialNetwork.getId(), socialNetwork);
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with, the resultCode it returned, and any additional data from it. Overrided in chosen social network
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (SocialNetwork socialNetwork : mSocialNetworks.values()) {
            socialNetwork.onActivityResult(requestCode, resultCode, data);
        }
    }
}
