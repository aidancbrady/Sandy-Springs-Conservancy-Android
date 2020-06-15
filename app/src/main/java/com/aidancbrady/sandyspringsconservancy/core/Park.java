package com.aidancbrady.sandyspringsconservancy.core;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Park {

    private String name;
    private String description;
    private String phone;
    private String address;
    private String coordX;
    private String coordY;
    private List<String> imageURLs = new ArrayList<>();
    private List<String> amenities = new ArrayList<>();

    private List<Bitmap> images = new ArrayList<>();

    public void addLoadedImage(Bitmap bitmap) {
        images.add(bitmap);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getImageURLs() {
        return imageURLs;
    }

    public List<Bitmap> getImages() {
        return images;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public LatLng getLatLng() {
        return new LatLng(Double.parseDouble(coordX), Double.parseDouble(coordY));
    }

    @Override
    public String toString() {
        return getName();
    }

    public static Park parse(JSONObject obj) throws Exception {
        Park ret = new Park();
        ret.name = obj.getString("name");
        ret.description = obj.getString("description");
        ret.phone = obj.getString("phone");
        ret.address = obj.getString("address");
        ret.coordX = obj.getString("coordX");
        ret.coordY = obj.getString("coordY");
        JSONArray arr = obj.getJSONArray("images");
        for (int i = 0; i < arr.length(); i++) {
            ret.imageURLs.add(arr.getString(i));
        }
        arr = obj.getJSONArray("amenities");
        for (int i = 0; i < arr.length(); i++) {
            ret.amenities.add(arr.getString(i));
        }
        return ret;
    }
}
