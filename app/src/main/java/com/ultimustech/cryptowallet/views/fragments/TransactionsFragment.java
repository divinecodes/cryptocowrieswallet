package com.ultimustech.cryptowallet.views.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.adapters.TransactionsAdapter;
import com.ultimustech.cryptowallet.models.Transaction;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends Fragment {
    private final String TAG = "MENU FRAGMENT";

    private RecyclerView recyclerView;
    private TransactionsAdapter transactionsAdapter;
    private ProgressBar progressSpinner;
    public TransactionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View transactionView = inflater.inflate(R.layout.fragment_transactions,container, false);
        progressSpinner = transactionView.findViewById(R.id.transactionLoading);
        progressSpinner.setVisibility(View.VISIBLE);

        //link recyclerview to transactions adapter
        recyclerView = transactionView.findViewById(R.id.transaction_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //array list
        final ArrayList<Transaction> transactions = new ArrayList<>();
        //dummy set adapter
        transactionsAdapter = new TransactionsAdapter(transactionView.getContext(),transactions );
        recyclerView.setAdapter(transactionsAdapter);
        return transactionView;
    }

}
