package com.snl.core.listener;

import com.snl.core.SocialPerson;
import com.snl.core.listener.base.SocialNetworkListener;

/**
 * Interface definition for a callback to be invoked when detailed social person request complete.
 *
 * @author Evgeny Gorbin (gorbin.e.o@gmail.com)
 */
public interface OnRequestDetailedSocialPersonCompleteListener<T extends SocialPerson> extends SocialNetworkListener {
    /**
     * Called when detailed social person request complete.
     * @param socialNetworkId id of social network where request was complete
     * @param socialPerson Detailed social person object. Look at Person class in chosen module.
     */
    void onRequestDetailedSocialPersonSuccess(int socialNetworkId, T socialPerson);
}