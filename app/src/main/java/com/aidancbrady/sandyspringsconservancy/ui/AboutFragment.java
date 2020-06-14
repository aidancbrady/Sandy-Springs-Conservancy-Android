package com.aidancbrady.sandyspringsconservancy.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aidancbrady.sandyspringsconservancy.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AboutFragment extends Fragment {

    private static final String LOG_TAG = "AboutFragment";

    private String htmlText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        WebView webView = root.findViewById(R.id.webView);
        if (htmlText == null) {
            if (!loadAboutText()) {
                return root;
            }
        }
        webView.loadDataWithBaseURL("", htmlText, "text/html", "UTF-8", "");
        return root;
    }

    private boolean loadAboutText() {
        try {
            InputStream stream = getResources().openRawResource(R.raw.about);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte[] buffer = new byte[16384];
            int bytesRead;
            while ((bytesRead = stream.read(buffer, 0, buffer.length)) != -1) {
                bytes.write(buffer, 0, bytesRead);
            }
            htmlText = new String(bytes.toByteArray());
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load about HTML.");
            e.printStackTrace();
            return false;
        }
    }
}