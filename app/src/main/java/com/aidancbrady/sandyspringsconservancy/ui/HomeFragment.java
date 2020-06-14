package com.aidancbrady.sandyspringsconservancy.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.aidancbrady.sandyspringsconservancy.R;
import com.aidancbrady.sandyspringsconservancy.core.Constants;
import com.aidancbrady.sandyspringsconservancy.core.DataCache;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private ImageView background;
    private int backgroundIndex = 0;

    private Timer backgroundTimer;

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
        background.setImageBitmap(DataCache.backgroundList.get(0));

        if (backgroundTimer == null) {
            backgroundTimer = new Timer();
            runBackgroundAnimation();
        }

        return root;
    }

    private void runBackgroundAnimation() {
        backgroundTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int prev = backgroundIndex;
                backgroundIndex = (backgroundIndex + 1) % DataCache.backgroundList.size();
                Drawable curDrawable = new BitmapDrawable(getResources(), DataCache.backgroundList.get(prev));
                Drawable nextDrawable = new BitmapDrawable(getResources(), DataCache.backgroundList.get(backgroundIndex));
                Drawable[] arr = new Drawable[] {curDrawable, nextDrawable};
                TransitionDrawable transition = new TransitionDrawable(arr);
                background.setImageDrawable(transition);
                transition.startTransition(500);
            }
        }, 2000, 2000);
    }

    public void onDestroy() {
        super.onDestroy();
        backgroundTimer.cancel();
    }

    private void open(View view, int id) {
        Navigation.findNavController(view).navigate(id);
    }
}