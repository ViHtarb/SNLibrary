package com.snl.core.listener;

import com.snl.core.listener.base.SocialNetworkListener;

/**
 * Interface definition for a callback to be invoked when posting complete.
 */
public interface OnPostingCompleteListener extends SocialNetworkListener {
    /**
     * Called when posting complete.
     * @param socialNetworkId id of social network where request was complete
     */
    void onPostSuccessfully(int socialNetworkId);
}
