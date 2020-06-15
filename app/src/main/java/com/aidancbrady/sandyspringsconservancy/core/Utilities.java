package com.aidancbrady.sandyspringsconservancy.core;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

public class Utilities {

    private static final float METERS_TO_MILES = 0.000621371F;

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static float distance(LatLng pos1, LatLng pos2) {
        float[] ret = new float[3];
        Location.distanceBetween(pos1.latitude, pos1.longitude, pos2.latitude, pos2.longitude, ret);
        return ret[0];
    }

    public static float distanceInMiles(LatLng pos1, LatLng pos2) {
        return distance(pos1, pos2) * METERS_TO_MILES;
    }

    public static Drawable getDrawableByName(Context context, String name) {
        name = name.toLowerCase()
                .replace(" ", "_")
                .replace("/", "_");
        int id = context.getResources().getIdentifier(name, "drawable",
                context.getPackageName());
        return ActivityCompat.getDrawable(context, id);
    }
}
