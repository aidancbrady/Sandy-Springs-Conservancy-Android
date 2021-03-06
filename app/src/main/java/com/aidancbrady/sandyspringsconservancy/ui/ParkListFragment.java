package com.aidancbrady.sandyspringsconservancy.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;

import com.aidancbrady.sandyspringsconservancy.MenuActivity;
import com.aidancbrady.sandyspringsconservancy.R;
import com.aidancbrady.sandyspringsconservancy.core.DataHandler;
import com.aidancbrady.sandyspringsconservancy.core.Park;
import com.aidancbrady.sandyspringsconservancy.core.Utilities;

import java.util.ArrayList;
import java.util.List;

public class ParkListFragment extends ListFragment {

    public static final String AMENITIES_BUNDLE_TAG = "amenities";

    private ParkListAdapter listAdapter;

    @SuppressLint("ClickableViewAccessibility")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Park> parks = new ArrayList<>(DataHandler.parkList);
        if (getArguments() != null) {
            List<String> amenities = getArguments().getStringArrayList(AMENITIES_BUNDLE_TAG);
            if (amenities != null) {
                parks = DataHandler.getParksWithAmenities(amenities);
            }
        }

        setListAdapter(listAdapter = new ParkListAdapter(getContext(), parks));
        listAdapter.sort((c1, c2) ->
                Float.compare(Utilities.distance(DataHandler.lastLocation, c1.getLatLng()),
                        Utilities.distance(DataHandler.lastLocation, c2.getLatLng())));

        View  emptyView = getActivity().getLayoutInflater().inflate(R.layout.list_empty_view, null);

        ((ViewGroup)getListView().getParent()).addView(emptyView);
        getListView().setEmptyView(emptyView);

        EditText searchField = getView().findViewById(R.id.inputSearch);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
        getListView().setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
            return false;
        });
        searchField.setOnFocusChangeListener((v, hasFocus) -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_park_list, container, false);
    }

    @Override
    public void onListItemClick(@NonNull ListView list, @NonNull View v, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString(ParkFragment.PARK_BUNDLE_TAG, listAdapter.getItem(position).getName());
        Navigation.findNavController(v).navigate(R.id.nav_park, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MenuActivity) getActivity()).setMenuState(false);
    }

    private class ParkListAdapter extends ArrayAdapter<Park> {

        public ParkListAdapter(Context context, List<Park> parks) {
            super(context, R.layout.list_item_park, parks);
        }

        @NonNull
        @Override
        public View getView(int i, View view, @NonNull ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(getContext()).
                        inflate(R.layout.list_item_park, parent, false);
            }
            Park park = getItem(i);
            TextView nameText = view.findViewById(R.id.title_text);
            TextView phoneText = view.findViewById(R.id.phone_text);
            TextView distanceText = view.findViewById(R.id.distance_text);
            ImageView imageView = view.findViewById(R.id.image_view);

            nameText.setText(park.getName());
            phoneText.setText(park.getPhoneNumber());
            double dist = Math.round(Utilities.distanceInMiles(park.getLatLng(), DataHandler.lastLocation));
            dist = Math.round(dist * 100) / 100D;
            distanceText.setText(getString(R.string.park_distance_miles, dist));
            if (park.getImages().size() > 0) {
                imageView.setImageBitmap(park.getImages().get(0));
            }
            return view;
        }
    }
}
