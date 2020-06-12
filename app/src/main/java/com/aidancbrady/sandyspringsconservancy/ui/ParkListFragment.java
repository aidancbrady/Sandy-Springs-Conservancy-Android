package com.aidancbrady.sandyspringsconservancy.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;

import com.aidancbrady.sandyspringsconservancy.R;

public class ParkListFragment extends ListFragment {

    private String[] dataArray = new String[] { "One", "Two", "Three", };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       ListAdapter listAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, dataArray);
        setListAdapter(listAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater
                .inflate(R.layout.fragment_park_list, container, false);
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {

        Toast.makeText(getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();
    }
}
