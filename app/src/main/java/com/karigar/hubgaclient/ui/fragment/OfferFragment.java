package com.karigar.hubgaclient.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.karigar.hubgaclient.Model.Ads;
import com.karigar.hubgaclient.Model.OfferModel;
import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.ui.Adapter.OfferAdapter;
import com.karigar.hubgaclient.ui.Callback;
import com.karigar.hubgaclient.utils.CommonUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Sajjad on 4/24/2018.
 */

@SuppressLint("ValidFragment")
public class OfferFragment extends Fragment {

    List<OfferModel> offerModels;
    RecyclerView offerRecycler;
    OfferAdapter offerAdapter;
    Callback callback;
    ImageView img_ads;
    Ads ads_model;

    public OfferFragment(Callback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer, container, false);
        offerModels = new ArrayList<>();


        offerRecycler = (RecyclerView) view.findViewById(R.id.offerRecycler);
        img_ads=(ImageView)view.findViewById(R.id.slideIv);

        img_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ads_model != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("service_id", ads_model.getServiceId());
                    double temp=ads_model.getDiscount();
                    double discount=(temp/100);
                    bundle.putDouble("discount", discount);
                    callback.ChangeFragment("CruiseFragment", bundle);
                }
            }
        });

        //   LinearLayoutManager LayoutManager =  new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager LayoutManager  = new LinearLayoutManager(getContext());

        offerRecycler.setLayoutManager(LayoutManager);

        offerAdapter = new OfferAdapter(getActivity(), offerModels, callback);
        offerRecycler.setAdapter(offerAdapter);
        boolean flag=CommonUtils.isNetworkConnected(getActivity());
        if (flag) {

            adsScript();
        } else {
            Toast.makeText(getActivity(), "Please check your Internet", Toast.LENGTH_SHORT).show();

        }
        return view;
    }

    public void menuScript() {
        final ProgressDialog progressDialog = CommonUtils.showLoadingDialog(getActivity());
        progressDialog.show();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", 0);
        RequestParams params = new RequestParams();
        params.add("category_id", "1");
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-access-key", "OFEC-8BC8-12OP-434U");
        client.addHeader("x-access-token", sharedPreferences.getString("token", ""));
        client.post("https://habga.herokuapp.com/api/clientServices/menuService", params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e("response UpdateProfile", "start");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = response;
                    int status = jsonObject.getInt("status");
                    if (status == 200) {
                        JSONArray message = jsonObject.getJSONArray("message");
                        for (int i = 0; i < message.length(); i++) {
                            JSONObject data = message.getJSONObject(i);
                            OfferModel of = new OfferModel();
                            of.setId(data.getInt("ServiceID"));
                            of.setTitle(data.getString("Title"));
                            of.setDescription(data.getString("Description"));
                            of.setImage(data.getString("Visual"));
                            offerModels.add(of);
                        }
                        offerAdapter.notifyDataSetChanged();
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

    public void adsScript() {
        final ProgressDialog progressDialog = CommonUtils.showLoadingDialog(getActivity());
        progressDialog.show();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", 0);
        RequestParams params = new RequestParams();
        params.add("catID", "1");
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-access-key", "OFEC-8BC8-12OP-434U");
        client.addHeader("x-access-token", sharedPreferences.getString("token", ""));
        client.post("https://habga.herokuapp.com/api/ads/viewAds", params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e("response UpdateProfile", "start");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = response;
                    int status = jsonObject.getInt("status");
                    if (status == 200) {
                        JSONObject message = jsonObject.getJSONObject("message");
                            ads_model=new Ads();
                            ads_model.setAdsID(message.getInt("AdsID"));
                            ads_model.setAdvertisment(message.getString("advertisment"));
                            ads_model.setServiceId(message.getInt("serviceId"));
                            ads_model.setDiscount(message.getInt("discount"));

                        Picasso.with(getActivity()).load(ads_model.getAdvertisment()).into(img_ads);


                    }

                    menuScript();
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

    public void SearchResult(String msg){
        msg=msg.toLowerCase();
        List<OfferModel> search_offers=new ArrayList<>();
        if(!msg.isEmpty()) {
            for (OfferModel offerModel : offerModels) {
                if (offerModel.getTitle().toLowerCase().contains(msg)) {
                    search_offers.add(offerModel);
                }
            }
        }
        else {
            search_offers=offerModels;
        }

        offerAdapter = new OfferAdapter(getActivity(), search_offers, callback);
        offerRecycler.setAdapter(offerAdapter);
    }

}
