package com.snl.core.listener;

import com.snl.core.SocialPerson;
import com.snl.core.listener.base.SocialNetworkListener;
/**
 * Interface definition for a callback to be invoked when social person request complete.
 *
 * @author Anton Krasov
 * @author Evgeny Gorbin (gorbin.e.o@gmail.com)
 */
public interface OnRequestSocialPersonCompleteListener extends SocialNetworkListener {
    /**
     * Called when social person request complete.
     * @param socialNetworkId id of social network where request was complete
     * @param socialPerson requested {@link SocialPerson}
     */
    void onRequestSocialPersonSuccess(int socialNetworkId, SocialPerson socialPerson);
}
