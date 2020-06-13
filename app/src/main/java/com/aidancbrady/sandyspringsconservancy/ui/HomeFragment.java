package com.aidancbrady.sandyspringsconservancy.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.aidancbrady.sandyspringsconservancy.R;
import com.aidancbrady.sandyspringsconservancy.core.Constants;

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
        root.findViewById(R.id.list_button)
                .setOnClickListener(v -> open(v, R.id.nav_park_list));
        root.findViewById(R.id.logo_image)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(Constants.WEBSITE));
                    startActivity(intent);
                });
        return root;
    }

    private void open(View view, int id) {
        Navigation.findNavController(view).navigate(id);
    }
}