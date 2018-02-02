package com.ultimustech.cryptowallet.views.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.views.activities.NewTransactionActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private static final String TAG = "Dashboard Fragment";

    private TextView txtAccountBalance;
    private TextView txtCCWExchangeRate;
    private TextView txtOtherCurrenyExchangeRate;
    private Spinner spinnerOtherCurrencyShorthand;
    private TextView txtPrimaryAccountHash;
    private Button btnReceive;
    private Button btnSend;
    private TextView txtTransactionType;
    private TextView txtTransactionAccount;
    private TextView txtTransactionAmount;
    private TextView txtTransactionDate;
    private Spinner spinnerExchangeDuration;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setup view
        final View dashboardView = inflater.inflate(R.layout.fragment_dashboard,container,false);
        // find widgets
        txtAccountBalance = dashboardView.findViewById(R.id.account_balance);
        txtCCWExchangeRate = dashboardView.findViewById(R.id.ccw_exchange_rate);
        txtOtherCurrenyExchangeRate = dashboardView.findViewById(R.id.other_exchange_balance);
        spinnerOtherCurrencyShorthand = dashboardView.findViewById(R.id.other_currency_shorthand);
        txtPrimaryAccountHash = dashboardView.findViewById(R.id.account_hash);
        btnReceive = dashboardView.findViewById(R.id.button_receive);
        btnSend = dashboardView.findViewById(R.id.button_send);
        txtTransactionType = dashboardView.findViewById(R.id.transaction_type);
        txtTransactionAccount = dashboardView.findViewById(R.id.transaction_account_hash);
        txtTransactionAmount = dashboardView.findViewById(R.id.transaction_account);
        txtTransactionDate = dashboardView.findViewById(R.id.transaction_date);
        spinnerExchangeDuration  = dashboardView.findViewById(R.id.exchange_rate_duration);

        //create adapters for spinners
        final ArrayAdapter<CharSequence> shorthandAdapter = ArrayAdapter.createFromResource(dashboardView.getContext(),
                R.array.currencies,android.R.layout.simple_spinner_dropdown_item);
        shorthandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(dashboardView.getContext(),
                R.array.duration,android.R.layout.simple_spinner_dropdown_item);
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set spinner adapters
        spinnerOtherCurrencyShorthand.setAdapter(shorthandAdapter);
        spinnerExchangeDuration.setAdapter(durationAdapter);

        //set onclick listeners
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(dashboardView.getContext(), NewTransactionActivity.class);
                dashboardView.getContext().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_top_in,R.anim.push_top_out);
            }
        });

        // Inflate the layout for this fragment
        return dashboardView;
    }

}
