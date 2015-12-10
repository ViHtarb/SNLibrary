package com.snl.facebook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viнt@rь on 28.11.2015
 * Compatible FB permissions
 */
public final class FacebookPermissions {

    public static final String PUBLIC_PROFILE   = "public_profile";
    public static final String PUBLISH_ACTIONS  = "publish_actions";
    public static final String EMAIL            = "email";
    public static final String USER_FRIENDS     = "user_friends";

    private FacebookPermissions() {
        // not instantiate
    }

    public static List<String> getFbPermission() {
        List<String> fbPermissions = new ArrayList<>();
        fbPermissions.add("public_profile");
        fbPermissions.add("email");
        return fbPermissions;
    }
}
