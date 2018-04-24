package com.ultimustech.cryptowallet.controllers.helpers;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ultimustech.cryptowallet.R;

import java.util.ArrayList;

/**
 * Created by Poacher on 3/11/2018.
 */

public class MPChartsHelper {

    /**
     * function to initialize line chart
     * @param lineChart
     * @param title
     * @param description
     */
    public static void lineChartHelper(LineChart lineChart,  String title, Description description){
        ArrayList<Entry> entries  = new ArrayList<Entry>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(2f, 1));
        entries.add(new Entry(7f, 2));
        entries.add(new Entry(5f, 3));

        LineDataSet dataset = new LineDataSet(entries,title);
        dataset.setDrawFilled(true);
        dataset.setColors(ColorTemplate.MATERIAL_COLORS);
        LineData data = new LineData(dataset);

        lineChart.setData(data);
        lineChart.setDescription(description);
        lineChart.animate();
    }

    public static void pieChartHelper(PieChart pieChart, int color){


        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(true);
        Description description  = new Description();
        description.setText("Wallet Overview");
        description.setTextSize(15);
        description.setTextColor(color);
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
        pieData.setValueTextColor(color);

        pieChart.setData(pieData);
    }

}
