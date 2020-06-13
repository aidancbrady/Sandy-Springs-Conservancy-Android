package com.aidancbrady.sandyspringsconservancy.core;

import android.graphics.Bitmap;

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

    public List<String> getImageURLs() {
        return imageURLs;
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
