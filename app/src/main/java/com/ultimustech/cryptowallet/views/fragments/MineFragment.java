package com.ultimustech.cryptowallet.views.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ultimustech.cryptowallet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {
    private static final String TAG = "Mine Fragment";

    private EditText edtMineAccountHash;
    private Button scanQR;
    private Button btnMine;
    private Button btnStop;
    private Button btnFreeCCW;
    private Button btnWithdraw;
    private ImageView mineAnimation;

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get view
        final View mineView = inflater.inflate(R.layout.fragment_mine,container, false);

        //get widgets
        edtMineAccountHash = mineView.findViewById(R.id.mining_account_hash);
        btnMine = mineView.findViewById(R.id.start_mining);
        btnStop = mineView.findViewById(R.id.stop_mining);
        scanQR = mineView.findViewById(R.id.scan_qr);
        mineAnimation = mineView.findViewById(R.id.mining_anim);
        btnFreeCCW = mineView.findViewById(R.id.free_mining);
        btnWithdraw = mineView.findViewById(R.id.withdraw);

        //sample set the value for the kid
        edtMineAccountHash.setText(getResources().getString(R.string.sample_account_hash));

        //disable buttons
        btnStop.setVisibility(View.GONE);
        btnWithdraw.setVisibility(View.GONE);
        mineAnimation.setVisibility(View.GONE);


        //set onclick listener for  buttons
        btnMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMine.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);
                btnWithdraw.setVisibility(View.VISIBLE);
                Glide.with(mineView.getContext()).load(R.drawable.mining).into(mineAnimation); //load animation
                mineAnimation.setVisibility(View.VISIBLE);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStop.setVisibility(View.GONE);
                btnMine.setVisibility(View.VISIBLE);
                btnFreeCCW.setVisibility(View.VISIBLE);
                mineAnimation.setVisibility(View.GONE);
            }
        });

        // Inflate the layout for this fragment
        return mineView;
    }

}
