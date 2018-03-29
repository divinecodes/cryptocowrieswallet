package com.ultimustech.cryptowallet.views.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ultimustech.cryptowallet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeFragment extends Fragment {
    private static final String TAG = "Mine Fragment";


    public ExchangeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get view
        final View mineView = inflater.inflate(R.layout.fragment_exchange,container, false);

        // Inflate the layout for this fragment
        return mineView;
    }

}
