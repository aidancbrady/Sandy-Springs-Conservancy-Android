package com.aidancbrady.sandyspringsconservancy.ui;

import android.app.Activity;
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

import com.aidancbrady.sandyspringsconservancy.R;
import com.aidancbrady.sandyspringsconservancy.core.DataCache;
import com.aidancbrady.sandyspringsconservancy.core.Park;
import com.aidancbrady.sandyspringsconservancy.core.Utilities;

import java.util.ArrayList;

public class ParkListFragment extends ListFragment {

    private ParkListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(listAdapter = new ParkListAdapter(getContext()));
        listAdapter.sort((c1, c2) ->
                Float.compare(Utilities.distance(DataCache.lastLocation, c1.getLatLng()),
                              Utilities.distance(DataCache.lastLocation, c2.getLatLng())));
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        view.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((Activity) getContext()).getCurrentFocus().getWindowToken(), 0);
            }
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

    private class ParkListAdapter extends ArrayAdapter<Park> {

        public ParkListAdapter(Context context) {
            super(context, R.layout.list_item_park, new ArrayList<>(DataCache.parkList));
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
            System.out.println(park.getLatLng() + " " + DataCache.lastLocation);
            double dist = Math.round(Utilities.distanceInMiles(park.getLatLng(), DataCache.lastLocation));
            dist = Math.round(dist * 100) / 100D;
            distanceText.setText(getString(R.string.park_distance_miles, dist));
            if (park.getImages().size() > 0) {
                imageView.setImageBitmap(park.getImages().get(0));
            }
            return view;
        }
    }
}
