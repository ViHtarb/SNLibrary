package com.snl.core.listener;

import com.snl.core.AccessToken;
import com.snl.core.listener.base.SocialNetworkListener;

/**
 * Interface definition for a callback to be invoked when access token request complete.
 */
public interface OnRequestAccessTokenCompleteListener extends SocialNetworkListener {
    /**
     * Called when access token request complete.
     * @param socialNetworkID id of social network where request was complete
     * @param accessToken {@link AccessToken} that social network returned
     */
    void onRequestAccessTokenComplete(int socialNetworkID, AccessToken accessToken);
}
