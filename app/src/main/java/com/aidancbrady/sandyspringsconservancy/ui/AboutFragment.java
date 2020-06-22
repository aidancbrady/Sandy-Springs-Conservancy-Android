package com.aidancbrady.sandyspringsconservancy.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aidancbrady.sandyspringsconservancy.MenuActivity;
import com.aidancbrady.sandyspringsconservancy.R;

public class AboutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        WebView webView = root.findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/about.html");
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MenuActivity) getActivity()).setMenuState(false);
    }
}