package com.ultimustech.cryptowallet.views.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ultimustech.cryptowallet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View accountView = inflater.inflate(R.layout.fragment_account,container, false);

        // Inflate the layout for this fragment
        return accountView;
    }

}
