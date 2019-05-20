package io.indoorlocation.polestarlocationprovider;

import android.content.Context;
import android.location.Location;

import com.polestar.naosdk.api.external.NAOERRORCODE;
import com.polestar.naosdk.api.external.NAOLocationHandle;
import com.polestar.naosdk.api.external.NAOLocationListener;
import com.polestar.naosdk.api.external.NAOSensorsListener;
import com.polestar.naosdk.api.external.NAOSyncListener;
import com.polestar.naosdk.api.external.TNAOFIXSTATUS;

import java.util.Map;

import io.indoorlocation.core.IndoorLocation;
import io.indoorlocation.core.IndoorLocationProvider;

public class PolestarIndoorLocationProvider extends IndoorLocationProvider implements NAOLocationListener, NAOSensorsListener, NAOSyncListener {

    private boolean isStarted  = false;
    private boolean dataSynchronized = false;
    private boolean shouldStart = false;
    private Context context;
    private NAOLocationHandle naoLocationHandle;
    private Map<Double, Double> floorByAlitudeMap;

    public PolestarIndoorLocationProvider(Context context, String polestarApiKey) {
        super();
        this.context = context;
        this.naoLocationHandle = new NAOLocationHandle(this.context, PolestarIndoorLocationProviderServiceManager.class, polestarApiKey, this, this);
        this.naoLocationHandle.synchronizeData(this);
    }

    public void setFloorByAlitudeMap(Map<Double, Double> floorByAlitudeMap) {
        this.floorByAlitudeMap = floorByAlitudeMap;
    }

    @Override
    public boolean supportsFloor() {
        return true;
    }

    @Override
    public void start() {
        if (!this.isStarted) {
            if (this.dataSynchronized) {
                this.naoLocationHandle.start();
                this.isStarted = true;
                this.shouldStart = false;
                this.dispatchOnProviderStarted();
            }
            else {
                shouldStart = true;
            }
        }
    }

    @Override
    public void stop() {
        if (this.isStarted) {
            this.naoLocationHandle.stop();
            this.isStarted = false;
            this.dispatchOnProviderStopped();
        }
    }

    @Override
    public boolean isStarted() {
        return this.isStarted;
    }

    /*
    NAOLocationListener
     */

    @Override
    public void onLocationChanged(Location location) {
        Location standardLocation = new Location(getName());
        standardLocation.setLatitude(location.getLatitude());
        standardLocation.setLongitude(location.getLongitude());
        standardLocation.setTime(System.currentTimeMillis());
        IndoorLocation indoorLocation = null;
        if (this.floorByAlitudeMap == null) {
            indoorLocation = new IndoorLocation(standardLocation, location.getAltitude() / 5);
        }
        else {
            Double floor = this.floorByAlitudeMap.get(location.getAltitude());
            if (floor == null) {
                indoorLocation = new IndoorLocation(standardLocation, location.getAltitude() / 5);
            }
            else {
                indoorLocation = new IndoorLocation(standardLocation, floor);
            }
        }
        this.dispatchIndoorLocationChange(indoorLocation);
    }

    @Override
    public void onLocationStatusChanged(TNAOFIXSTATUS tnaofixstatus) {

    }

    @Override
    public void onEnterSite(String s) {

    }

    @Override
    public void onExitSite(String s) {

    }

    /*
    NAOErrorListener
     */

    @Override
    public void onError(NAOERRORCODE naoerrorcode, String s) {
        this.dispatchOnProviderError(new Error(s));
    }

    /*
    NAOSensorsListener
     */

    @Override
    public void requiresCompassCalibration() {

    }

    @Override
    public void requiresWifiOn() {

    }

    @Override
    public void requiresBLEOn() {

    }

    @Override
    public void requiresLocationOn() {

    }

    /*
    NAOSyncListener
     */
    @Override
    public void onSynchronizationSuccess() {
        this.dataSynchronized = true;
        if (this.shouldStart) {
            this.start();
        }
    }

    @Override
    public void onSynchronizationFailure(NAOERRORCODE naoerrorcode, String s) {
        this.dispatchOnProviderError(new Error(s));
    }
}
