package com.ultimustech.cryptowallet.views.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.database.FirebaseDBController;
import com.ultimustech.cryptowallet.controllers.database.FirebaseDBHelper;
import com.ultimustech.cryptowallet.controllers.helpers.DefaultHelpers;
import com.ultimustech.cryptowallet.controllers.helpers.MPChartsHelper;
import com.ultimustech.cryptowallet.models.Account;
import com.ultimustech.cryptowallet.views.activities.AccountSetupActivity;
import com.ultimustech.cryptowallet.views.activities.LoginActivity;
import com.ultimustech.cryptowallet.views.activities.NewTransactionActivity;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private static final String TAG = "Dashboard Fragment";

    private FirebaseDBHelper firebaseDBHelper = new FirebaseDBHelper();
    private DefaultHelpers defaultHelpers = new DefaultHelpers();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference accountRef;
    private ValueEventListener accountListener;

    private TextView txtAccountBalance;
    private TextView txtCCWExchangeRate;
    private TextView txtOtherCurrenyExchangeRate;
    private TextView txtPrimaryAccountHash;
    private Button btnReceive;
    private Button btnSend;
    private TextView txtTransactionType;
    private TextView txtTransactionAccount;
    private TextView txtTransactionAmount;
    private TextView txtTransactionDate;
    private LinearLayout dashboardLayout;


    private PieChart pieChart;
    private LineChart bitcoinChart;
    private LineChart etherChart;
    private LineChart litecoinChart;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setup view
        final View dashboardView = inflater.inflate(R.layout.fragment_dashboard,container,false);

        //check if there is internet connection
        if(!defaultHelpers.isOnline(dashboardView.getContext())){
            Snackbar.make(dashboardView,"No Internet Connection",Snackbar.LENGTH_LONG).show();
        }
        //initialize firebase authentication instance and get the currenct user
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        accountRef = FirebaseDatabase.getInstance().getReference().child("accounts").child(firebaseUser.getUid());

        txtPrimaryAccountHash = dashboardView.findViewById(R.id.account_hash);
        txtAccountBalance = dashboardView.findViewById(R.id.account_balance);

        btnSend = dashboardView.findViewById(R.id.button_send);
        dashboardLayout = dashboardView.findViewById(R.id.dashboardLayout);

        //set onclick listeners
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(dashboardView.getContext(), NewTransactionActivity.class);
                dashboardView.getContext().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_top_in,R.anim.push_top_out);
            }
        });

        pieChart = dashboardView.findViewById(R.id.balancesChart);
        bitcoinChart = dashboardView.findViewById(R.id.bitcoinChart);
        etherChart = dashboardView.findViewById(R.id.etherChart);
        litecoinChart = dashboardView.findViewById(R.id.litecoinChart);

        MPChartsHelper.pieChartHelper(pieChart, dashboardView.getResources().getColor(R.color.colorPrimaryDark));

        //set data for bitcoin chart
        Description bitcoinDesc  = new Description();
        bitcoinDesc.setText("Bitcoin Price");
        bitcoinDesc.setTextSize(15);
        bitcoinDesc.setTextColor(dashboardView.getResources().getColor(R.color.colorPrimaryDark));
        MPChartsHelper.lineChartHelper(bitcoinChart, "Bitcoin ", bitcoinDesc);

        //set up data for  ethereum
        Description etherDesc = new Description();
        etherDesc.setText("Ether Price");
        etherDesc.setTextSize(15);
        etherDesc.setTextColor(dashboardView.getResources().getColor(R.color.colorAccent));
        MPChartsHelper.lineChartHelper(etherChart, "Ethereum", etherDesc);

        //set up data for litecoin
        Description liteDesc = new Description();
        liteDesc.setText("Litecoin Price ");
        liteDesc.setTextColor(dashboardView.getResources().getColor(R.color.colorPrimary));
        MPChartsHelper.lineChartHelper(litecoinChart, "LiteCoin ", liteDesc);


        // Inflate the layout for this fragment
        return dashboardView;
    }

    @Override
    public void onStart(){
        super.onStart();

        ValueEventListener accountEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get account details
                Account account = dataSnapshot.getValue(Account.class);

                if(account != null){
                    txtPrimaryAccountHash.setText(account.accountHash);
                    String strBalance =  String.format("%.7f CCW",account.balance);
                    txtAccountBalance.setText(strBalance);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "Loading Details Error: "+databaseError.getCode());
            }
        };

        accountRef.addValueEventListener(accountEventListener);

        accountListener = accountEventListener;
    }

}
