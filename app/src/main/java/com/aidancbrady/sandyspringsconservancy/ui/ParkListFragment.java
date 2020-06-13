package com.aidancbrady.sandyspringsconservancy.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;

import com.aidancbrady.sandyspringsconservancy.R;
import com.aidancbrady.sandyspringsconservancy.core.DataCache;
import com.aidancbrady.sandyspringsconservancy.core.Park;

import java.util.ArrayList;
import java.util.List;

public class ParkListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ParkListAdapter());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_park_list, container, false);
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {
        Toast.makeText(getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();
    }

    private class ParkListAdapter extends BaseAdapter {

        private List<Park> list = new ArrayList<>(DataCache.parkList);

        @Override
        public int getCount() {
            System.out.println(list.size());
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(getContext()).
                        inflate(R.layout.item_park, parent, false);
            }

            Park park = list.get(i);
            TextView nameText = view.findViewById(R.id.title_text);
            TextView phoneText = view.findViewById(R.id.phone_text);
            TextView distanceText = view.findViewById(R.id.distance_text);

            nameText.setText(park.getName());
            phoneText.setText(park.getPhoneNumber());
            distanceText.setText("TODO fix distance");
            return view;
        }
    }
}
