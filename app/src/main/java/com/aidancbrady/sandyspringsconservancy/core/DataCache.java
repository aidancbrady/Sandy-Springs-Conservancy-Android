package com.aidancbrady.sandyspringsconservancy.core;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DataCache {

    public static List<Bitmap> backgroundList = new ArrayList<>();
    public static List<Park> parkList = new ArrayList<>();

    public static LatLng lastLocation = new LatLng(0, 0);

    public static int getParkIndex(String parkName) {
        for (int i = 0; i < parkList.size(); i++) {
            if (parkList.get(i).getName().equals(parkName)) {
                return i;
            }
        }
        return -1;
    }
}
