package io.indoorlocation.polestar.demoapp;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

import io.mapwize.mapwizesdk.core.MapwizeConfiguration;

public class PolestarIndoorLocationProviderDemoApplication extends Application {

    static final String MAPWIZE_API_KEY = "YOUR_MAPWIZE_APIKEY";
    static final String POLESTAR_API_KEY = "emulator / YOUR_POLESTAR_APIKEY";

    @Override
    public void onCreate() {
        super.onCreate();
        Mapbox.getInstance(this, "pk.mapwize");
        // Mapwize globale initialization
        MapwizeConfiguration config = new MapwizeConfiguration.Builder(this, MAPWIZE_API_KEY).build();
        MapwizeConfiguration.start(config);
    }

}
