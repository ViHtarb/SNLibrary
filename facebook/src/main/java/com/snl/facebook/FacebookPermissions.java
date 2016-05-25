package com.snl.facebook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viнt@rь on 28.11.2015
 * <p>Compatible FB permissions</p>
 * <p>The all FB permissions list is available in <a href="https://developers.facebook.com/docs/facebook-login/permissions/overview">documentation</a></p>
 */
public final class FacebookPermissions {

    public static final String PUBLIC_PROFILE   = "public_profile";
    public static final String PUBLISH_ACTIONS  = "publish_actions";
    public static final String EMAIL            = "email";
    public static final String USER_FRIENDS     = "user_friends";

    private FacebookPermissions() {
        // not instantiate
    }

    /**
     * @return base permissions list, contains {@link #PUBLIC_PROFILE} {@link #EMAIL}
     */
    public static List<String> getPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add(PUBLIC_PROFILE);
        permissions.add(EMAIL);
        return permissions;
    }
}
