package com.aidancbrady.sandyspringsconservancy.core;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DataHandler {

    private static final String FAVORITES_TAG = "favorites";

    public static List<Bitmap> backgroundList = new ArrayList<>();
    public static List<Park> parkList = new ArrayList<>();

    public static LatLng lastLocation = new LatLng(0, 0);

    private static Set<String> favorites;

    public static Park getPark(String parkName) {
        return parkList.get(getParkIndex(parkName));
    }

    public static int getParkIndex(String parkName) {
        for (int i = 0; i < parkList.size(); i++) {
            if (parkList.get(i).getName().equals(parkName)) {
                return i;
            }
        }
        return -1;
    }

    public static void loadFavorites(Activity activity) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        favorites = new LinkedHashSet<>(sharedPref.getStringSet(FAVORITES_TAG, new LinkedHashSet<>()));
    }

    public static Set<String> getFavorites() {
        return favorites;
    }

    public static boolean isFavorite(String parkName) {
        return getFavorites().contains(parkName);
    }

    public static void toggleFavorite(Activity activity, String parkName) {
        if (favorites.contains(parkName)) {
            favorites.remove(parkName);
        } else {
            favorites.add(parkName);
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(FAVORITES_TAG, favorites);
        editor.apply();
    }
}
