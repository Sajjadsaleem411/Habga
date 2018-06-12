package com.karigar.hubgaclient.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.karigar.hubgaclient.Model.OfferModel;
import com.karigar.hubgaclient.Model.TicketModel;
import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.ui.Adapter.EventAdapter;
import com.karigar.hubgaclient.ui.Adapter.TicketAdapter;
import com.karigar.hubgaclient.ui.Callback;
import com.karigar.hubgaclient.utils.CommonUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Sajjad on 4/25/2018.
 */

@SuppressLint("ValidFragment")
public class TicketFragment extends Fragment {
    RecyclerView recyclerView;
    TicketAdapter mAdapter;

    private List<Integer> dataList = new ArrayList<>();
    List<TicketModel> tickets = new ArrayList<>();
    Callback callback;

    public TicketFragment(Callback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_events);
        mAdapter = new TicketAdapter(tickets,callback);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //prepareData();
        boolean flag=CommonUtils.isNetworkConnected(getActivity());
        if (flag) {
            TicketScript();
        } else {
            Toast.makeText(getActivity(), "Please check your Internet", Toast.LENGTH_SHORT).show();

        }

        return view;
    }

    public void prepareData() {
        dataList.add(01);
        dataList.add(02);
        dataList.add(03);
        dataList.add(04);
        dataList.add(05);
        dataList.add(06);
        dataList.add(07);
        dataList.add(8);
        dataList.add(9);
        dataList.add(10);
        dataList.add(11);
        dataList.add(12);
        dataList.add(13);
        dataList.add(14);
    }

    public void TicketScript() {
        final ProgressDialog progressDialog = CommonUtils.showLoadingDialog(getActivity());
        progressDialog.show();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", 0);
        RequestParams params = new RequestParams();
        /*params.add("category_id", "1");*/
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-access-key", "OFEC-8BC8-12OP-434U");
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.addHeader("x-access-token", sharedPreferences.getString("token",""));
        client.addHeader("locale", "en");
        client.post("http://habga.herokuapp.com/api/ticket/menuTickets", params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e("response UpdateProfile", "start");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject jsonObject = response;
                    int status = jsonObject.getInt("status");
                    if (status == 200) {
                        JSONArray message = jsonObject.getJSONArray("message");
                        for (int i = 0; i < message.length(); i++) {
                            JSONObject data = message.getJSONObject(i);


                            TicketModel ticketModel = new TicketModel();
                            ticketModel.setBookingID(data.getInt("BookingID"));
                            ticketModel.setTitle(data.getString("Title"));
                            tickets.add(ticketModel);

               /*           *//*  ticketModel.setBookingID(data.getInt("ServiceID"));
                            ticketModel.setCatID(data.getInt("CatID"));*//*
                            JSONObject ticket_image = data.getJSONObject("ticket_image");

                            ticketModel.setType(ticket_image.getString("type"));
                            JSONArray numbers = ticket_image.getJSONArray("data");
                            ticketModel.data = new ArrayList<>();
          *//*                  for (int j = 0; j < numbers.length(); j++) {
                                ticketModel.data.add(numbers.getInt(j));
                            }
*/
                        }

                        progressDialog.dismiss();
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });

    }

}