package com.ultimustech.cryptowallet.views.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.adapters.TransactionsAdapter;
import com.ultimustech.cryptowallet.models.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
//        progressSpinner.setVisibility(View.VISIBLE);

        //link recyclerview to transactions adapter
        recyclerView = transactionView.findViewById(R.id.transaction_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //array list
        Transaction transaction1 = new Transaction("SEND", "1cfb127af5310dbde5100cc50db5ae389c5aa62d",
                "ADAFIONDAF",58.00f,new Date().getTime());
        Transaction transaction2 = new Transaction("BUY", "2cfb127af5310dbde5100cc50db5ae304c5sd62d","ADFAIONV",2.0f,
                new Date().getTime());

        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(new Transaction("SEND", "8cfb127af5310dbde5100cc50db5ae304c5sd62d","ADFAIONV",8.0f,
                new Date().getTime()));
        Toast.makeText(transactionView.getContext(), "SIZE OF ARRAYLIST: "+transactions.size(), Toast.LENGTH_LONG).show();
        //dummy set adapter
        transactionsAdapter = new TransactionsAdapter(transactionView.getContext(),transactions);
        recyclerView.setAdapter(transactionsAdapter);

        return transactionView;
    }
}
