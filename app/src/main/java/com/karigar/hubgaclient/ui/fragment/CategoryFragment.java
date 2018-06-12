package com.karigar.hubgaclient.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karigar.hubgaclient.Model.Ads;
import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.ui.Callback;
import com.karigar.hubgaclient.utils.CommonUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 4/24/2018.
 */

@SuppressLint("ValidFragment")
public class CategoryFragment extends Fragment {
    ImageView MenuImg, HomImg, Img1, Img2, Img3, Img4, Img5;
    //  EditText SerchText;
    TextView text1, text2, text3, text4, text5;
    RelativeLayout Advanture, Indoore, Outdoor, Home;
    LinearLayout all;
    Callback callBack;

    ImageView img_ads;
    Ads ads_model;

    public CategoryFragment(Callback back) {
        callBack = back;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        //     MenuImg = (ImageView)view.findViewById(R.id.Iv);
//        HomImg = (ImageView) findViewById(R.id.Iv01);
        Advanture = (RelativeLayout) view.findViewById(R.id.rl2);
        Indoore = (RelativeLayout) view.findViewById(R.id.rl3);
        Outdoor = (RelativeLayout) view.findViewById(R.id.rl4);
        Home = (RelativeLayout) view.findViewById(R.id.rl5);
        all = (LinearLayout) view.findViewById(R.id.rl6);
        img_ads = (ImageView) view.findViewById(R.id.mainIv);

        img_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ads_model != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("service_id", ads_model.getServiceId());
                    double discount=ads_model.getDiscount()/100;
                    bundle.putDouble("discount", discount);
                    callBack.ChangeFragment("CruiseFragment", bundle);
                }
            }
        });


        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("category"," All habgas");
                callBack.ChangeFragment("OfferFragment", bundle);
            }
        });
        Advanture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("category"," Advanture habgas");
                callBack.ChangeFragment("OfferFragment", bundle);
            }
        });
        Indoore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("category"," Indoor habgas");
                callBack.ChangeFragment("OfferFragment", bundle);
            }
        });
        Outdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putString("category"," Outdoor habgas");
                callBack.ChangeFragment("OfferFragment", bundle);
            }
        });
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putString("category"," Home habgas");
                callBack.ChangeFragment("OfferFragment", bundle);
            }
        });
        boolean flag=CommonUtils.isNetworkConnected(getActivity());
        if (flag) {

            adsScript();
        } else {
            Toast.makeText(getActivity(), "Please check your Internet", Toast.LENGTH_SHORT).show();

        }
        return view;
    }

    public void adsScript() {
        final ProgressDialog progressDialog = CommonUtils.showLoadingDialog(getActivity());
        progressDialog.show();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", 0);
        RequestParams params = new RequestParams();
        params.add("catID", "0");
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
                        ads_model = new Ads();
                        ads_model.setAdsID(message.getInt("AdsID"));
                        ads_model.setAdvertisment(message.getString("advertisment"));
                        ads_model.setServiceId(message.getInt("serviceId"));
                        ads_model.setDiscount(message.getInt("discount"));
                        Picasso.with(getActivity()).load(ads_model.getAdvertisment()).into(img_ads);


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
