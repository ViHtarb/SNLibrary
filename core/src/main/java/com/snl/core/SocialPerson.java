package com.snl.core;

import android.os.Parcelable;

/**
 * Created by Viнt@rь on 28.11.2015
 */
public interface SocialPerson extends Parcelable {

    String getId();
    String getName();
    String getProfileURL();
    String getAvatarURL();
    String getEmail();
}