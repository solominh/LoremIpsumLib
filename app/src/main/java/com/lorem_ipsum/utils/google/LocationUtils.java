package com.lorem_ipsum.utils.google;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.lorem_ipsum.utils.AppUtils;

import java.util.List;


/**
 * Created by viethoa on 6/4/15.
 */
public class LocationUtils implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private Location mLastKnownLocation = null;
    private boolean mRequestingLocationUpdates = false;

    private static LocationUtils instance = null;

    private LocationUtils() {
        setup();
    }

    public static LocationUtils getInstance() {
        if (instance == null) {
            instance = new LocationUtils();
        }
        return instance;
    }

    public void setup() {
        buildGoogleApiClient();
    }

    //-----------------------------------------------------------------------------
    //- LifeCycle - hoangminh - 10:20 AM - 2/16/16

    public void onResume() {
        // Reconnect
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
            return;
        }

        // Start location update
        if (mRequestingLocationUpdates) {
            requestLocationUpdates();
        }
    }

    public void onPause() {
        removeLocationUpdates();
    }

    public void onDestroy() {
        mGoogleApiClient.disconnect();
        removeLocationUpdates();
        mGoogleApiClient = null;
        mLocationRequest = null;
    }

    //-----------------------------------------------------------------------------
    //- Location  - hoangminh - 10:20 AM - 2/16/16

    protected void requestLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, getLocationRequest(), this);
    }

    protected void removeLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    //-----------------------------------------------------------------------------
    //- Location utils - hoangminh - 11:21 AM - 2/16/16

    public Location getLastKnownLocation() {
        return mLastKnownLocation;
    }

    public Location getLastKnownLocationV1() {
        Context context = AppUtils.getAppContext();
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Get all providers
        List<String> providers = locationManager.getAllProviders();

        // Get best location with smallest accuracy
        Location bestLocation = null;
        for (String provider : providers) {
            Location location = locationManager.getLastKnownLocation(provider);
            // Sanity check
            if (location == null) {
                continue;
            }

            // Get best location
            if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = location;
            }
        }

        return bestLocation;
    }

    public void connect(boolean requestingLocationUpdates) {
        mRequestingLocationUpdates = requestingLocationUpdates;
        mGoogleApiClient.connect();
    }

    public void startLocationUpdates() {
        mRequestingLocationUpdates = true;
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
            return;
        }
        requestLocationUpdates();
    }

    public void stopLocationUpdates() {
        mRequestingLocationUpdates = false;
        removeLocationUpdates();
    }

    //-----------------------------------------------------------------------------
    //- Interface - hoangminh - 10:13 AM - 2/16/16

    @Override
    public void onConnected(Bundle connectionHint) {
        // Last know location
        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        // Location update
        if (mRequestingLocationUpdates) {
            requestLocationUpdates();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastKnownLocation = location;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // Reconnect
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do something
    }

    //-----------------------------------------------------------------------------
    //- Build GoogleApiClient - hoangminh - 8:54 AM - 2/16/16

    protected synchronized void buildGoogleApiClient() {
        Context context = AppUtils.getAppContext();

        // Api client
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected LocationRequest getLocationRequest() {
        if (mLocationRequest != null)
            return mLocationRequest;

        mLocationRequest = new LocationRequest();

        // True interval to receive location update
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // If other app has faster interval, system will send location update => our app will receive it unexpectedly
        // If we just want normal update => Fastest_update_interval = Normal_interval
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        // Balance between accuracy and power usage
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return mLocationRequest;
    }
}
