package com.karigar.hubgaclient.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.ui.Callback;

/**
 * Created by Administrator on 5/2/2018.
 */

@SuppressLint("ValidFragment")
public class PaymentFrament extends Fragment {
    ImageView mainImg,menuImg,homeImg,SerImg1,SerImg2,SerImg3,SerImg4;
    TextView text1,text2,text3,text4;
    Callback callback;
    Bundle bundle;
    public PaymentFrament(Callback callback){
        this.callback=callback;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_payment, container, false);

        bundle = this.getArguments();
        homeImg = (ImageView)view.findViewById(R.id.Iv6);
        SerImg1 = (ImageView) view.findViewById(R.id.Iv2);
        SerImg2 = (ImageView) view.findViewById(R.id.Iv3);
        SerImg3 = (ImageView) view.findViewById(R.id.Iv4);
        SerImg4 = (ImageView) view.findViewById(R.id.Iv5);
        text1 = (TextView) view.findViewById(R.id.Vtext);
        text2 = (TextView) view.findViewById(R.id.Vtext1);
        text3 = (TextView) view.findViewById(R.id.Vtext2);
        text4 = (TextView) view.findViewById(R.id.Vtext3);
/*
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this,DrawerActivity.class);
                startActivity(intent);
            }
        });

        homeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Intent intent = new  Intent(PaymentActivity.this,CategoryActivity.class);
                startActivity(intent);*//*
            }
        });*/

        SerImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        SerImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        SerImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        SerImg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               callback.ChangeFragment("BarcodeFragment",bundle);
                /*
                Intent intent = new Intent(PaymentActivity.this,BarcodeActivity.class);
                startActivity(intent);*/
            }
        });

        return view;
    }


}