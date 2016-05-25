package com.snl.core.listener;

import com.snl.core.SocialPerson;
import com.snl.core.listener.base.SocialNetworkListener;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when array of social person request complete.
 *
 * @author Evgeny Gorbin (gorbin.e.o@gmail.com)
 */
public interface OnRequestSocialPersonsCompleteListener extends SocialNetworkListener {
    /**
     * Called when array of social person request complete.
     * @param socialNetworkId id of social network where request was complete
     * @param socialPersons ArrayList of requested {@link SocialPerson}
     */
    void onRequestSocialPersonsSuccess(int socialNetworkId, ArrayList<SocialPerson> socialPersons);
}
