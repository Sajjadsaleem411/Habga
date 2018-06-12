package com.karigar.hubgaclient.ui.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karigar.hubgaclient.Model.OfferModel;
import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.ui.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {
    List<OfferModel> serviceList;
    Context context;
    Callback callback;
    public OfferAdapter(Context context, List<OfferModel> serviceList, Callback callback) {
        this.context = context;
        this.serviceList = serviceList;
        this.callback=callback;
    }

    private final String Tag = "RecyclerViewAdapter";

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_offer, parent, false);
        OfferViewHolder holderView = new OfferViewHolder(view);

        return holderView;
    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        final OfferModel offerModel = serviceList.get(position);
        holder.Vtext1.setText(offerModel.getTitle());
        holder.Vtext2.setText(offerModel.getDescription());
        if(offerModel.getImage().equals("")){

        }else {
            Picasso.with(context).load(offerModel.getImage()).into(holder.Iv4);
        }
        holder.Rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("service_id",offerModel.getId());
                bundle.putInt("discount",0);
                callback.ChangeFragment("CruiseFragment",bundle);

                /*context.startActivity(new Intent(context, CruiseActivity.class)
                                    .putExtra("service_id",offerModel.getId()));*/
            }
        });

    }

    @Override
    public int getItemCount() {

        return serviceList.size();
    }


    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        ImageView Iv4;
        TextView Vtext1,Vtext2,Vtext3;
        RelativeLayout Rl;

        OfferViewHolder(View itemView) {

            super(itemView);
            Iv4 = (ImageView) itemView.findViewById(R.id.Iv4);
            Vtext1 = (TextView) itemView.findViewById(R.id.Vtext1);
            Vtext2 = (TextView) itemView.findViewById(R.id.Vtext2);/*
            Vtext3 = (TextView) itemView.findViewById(R.id.Vtext3);*/
            Rl = (RelativeLayout) itemView.findViewById(R.id.Rl);
        }
    }
}
