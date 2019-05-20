package io.indoorlocation.polestar.demoapp;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

import io.mapwize.mapwizeformapbox.AccountManager;

public class PolestarIndoorLocationProviderDemoApplication extends Application {

    static final String MAPWIZE_API_KEY = "";
    static final String POLESTAR_API_KEY = "emulator";


    @Override
    public void onCreate() {
        super.onCreate();
        AccountManager.start(this, MAPWIZE_API_KEY);
    }

}
