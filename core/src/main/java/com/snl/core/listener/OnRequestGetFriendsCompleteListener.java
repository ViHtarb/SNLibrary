package com.snl.core.listener;

import com.snl.core.SocialPerson;
import com.snl.core.listener.base.SocialNetworkListener;

import java.util.ArrayList;
/**
 * Interface definition for a callback to be invoked when friends list request complete.
 *
 * @author Evgeny Gorbin (gorbin.e.o@gmail.com)
 */
public interface OnRequestGetFriendsCompleteListener extends SocialNetworkListener {
    /**
     * Called when friends list request complete.
     * @param socialNetworkId id of social network where request was complete
     * @param friendsId array of friends list's user ids
     */
    void OnGetFriendsIdComplete(int socialNetworkId, String[] friendsId);

    /**
     * Called when friends list request complete.
     * @param socialNetworkId id of social network where request was complete
     * @param socialFriends ArrayList of of friends list's social persons
     */
    void OnGetFriendsComplete(int socialNetworkId, ArrayList<SocialPerson> socialFriends);
}
