package com.snl.core.listener;

import com.snl.core.listener.base.SocialNetworkListener;

/**
 * Interface definition for a callback to be invoked when login complete.
 */
public interface OnLoginCompleteListener extends SocialNetworkListener {
    /**
     * Called when login complete.
     * @param socialNetworkId id of social network where request was complete
     */
    void onLoginSuccess(int socialNetworkId);
}
