package com.aidancbrady.sandyspringsconservancy.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.aidancbrady.sandyspringsconservancy.R;
import com.aidancbrady.sandyspringsconservancy.core.Constants;
import com.aidancbrady.sandyspringsconservancy.core.DataHandler;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private static final int MAX_FAVORITES_HEIGHT = 400;

    private ImageView background;
    private int backgroundIndex = 0;

    private Timer backgroundTimer;

    private ListView favoritesList;
    private FavoritesAdapter favoritesAdapter;

    private boolean favoritesOpen;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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

        background = root.findViewById(R.id.backgroundImage);
        background.setImageBitmap(DataHandler.backgroundList.get(backgroundIndex));
        if (backgroundTimer == null) {
            backgroundTimer = new Timer();
            runBackgroundAnimation();
        }

        favoritesList = root.findViewById(R.id.favoritesList);
        favoritesList.setAdapter(favoritesAdapter = new FavoritesAdapter());
        favoritesList.setOnItemClickListener((parent, view, position, id) -> {
            String parkName = favoritesAdapter.getItem(position);
            Bundle bundle = new Bundle();
            bundle.putString(ParkFragment.PARK_BUNDLE_TAG, parkName);
            Navigation.findNavController(view).navigate(R.id.nav_park, bundle);
        });
        favoritesList.setMinimumHeight(0);
        favoritesOpen = false;
        Button favoritesButton = root.findViewById(R.id.favorites_button);
        favoritesButton.setOnClickListener(v -> {
            runFavoritesAnimation();
        });
        return root;
    }

    private void runFavoritesAnimation() {
        ValueAnimator anim = favoritesOpen ?
                ValueAnimator.ofInt(MAX_FAVORITES_HEIGHT, 0) :
                ValueAnimator.ofInt(0, MAX_FAVORITES_HEIGHT);
        favoritesOpen = !favoritesOpen;
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = favoritesList.getLayoutParams();
            layoutParams.height = val;
            favoritesList.setLayoutParams(layoutParams);
            getView().invalidate();
        });
        anim.setDuration(500);
        anim.start();
    }

    private void runBackgroundAnimation() {
        backgroundTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int prev = backgroundIndex;
                backgroundIndex = (backgroundIndex + 1) % DataHandler.backgroundList.size();
                Drawable curDrawable = new BitmapDrawable(getResources(), DataHandler.backgroundList.get(prev));
                Drawable nextDrawable = new BitmapDrawable(getResources(), DataHandler.backgroundList.get(backgroundIndex));
                Drawable[] arr = new Drawable[] {curDrawable, nextDrawable};
                TransitionDrawable transition = new TransitionDrawable(arr);
                background.setImageDrawable(transition);
                transition.startTransition(500);
            }
        }, 5000, 5000);
    }

    public void onDestroy() {
        super.onDestroy();
        backgroundTimer.cancel();
    }

    private void open(View view, int id) {
        Navigation.findNavController(view).navigate(id);
    }

    private class FavoritesAdapter extends ArrayAdapter<String> {

        public FavoritesAdapter() {
            super(HomeFragment.this.getContext(), R.layout.list_item_park_simple, DataHandler.getFavorites().toArray(new String[0]));
        }

        @NonNull
        @Override
        public View getView(int i, View view, @NonNull ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(getContext()).
                        inflate(R.layout.list_item_park_simple, parent, false);
            }
            String parkName = getItem(i);
            TextView nameText = view.findViewById(R.id.title_text);
            nameText.setText(parkName);
            return view;
        }
    }
}