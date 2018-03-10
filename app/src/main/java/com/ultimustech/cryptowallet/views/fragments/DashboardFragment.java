package com.ultimustech.cryptowallet.views.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.views.activities.NewTransactionActivity;

import java.util.ArrayList;

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
        btnReceive = dashboardView.findViewById(R.id.button_receive);
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


        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(true);
        Description description  = new Description();
        description.setText("Wallet Overview");
        description.setTextSize(15);
        description.setTextColor(dashboardView.getResources().getColor(R.color.colorPrimaryDark));
        pieChart.setDescription(description);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);


        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(30f, "Spent"));
        yValues.add(new PieEntry(40f,"Received"));
        yValues.add(new PieEntry(30f, "Balance"));

        PieDataSet dataSet = new PieDataSet(yValues, "Balances");
        dataSet.setSliceSpace(8f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData((dataSet));
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(dashboardView.getResources().getColor(R.color.colorPrimary));

        pieChart.setData(pieData);
        // Inflate the layout for this fragment
        return dashboardView;
    }

}
