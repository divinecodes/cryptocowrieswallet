package com.ultimustech.cryptowallet.views.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.models.Account;
import com.ultimustech.cryptowallet.views.activities.AccountSetupActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private static final String TAG = "Account Fragment";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ValueEventListener accountListener;
    private DatabaseReference accountRef;

    private ImageView qrCode;
    private TextView accountCode;
    private TextView accountEmail;
    private TextView revealPassphrase;
    private TextView hidePassphrases;
    private LinearLayout passphrases;
    private Button setup;
    private Button changeDetails;
    private Button passphrase1;
    private Button passphrase2;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View accountView = inflater.inflate(R.layout.fragment_account,container, false);

        //initialize firebase
        firebaseAuth  = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        accountRef = FirebaseDatabase.getInstance().getReference().child("accounts").child(firebaseUser.getUid());

        //get wigets
        qrCode = accountView.findViewById(R.id.account_qr);
        accountCode = accountView.findViewById(R.id.account_hash);
        accountEmail =  accountView.findViewById(R.id.account_email);
        revealPassphrase = accountView.findViewById(R.id.reveal_passphrase);
        hidePassphrases = accountView.findViewById(R.id.hide_passphrases);
        passphrases = accountView.findViewById(R.id.passphrases);
        setup = accountView.findViewById(R.id.button_setup_account);
        changeDetails = accountView.findViewById(R.id.button_change_details);
        passphrase1 = accountView.findViewById(R.id.passphrase_1);
        passphrase2 = accountView.findViewById(R.id.passphrase_2);

        //set on click listener for revealing passphrases
        revealPassphrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealPassphrase.setVisibility(View.GONE);
                hidePassphrases.setVisibility(View.VISIBLE);
                passphrases.setVisibility(View.VISIBLE);
            }
        });

        //hide passphrases
        hidePassphrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealPassphrase.setVisibility(View.VISIBLE);
                hidePassphrases.setVisibility(View.GONE);
                passphrases.setVisibility(View.GONE);
            }
        });

        //setup account
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(accountView.getContext(), AccountSetupActivity.class);
                accountView.getContext().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

            }
        });
        return accountView;
    }

    @Override
    public void onStart(){
        super.onStart();

        ValueEventListener accountEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getAccount Details
                Account account = dataSnapshot.getValue(Account.class);

                if(account != null){
                    setup.setVisibility(View.GONE);
                    passphrase1.setText(account.passphrase1);
                    passphrase2.setText(account.passphrase2);
                    accountCode.setText(account.accountHash);
                    accountEmail.setText(firebaseUser.getEmail());
                } else {
                    changeDetails.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG,"loadingAccount Details Cancelled", databaseError.toException());
            }
        };
        accountRef.addValueEventListener(accountEventListener);

        accountListener = accountEventListener;
    }

    public void makeToast(String message){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(getContext(), message,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        //Remove the value event listener
        if(accountListener != null){
            accountRef.removeEventListener(accountListener);
        }
    }


}
