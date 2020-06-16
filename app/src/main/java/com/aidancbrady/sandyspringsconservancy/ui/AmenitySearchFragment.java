package com.aidancbrady.sandyspringsconservancy.ui;

import android.content.Context;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.aidancbrady.sandyspringsconservancy.MenuActivity;
import com.aidancbrady.sandyspringsconservancy.R;
import com.aidancbrady.sandyspringsconservancy.core.DataHandler;
import com.aidancbrady.sandyspringsconservancy.core.Utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AmenitySearchFragment extends Fragment {

    private GridView amenityGridView;
    private AmenityGridAdapter amenityGridAdapter;
    private MenuItem searchButton;

    private Set<String> selectedAmenities = new HashSet<>();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        amenityGridView = getView().findViewById(R.id.amenityGridView);
        amenityGridView.setAdapter(amenityGridAdapter = new AmenityGridAdapter(getContext(), new ArrayList<>(DataHandler.amenities)));
        amenityGridView.setOnItemClickListener((parent, v, position, id) -> {
            String amenity = amenityGridAdapter.getItem(position);
            if (selectedAmenities.contains(amenity)) {
                selectedAmenities.remove(amenity);
            } else {
                selectedAmenities.add(amenity);
            }
            searchButton.setEnabled(selectedAmenities.size() > 0);
            amenityGridView.invalidateViews();
        });
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MenuActivity) getActivity()).setMenuState(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_amenity_search, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        searchButton = menu.add(R.string.search);
        searchButton.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        searchButton.setEnabled(selectedAmenities.size() > 0);
        searchButton.setOnMenuItemClickListener(i -> {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(ParkListFragment.AMENITIES_BUNDLE_TAG, new ArrayList<>(selectedAmenities));
            Navigation.findNavController(getView()).navigate(R.id.nav_park_list, bundle);
            return true;
        });
    }

    private class AmenityGridAdapter extends ArrayAdapter<String> {

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
            view.setBackgroundColor(selectedAmenities.contains(amenity) ? 0xFFDDDDDD : 0x0);
            return view;
        }
    }
}