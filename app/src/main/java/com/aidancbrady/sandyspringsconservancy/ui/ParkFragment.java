package com.aidancbrady.sandyspringsconservancy.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aidancbrady.sandyspringsconservancy.MenuActivity;
import com.aidancbrady.sandyspringsconservancy.R;
import com.aidancbrady.sandyspringsconservancy.core.DataHandler;
import com.aidancbrady.sandyspringsconservancy.core.Park;
import com.aidancbrady.sandyspringsconservancy.core.Utilities;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class ParkFragment extends Fragment {

    public static final String PARK_BUNDLE_TAG = "parkName";
    private static final String LOG_TAG = "ParkFragment";

    private Park park;
    private MapView mapView;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            park = DataHandler.getPark(getArguments().getString(PARK_BUNDLE_TAG));
            // set title to park name
            ((MenuActivity) getActivity()).getSupportActionBar().setTitle(park.getName());

            LinearLayout imageLayout = getView().findViewById(R.id.imageLinearLayout);
            for (int i = 0; i < park.getImages().size(); i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setId(i);
                imageView.setImageBitmap(park.getImages().get(i));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                int height = (int) (width * 9D / 16D);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
                imageLayout.addView(imageView);
            }

            TextView description = getView().findViewById(R.id.descriptionText);
            description.setText(park.getDescription());

            TextView phoneNumberText = getView().findViewById(R.id.phoneNumberText);
            phoneNumberText.setText(park.getPhoneNumber());
            phoneNumberText.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + park.getPhoneNumber()));
                startActivity(intent);
            });

            mapView = getView().findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            try {
                MapsInitializer.initialize(getActivity());
            } catch (Exception e) {
                Utilities.error(LOG_TAG, "Error initializing map.");
                e.printStackTrace();
            }
            mapView.getMapAsync(map -> {
                LatLng pos = park.getLatLng();
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pos, 13);
                map.addMarker(new MarkerOptions().position(pos).draggable(false).visible(true)
                        .title(park.getName()).snippet(park.getAddress()));
                map.animateCamera(update);
            });
            setHasOptionsMenu(true);

            GridView grid = getView().findViewById(R.id.amenityGridView);
            grid.setAdapter(new AmenityGridAdapter(getContext(), park.getAmenities()));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.add("favorite");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        setFavoriteIconState(item);
        item.setOnMenuItemClickListener(i -> {
            DataHandler.toggleFavorite(getActivity(), park.getName());
            setFavoriteIconState(item);
            return true;
        });
    }

    private void setFavoriteIconState(MenuItem item) {
        boolean favorite = DataHandler.isFavorite(park.getName());
        item.setIcon(favorite ? R.drawable.ic_favorite_true :
                R.drawable.ic_favorite_false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MenuActivity) getActivity()).setMenuState(false);
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_park, container, false);
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