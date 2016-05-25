package com.snl.core.listener;

import com.snl.core.listener.base.SocialNetworkListener;

/**
 * Interface definition for a callback to be invoked when remove friend request complete.
 *
 * @author Evgeny Gorbin (gorbin.e.o@gmail.com)
 */
public interface OnRequestRemoveFriendCompleteListener extends SocialNetworkListener {
    /**
     * Called when remove friend request complete.
     * @param socialNetworkId id of social network where request was complete
     * @param userId user id that was removed
     */
    void onRequestRemoveFriendComplete(int socialNetworkId, String userId);
}
