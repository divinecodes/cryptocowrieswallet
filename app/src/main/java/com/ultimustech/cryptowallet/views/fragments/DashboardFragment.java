package com.ultimustech.cryptowallet.views.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.helpers.MPChartsHelper;
import com.ultimustech.cryptowallet.views.activities.NewTransactionActivity;

import java.io.IOException;

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

        txtPrimaryAccountHash = dashboardView.findViewById(R.id.account_hash);

        btnSend = dashboardView.findViewById(R.id.button_send);

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

}
