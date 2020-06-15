package com.aidancbrady.sandyspringsconservancy.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aidancbrady.sandyspringsconservancy.R;
import com.aidancbrady.sandyspringsconservancy.core.DataHandler;
import com.aidancbrady.sandyspringsconservancy.core.Utilities;

import java.util.ArrayList;
import java.util.List;

public class AmenitySearchFragment extends Fragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridView grid = getView().findViewById(R.id.amenityGridView);
        grid.setAdapter(new AmenityGridAdapter(getContext(), new ArrayList<>(DataHandler.amenities)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_amenity_search, container, false);
    }

    private static class AmenityGridAdapter extends ArrayAdapter<String> {

        public AmenityGridAdapter(Context context, List<String> amenities) {
            super(context, R.layout.item_amenity, amenities);
        }

        @NonNull
        @Override
        public View getView(int i, View view, @NonNull ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(getContext()).
                        inflate(R.layout.item_amenity, parent, false);
            }
            String amenity = getItem(i);
            TextView nameText = view.findViewById(R.id.amenityLabelText);
            ImageView imageView = view.findViewById(R.id.amenityIcon);
            imageView.setImageDrawable(Utilities.getDrawableByName(getContext(), amenity));
            nameText.setText(amenity);
            return view;
        }
    }
}