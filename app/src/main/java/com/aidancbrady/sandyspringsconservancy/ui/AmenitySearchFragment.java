package com.aidancbrady.sandyspringsconservancy.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.aidancbrady.sandyspringsconservancy.R;

public class AmenitySearchFragment extends Fragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View amenityView = LayoutInflater.from(getContext()).inflate(R.layout.item_amenity, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_amenity_search, container, false);
    }
}