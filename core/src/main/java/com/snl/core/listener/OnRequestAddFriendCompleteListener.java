package com.snl.core.listener;

import com.snl.core.listener.base.SocialNetworkListener;

/**
 * Interface definition for a callback to be invoked when invite friend request complete.
 *
 * @author Evgeny Gorbin (gorbin.e.o@gmail.com)
 */
public interface OnRequestAddFriendCompleteListener extends SocialNetworkListener {

    /**
     * Called when invite friend request complete.
     * @param socialNetworkId id of social network where request was complete
     * @param userId user id that was invited
     */
    void onRequestAddFriendComplete(int socialNetworkId, String userId);
}
