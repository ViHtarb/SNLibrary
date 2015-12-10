package com.snl.core;

/**
 * Created by Viнt@rь on 28.11.2015
 */
public class AccessToken {
    //*** Access token string for social network*/
    private String mToken;
    //*** Access secret string for social network if present*/
    private String mSecret;

    public AccessToken(String token, String secret) {
        mToken = token;
        mSecret = secret;
    }

    public String getToken() {
        return mToken;
    }

    public String getSecret() {
        return mSecret;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "token='" + mToken + '\'' +
                ", secret='" + mSecret + '\'' +
                '}';
    }
}
