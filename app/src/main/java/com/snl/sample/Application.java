package com.snl.sample;

/**
 * Created by Viнt@rь on 28.11.2015
 */
public class Application extends android.app.Application {

    private static Application sInstance;
    public static Application getInstance() {
        return sInstance;
    }

    public Application() {
        sInstance = this;
    }
}
