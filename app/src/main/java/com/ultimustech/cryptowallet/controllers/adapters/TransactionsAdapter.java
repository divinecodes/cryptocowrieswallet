package com.ultimustech.cryptowallet.controllers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.models.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Poacher on 1/31/2018.
 */

public class TransactionsAdapter extends  RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    private ArrayList<Transaction> transactions;
    private Context context;
    public TransactionsAdapter(Context context, ArrayList<Transaction>transactions){
        this.transactions = transactions;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_child,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        //data would be replaced with data from firebase
        Transaction transaction = transactions.get(position);

        holder.transactionType.setText(transaction.type);
        holder.transactionAccount.setText(transaction.account);
        holder.transactionAmount.setText(String.format("%s CCW",transaction.amount));
        holder.transactionDate.setText(new Date(transaction.transactionDate).toString());
    }


    @Override
    public int getItemCount(){
        //return the size of the list
        return transactions.size();
    }
    //Inner Viewholder class to hold views
    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView transactionType = null;
        TextView  transactionTypeLabel = null;
        TextView  transactionAccount = null;
        TextView transactionDate  = null;
        TextView transactionAmount = null;
        LinearLayout transactionLayout = null;

        public ViewHolder (final View itemView){
            super(itemView);

            //casting views to various ids
            transactionTypeLabel = itemView.findViewById(R.id.transaction_account);
            transactionAccount = itemView.findViewById(R.id.transaction_account_hash);
            transactionAmount = itemView.findViewById(R.id.transaction_amount);
            transactionType = itemView.findViewById(R.id.transaction_type);
            transactionDate = itemView.findViewById(R.id.transaction_date);
            transactionLayout = itemView.findViewById(R.id.transactionLayout);

        }
    }
}
