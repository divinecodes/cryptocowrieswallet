package com.ultimustech.cryptowallet.controllers.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.views.activities.BuyCoinsActivity;

import java.util.ArrayList;

/**
 * Created by Poacher on 3/29/2018.
 */

public class ExchangesAdapter extends RecyclerView.Adapter<ExchangesAdapter.ViewHolder> {
    private ArrayList<String> publicAddressList;

    public ExchangesAdapter(ArrayList<String> publicAddressList){
        this.publicAddressList = publicAddressList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new ViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.exchanges_child, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        //TODO: use dummy data for now, later pull data from firebase

        holder.coinsForSale.setText("130 CCW");
        String [] hashes = holder.userPublicAddress.getResources().getStringArray(R.array.sampleHash);

        holder.userPublicAddress.setText(hashes[position]);

    }

    @Override
    public int getItemCount(){
        return publicAddressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userPublicAddress = null;
        TextView coinsForSale = null;
        Button btnBuyCoins = null;

        public ViewHolder(final View itemView){
            super(itemView);

            userPublicAddress = itemView.findViewById(R.id.user_public_address);
            coinsForSale = itemView.findViewById(R.id.coins_for_sale);
            btnBuyCoins = itemView.findViewById(R.id.buy_ccw);

            //TODO: add intent to start buy action here
            //TODO: start activity to buy ccw.
            btnBuyCoins.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), BuyCoinsActivity.class);
                    i.putExtra("sellerAddress",userPublicAddress.getText());
                    i.putExtra("coinsForSale", coinsForSale.getText());
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}
