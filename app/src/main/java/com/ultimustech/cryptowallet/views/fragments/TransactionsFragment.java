package com.ultimustech.cryptowallet.views.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.adapters.TransactionsAdapter;
import com.ultimustech.cryptowallet.models.Account;
import com.ultimustech.cryptowallet.models.Transaction;
import com.ultimustech.cryptowallet.views.activities.LoginActivity;
import com.ultimustech.cryptowallet.views.activities.NewTransactionActivity;

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
    private FloatingActionButton newTransaction;
    private TextView loadingText;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mTransactionRef;
    private DatabaseReference mAccountRef;
    private FirebaseDatabase firebaseDatabase;
    private ValueEventListener accountListener;
    private ChildEventListener mChildEventListener;

    private Account account;
    private String address;

    public TransactionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View transactionView = inflater.inflate(R.layout.fragment_transactions,container, false);
        progressSpinner = transactionView.findViewById(R.id.transactionLoading);
        loadingText = transactionView.findViewById(R.id.loadingText);

        newTransaction = transactionView.findViewById(R.id.addTransaction);
        progressSpinner.setVisibility(View.VISIBLE);
        loadingText.setVisibility(View.VISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mAccountRef = FirebaseDatabase.getInstance().getReference().child("accounts").child(firebaseUser.getUid());
        mTransactionRef = FirebaseDatabase.getInstance().getReference("transactions");
        //link recyclerview to transactions adapter
        recyclerView = transactionView.findViewById(R.id.transaction_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //array list


        final ArrayList<Transaction> transactions = new ArrayList<>();

        //get user id
        if( firebaseUser == null){
            Toast.makeText(transactionView.getContext(), "User not registered",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(transactionView.getContext(), LoginActivity.class);
            transactionView.getContext().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.push_top_in,R.anim.push_top_out);
        }else {
            ValueEventListener accountsValue = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Account tempAccount = dataSnapshot.getValue(Account.class);
                    if (tempAccount != null) {
                        account = tempAccount;
                        address = account.accountHash;

                        mChildEventListener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Transaction transaction = dataSnapshot.getValue(Transaction.class);
                                transactions.add(transaction);
                                //dummy set adapter
                                transactionsAdapter = new TransactionsAdapter(transactionView.getContext(),transactions);
                                recyclerView.setAdapter(transactionsAdapter);
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                transactionsAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };

                        mTransactionRef.child(address).addChildEventListener(mChildEventListener);
                        loadingText.setVisibility(View.GONE);
                        progressSpinner.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };

            mAccountRef.addValueEventListener(accountsValue);

            accountListener = accountsValue;


        }
//


        //set on click listener for fab
        newTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(transactionView.getContext(), NewTransactionActivity.class);
                transactionView.getContext().startActivity(intent);
            }
        });

        return transactionView;
    }

    @Override
        public void onStart(){

            super.onStart();
            //attach a listener to read the data
            ValueEventListener accountsValue = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Account tempAccount = dataSnapshot.getValue(Account.class);
                    if (tempAccount != null) {
                        account = tempAccount;
                        address = account.accountHash;
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };

            mAccountRef.addValueEventListener(accountsValue);

            accountListener = accountsValue;
        }
}
