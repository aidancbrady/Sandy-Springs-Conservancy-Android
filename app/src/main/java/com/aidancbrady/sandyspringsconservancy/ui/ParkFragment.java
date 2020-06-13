package com.aidancbrady.sandyspringsconservancy.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidancbrady.sandyspringsconservancy.R;
import com.aidancbrady.sandyspringsconservancy.core.DataCache;
import com.aidancbrady.sandyspringsconservancy.core.Park;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ParkFragment extends Fragment {

    private static final String LOG_TAG = "ParkFragment";

    private Park park;
    private MapView mapView;

    public ParkFragment() {
        // Required empty public constructor
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            park = DataCache.parkList.get(getArguments().getInt("park"));
            LinearLayout layout = getView().findViewById(R.id.park_layout);
            LinearLayout imageLayout = getView().findViewById(R.id.imageLinearLayout);
            for (int i = 0; i < park.getImages().size(); i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setId(i);
                imageView.setImageBitmap(park.getImages().get(i));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
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

            View amenityView = getView().findViewById(R.id.amenityView);
            amenityView.setMinimumHeight(300);

            mapView = getView().findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            try {
                MapsInitializer.initialize(getActivity());
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error initializing map.");
                e.printStackTrace();
            }
            mapView.getMapAsync(map -> {
                LatLng pos = park.getLatLng();
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pos, 13);
                map.addMarker(new MarkerOptions().position(pos).draggable(false).visible(true)
                        .title(park.getName()).snippet(park.getAddress()));
                map.animateCamera(update);
            });
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_park, container, false);
    }
}