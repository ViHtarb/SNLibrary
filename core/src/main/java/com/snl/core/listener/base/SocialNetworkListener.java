package com.snl.core.listener.base;

import com.snl.core.SocialNetwork;

/**
 * Base interface definition for a callback to be invoked when any social network request complete.
 */
public interface SocialNetworkListener {
    /**
     * Called when social network request complete with error.
     * @param socialNetworkId id of social network where request was complete with error
     * @param request request where occurred error
     * @param errorMessage error message where request was complete with error
     * @param data data of social network where request was complete with error
     */
    void onError(int socialNetworkId, SocialNetwork.Request request, String errorMessage, Object data);
}
