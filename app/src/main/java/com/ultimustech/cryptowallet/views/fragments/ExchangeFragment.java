package com.ultimustech.cryptowallet.views.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.adapters.ExchangesAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeFragment extends Fragment {
    private static final String TAG = "Mine Fragment";
    private RecyclerView recyclerView;

    public ExchangeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get view
        View exchangesView = inflater.inflate(R.layout.fragment_exchange,container, false);
//        recyclerView = exchangesView.findViewById(R.id.exchangesList);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        //todo: get the list from firebase and add to the arraylist
//        ArrayList<String>  hashes = new ArrayList<>();
//        String[] hash = getResources().getStringArray(R.array.sampleHash);
//        for(int i = 0; i < hash.length; i++) hashes.add(hash[i]);
//
//        recyclerView.setAdapter(new ExchangesAdapter(hashes));
//        // Inflate the layout for this fragment
        return exchangesView;
    }

}
