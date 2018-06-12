package com.karigar.hubgaclient.ui.Adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.karigar.hubgaclient.Model.TicketModel;
import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.ui.Callback;

import java.util.List;

/**
 * Created by Muhammad Sajjad on 5/5/2018.
 */

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    private List<TicketModel> tickets;
    private Callback callback;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public FrameLayout layout;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_ticket_title);
            layout=(FrameLayout)view.findViewById(R.id.layout_ticket_item);
        }
    }


    public TicketAdapter(List<TicketModel> moviesList, Callback callback) {
        this.tickets = moviesList;
        this.callback=callback;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
    //    Movie movie = moviesList.get(position);
        holder.title.setText(tickets.get(position).getTitle());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("booking_id",tickets.get(position).getBookingID());
                bundle.putString("title",tickets.get(position).getTitle());
                callback.ChangeFragment("EventFragment",bundle);

                /*context.startActivity(new Intent(context, CruiseActivity.class)
                                    .putExtra("service_id",offerModel.getId()));*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }
}
