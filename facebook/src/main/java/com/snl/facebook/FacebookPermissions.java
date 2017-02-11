/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2016. Viнt@rь
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.snl.facebook;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringDef;

import com.facebook.AccessToken;

import java.lang.annotation.Retention;
import java.util.HashSet;
import java.util.Set;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;
import static android.support.annotation.RestrictTo.Scope.SUBCLASSES;

/**
 * <p>
 *     Compatible FB permissions
 * </p>
 * <p>
 *     The all FB permissions list is available in
 *     <a href="https://developers.facebook.com/docs/facebook-login/permissions/overview">documentation</a>
 * </p>
 */
public final class FacebookPermissions {

    public static final String PUBLIC_PROFILE = "public_profile";
    public static final String EMAIL = "email";
    public static final String USER_FRIENDS = "user_friends";

    @RestrictTo(SUBCLASSES)
    static final String PUBLISH_ACTIONS = "publish_actions";

    private FacebookPermissions() {
        // not instantiate
    }

    /**
     * @return base permissions list, contains {@link #PUBLIC_PROFILE}, {@link #EMAIL}, {@link #USER_FRIENDS}
     */
    public static Set<String> getPermissions() {
        Set<String> permissions = new HashSet<>();
        permissions.add(PUBLIC_PROFILE);
        permissions.add(EMAIL);
        permissions.add(USER_FRIENDS);
        return permissions;
    }

    /**
     * @return is permission granted
     */
    public static boolean isPermissionGranted(@NonNull String permission) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains(permission);
    }
}
