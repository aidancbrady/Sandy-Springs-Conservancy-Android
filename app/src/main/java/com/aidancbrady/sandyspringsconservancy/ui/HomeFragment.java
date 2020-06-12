package com.aidancbrady.sandyspringsconservancy.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.aidancbrady.sandyspringsconservancy.MapFragment;
import com.aidancbrady.sandyspringsconservancy.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        root.findViewById(R.id.map_button)
                .setOnClickListener(v -> open(v, R.id.nav_park_map));
        root.findViewById(R.id.amenity_search_button)
                .setOnClickListener(v -> open(v, R.id.nav_amenity_search));
        return root;
    }

    private void open(View view, int id) {
        Navigation.findNavController(view).navigate(id);
    }
}