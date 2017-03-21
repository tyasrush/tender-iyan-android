package com.tender.iyan.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.security.ProviderInstaller;

/**
 * Created by tyas on 3/20/17.
 */

public class LocationUtil implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int LOCATION_REQUEST = 2;
    private Context context;
    private LocationManager manager;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private TrackingLocation trackingLocation;

    public LocationUtil(Context context) {
        this.context = context;
        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        client = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100); // Update location every second
    }

    public void connect() {
        client.connect();
    }

    public void find() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (context instanceof AppCompatActivity)
                ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
        } else {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            } else {
                Location location = null;
                if (manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
                    location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } else if (manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) != null) {
                    location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                if (location != null) {
                    if (trackingLocation != null)
                        trackingLocation.onLocationFound(location);
                } else {
                    if (trackingLocation != null)
                        trackingLocation.onLocationError("location null");
                }
            }
        }
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (context instanceof AppCompatActivity)
                    ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
            } else {
                if (client.isConnected())
                    LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, locationListener);
                else
                    connect();
            }
        } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = null;
            if (manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else if (manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) != null) {
                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location != null) {
                if (trackingLocation != null)
                    trackingLocation.onLocationFound(location);
            } else {
                if (trackingLocation != null)
                    trackingLocation.onLocationError("location null");
            }
        }
    }


    @Override public void onConnected(@Nullable Bundle bundle) {

    }

    @Override public void onConnectionSuspended(int i) {
        Toast.makeText(context, "Terjadi kesalahan, koneksi tidak stabil", Toast.LENGTH_SHORT).show();
        client.connect();
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "Terjadi kesalahan saat mencari lokasi", Toast.LENGTH_SHORT).show();
    }

    public void setTrackingLocation(TrackingLocation trackingLocation) {
        this.trackingLocation = trackingLocation;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override public void onLocationChanged(Location location) {
            if (location != null) {

                if (trackingLocation != null)
                    trackingLocation.onLocationFound(location);

                LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
            } else {
                find();
            }
        }
    };

    public interface TrackingLocation {
        void onLocationFound(Location location);

        void onLocationError(String message);
    }
}
