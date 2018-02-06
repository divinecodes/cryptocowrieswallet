package com.ultimustech.cryptowallet.views.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.views.activities.AccountSetupActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private static final String TAG = "Account Fragment";

    private ImageView qrCode;
    private TextView accountCode;
    private TextView accountEmail;
    private TextView revealPassphrase;
    private TextView hidePassphrases;
    private LinearLayout passphrases;
    private Button setup;
    private Button changeDetails;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View accountView = inflater.inflate(R.layout.fragment_account,container, false);

        //get wigets
        qrCode = accountView.findViewById(R.id.account_qr);
        accountCode = accountView.findViewById(R.id.account_hash);
        accountEmail =  accountView.findViewById(R.id.account_email);
        revealPassphrase = accountView.findViewById(R.id.reveal_passphrase);
        hidePassphrases = accountView.findViewById(R.id.hide_passphrases);
        passphrases = accountView.findViewById(R.id.passphrases);
        setup = accountView.findViewById(R.id.button_setup_account);
        changeDetails = accountView.findViewById(R.id.button_change_details);


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

}
