package com.aidancbrady.sandyspringsconservancy.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aidancbrady.sandyspringsconservancy.R;
import com.aidancbrady.sandyspringsconservancy.core.DataCache;
import com.aidancbrady.sandyspringsconservancy.core.Park;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    private static final LatLng CENTER_COORDS = new LatLng(33.934774, -84.392127);

    private OnMapReadyCallback callback = googleMap -> {
        for (int i = 0; i < DataCache.parkList.size(); i++) {
            Park park = DataCache.parkList.get(i);
            googleMap.addMarker(new MarkerOptions().position(park.getLatLng())
                    .title(park.getName()).snippet(park.getAddress()));
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CENTER_COORDS, 12));
        googleMap.setOnInfoWindowClickListener(marker -> {
            int index = DataCache.getParkIndex(marker.getTitle());
            Bundle bundle = new Bundle();
            bundle.putInt("parkIndex", index);
            Navigation.findNavController(getView()).navigate(R.id.nav_park, bundle);
        });
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}