package io.indoorlocation.polestar.demoapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

import io.indoorlocation.polestarlocationprovider.PolestarIndoorLocationProvider;
import io.mapwize.mapwizecomponents.ui.MapwizeFragment;
import io.mapwize.mapwizeformapbox.api.MapwizeObject;
import io.mapwize.mapwizeformapbox.map.MapOptions;
import io.mapwize.mapwizeformapbox.map.MapwizePlugin;

public class MapActivity extends AppCompatActivity implements MapwizeFragment.OnFragmentInteractionListener {

    private MapboxMap mapboxMap;
    private MapwizePlugin mapwizePlugin;
    private MapwizeFragment mapwizeFragment;
    private PolestarIndoorLocationProvider polestarIndoorLocationProvider;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initMap();
    }

    private void initMap() {
        mapwizeFragment = MapwizeFragment.newInstance(new MapOptions.Builder().build());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.contentLayout, mapwizeFragment);
        ft.commit();
    }

    private void startLocationService() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        else {
            setupLocationProvider();
        }
    }

    private void setupLocationProvider() {
        polestarIndoorLocationProvider = new PolestarIndoorLocationProvider(this, PolestarIndoorLocationProviderDemoApplication.POLESTAR_API_KEY);
        polestarIndoorLocationProvider.start();
        if (mapwizePlugin != null) {
            mapwizePlugin.setLocationProvider(polestarIndoorLocationProvider);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    setupLocationProvider();

                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMenuButtonClick() {

    }

    @Override
    public void onInformationButtonClick(MapwizeObject mapwizeObject) {

    }

    @Override
    public void onFragmentReady(MapboxMap mapboxMap, MapwizePlugin mapwizePlugin) {
        this.mapboxMap = mapboxMap;
        this.mapwizePlugin = mapwizePlugin;

        startLocationService();
    }

    @Override
    public void onFollowUserButtonClickWithoutLocation() {

    }

    @Override
    public boolean shouldDisplayInformationButton(MapwizeObject mapwizeObject) {
        return false;
    }

    @Override
    public boolean shouldDisplayFloorController(List<Double> floors) {
        return false;
    }
}
