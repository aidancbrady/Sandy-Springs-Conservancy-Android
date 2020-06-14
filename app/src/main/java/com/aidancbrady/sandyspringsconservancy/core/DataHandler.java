package com.aidancbrady.sandyspringsconservancy.core;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class DataHandler {

    private static final String LOG_TAG = "DataHandler";

    private File cacheDir;
    private File cacheFile;

    private boolean connected;

    public DataHandler(Context context) {
        connected = Utilities.isOnline(context);
        cacheDir = new File(context.getFilesDir(), Constants.CACHE_DIR);
        cacheFile = new File(cacheDir, Constants.DATA_FILE);
    }

    public boolean loadData() {
        // TODO remove
        //flushCache();
        int storedVersion = getStoredVersion(cacheFile);
        if (storedVersion == -1) {
            if (!connected) {
                // if we don't have a cache stored and we're offline, fail
                return false;
            }
            JSONObject remoteData = parseJSON(readURL(Constants.DATA_FILE_URL));
            if (remoteData == null) {
                return false;
            }
            return loadRemoteData(remoteData);
        }

        if (connected) {
            JSONObject remoteData = parseJSON(readURL(Constants.DATA_FILE_URL));
            if (remoteData != null) {
                int remoteVersion = getVersion(remoteData);
                if (remoteVersion != -1 && storedVersion != remoteVersion) {
                    return loadRemoteData(remoteData);
                }
            }
        }

        return loadLocalData();
    }

    private boolean loadLocalData() {
        JSONObject obj = parseJSON(readFile(cacheFile));
        if (obj == null) {
            return false;
        }
        try {
            loadBackgrounds(obj);
            JSONArray parksArray = obj.getJSONArray("parks");
            loadParks(parksArray);
            loadSecondaryImages();
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error loading local data.");
            e.printStackTrace();
            return false;
        }
    }

    private boolean loadRemoteData(JSONObject remoteData) {
        try {
            flushCache();
            writeToFile(cacheFile, remoteData.toString().getBytes());
            loadBackgrounds(remoteData);
            JSONArray parksArray = remoteData.getJSONArray("parks");
            loadParks(parksArray);
            loadSecondaryImages();
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error loading remote data.");
            e.printStackTrace();
            return false;
        }
    }

    private void flushCache() {
        if (cacheDir.exists()) {
            for (File file : cacheDir.listFiles()) {
                file.delete();
            }
        } else {
            cacheDir.mkdirs();
        }
    }

    private void loadBackgrounds(JSONObject data) throws Exception {
        JSONArray backgroundArray = data.getJSONArray("backgrounds");
        for (int i = 0; i < backgroundArray.length(); i++) {
            String imgPath = backgroundArray.getString(i);
            File imgFile = new File(cacheDir, imgPath);
            if (connected && !imgFile.exists()) {
                writeToFile(imgFile, readURL(Constants.DATA_URL + imgPath));
            }
            DataCache.backgroundList.add(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        }
    }

    private void loadImages(Park park, boolean primary) {
        try {
            boolean didInitial = false;

            for (String image : park.getImageURLs()) {
                File localFile = new File(cacheDir, image);
                String urlPath = Constants.DATA_URL + image;

                if (primary || didInitial) {
                    if (connected && !localFile.exists()) {
                        writeToFile(localFile, readURL(urlPath));
                    }
                    park.addLoadedImage(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                }

                if (primary) {
                    return;
                }
                didInitial = true;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load images.");
            e.printStackTrace();
        }
    }

    private void loadSecondaryImages() {
        new Thread(() -> {
            for (Park park : DataCache.parkList) {
                loadImages(park, false);
            }
        }).start();
    }

    private void loadParks(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                Park park = Park.parse(array.getJSONObject(i));
                loadImages(park, true);
                DataCache.parkList.add(park);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to parse parks from JSON.");
            e.printStackTrace();
        }
    }

    private static void writeToFile(File file, byte[] contents) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(contents);
            fos.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error writing to file " + file);
        }
    }

    private static int getStoredVersion(File dataFile) {
        if (!dataFile.exists()) {
            return -1;
        }
        JSONObject obj = parseJSON(readFile(dataFile));
        if (obj == null) {
            return -1;
        }
        return getVersion(obj);
    }

    private static int getVersion(JSONObject obj) {
        try {
            return obj.optInt("version", -1);
        } catch (Exception e) {
            return -1;
        }
    }

    private static byte[] readFile(File file) {
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fos = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fos.read(bytes);
            fos.close();
            return bytes;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error loading file " + file.getAbsolutePath());
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] readURL(String textURL) {
        try {
            URL url = new URL(textURL);
            InputStream stream = url.openStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int bytesRead = 0;
            byte [] buffer = new byte[1024];
            while (-1 != (bytesRead = stream.read(buffer))) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error reading from URL " + textURL);
            e.printStackTrace();
            return null;
        }
    }

    private static JSONObject parseJSON(byte[] bytes) {
        try {
            String text = new String(bytes);
            return new JSONObject(text);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error parsing JSON from bytes.");
            e.printStackTrace();
            return null;
        }
    }
}
