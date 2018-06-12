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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<TicketModel> ticketList;

    Callback callback;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public FrameLayout layout;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_event_title);
            layout=(FrameLayout)view.findViewById(R.id.layout_event_item);
        }
    }


    public EventAdapter(List<TicketModel> moviesList, Callback callback) {
        this.ticketList = moviesList;
        this.callback=callback;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
    //    Movie movie = moviesList.get(position);
 //       holder.title.setText("Ticket "+position);

        holder.title.setText("Ticket "+ticketList.get(position).getTicketID());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("ticket_id",ticketList.get(position).getTicketID());

                callback.ChangeFragment("BarcodeFragment",bundle);

                /*context.startActivity(new Intent(context, CruiseActivity.class)
                                    .putExtra("service_id",offerModel.getId()));*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }
}
