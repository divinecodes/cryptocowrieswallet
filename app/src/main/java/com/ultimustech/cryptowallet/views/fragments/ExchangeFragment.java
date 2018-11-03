package com.ultimustech.cryptowallet.views.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.adapters.ExchangesAdapter;
import com.ultimustech.cryptowallet.controllers.helpers.MPChartsHelper;
import com.ultimustech.cryptowallet.controllers.helpers.Validation;
import com.ultimustech.cryptowallet.models.Coinbase;
import com.ultimustech.cryptowallet.views.activities.BuyCoinsActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeFragment extends Fragment {
    private static final String TAG = "Mine Fragment";

    private TextView shoreBase;
    private EditText inputAmnt;
    private Button buyCCW;

    private LineChart priceRate;

    public ExchangeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get view
        final View exchangesView = inflater.inflate(R.layout.fragment_exchange,container, false);

        //find views
        priceRate = exchangesView.findViewById(R.id.exchange_rate_chat);
        shoreBase = exchangesView.findViewById(R.id.cowries_base);
        inputAmnt = exchangesView.findViewById(R.id.cowries_to_buy);
        buyCCW = exchangesView.findViewById(R.id.buy_cowries);

        //set data for the  the exchanges rate
        ArrayList<Entry> entries  = new ArrayList<Entry>();
        entries.add(new Entry(1f, 0));
        entries.add(new Entry(2f, 4));
        entries.add(new Entry(3f, 2));
        entries.add(new Entry(5f, 3));
        entries.add(new Entry(7f, 1));


        shoreBase.setText(Coinbase.COINBASE_BALANCE + " CCW");

        Description ccwDesc = new Description();
        ccwDesc.setText("CCW Price");
        ccwDesc.setTextSize(15);
        ccwDesc.setTextColor(exchangesView.getResources().getColor(R.color.colorPrimary));
        MPChartsHelper.lineChartHelper(priceRate,"CCW Price Rate",ccwDesc,entries);

        /**
         * validate the amount
         */


        buyCCW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amntStr =  inputAmnt.getText().toString();
                double amount;
                if(!Validation.validNumber(amntStr)){
                    Snackbar.make(exchangesView, "Please Enter A Valid amount", Snackbar.LENGTH_LONG).show();
                } else {
                    amount = Double.parseDouble(amntStr);
                    // people are limited to a single a limit of currency they can buy
                    if(amount >= 1000){
                        Snackbar.make(exchangesView, "Amount To Buy Exceed 1000CCW",Snackbar.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(exchangesView.getContext(), BuyCoinsActivity.class);
                        intent.putExtra("amount",amount);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_top_in,R.anim.push_top_out);
                    }
                }
            }
        });


        return exchangesView;
    }

}
