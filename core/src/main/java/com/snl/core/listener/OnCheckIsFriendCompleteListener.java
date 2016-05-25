package com.snl.core.listener;

import com.snl.core.listener.base.SocialNetworkListener;

/**
 * Interface definition for a callback to be invoked when check friend request complete.
 */
public interface OnCheckIsFriendCompleteListener extends SocialNetworkListener {
    /**
     * Called when check friend request complete.
     * @param socialNetworkId id of social network where request was complete
     * @param userId user id that was checked
     * @param isFriend true if friends, else false
     */
    void onCheckIsFriendComplete(int socialNetworkId, String userId, boolean isFriend);
}
