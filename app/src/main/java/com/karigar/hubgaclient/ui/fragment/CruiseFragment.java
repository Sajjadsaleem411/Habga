package com.karigar.hubgaclient.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.ui.Callback;
import com.karigar.hubgaclient.utils.CommonUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 5/2/2018.
 */

@SuppressLint("ValidFragment")
public class CruiseFragment extends Fragment {
    ImageView menuImg, HomImg, LogoImg, DetailImg, timeImg, DateImg, LocatoinImg, PriceImg, howmanyImg, plusImg, minusImg;
    TextView title, contText, detText, timeFrom, timeTo, dateText, locText, PriText, howmanyText;
    Callback callback;
    ProgressDialog progressDialog;
    Button btn_book;
    String name, date, time;

    public CruiseFragment(Callback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cruise, container, false);
       /* HomImg = (ImageView) view.findViewById(R.id.Iv01);
        menuImg = (ImageView)view.findViewById(R.id.iIm);*/
        LogoImg = (ImageView) view.findViewById(R.id.img_cruise_logo);
     /*   DetailImg = (ImageView) view.findViewById(R.id.Iv);
        timeImg = (ImageView) view.findViewById(R.id.Iv1);
        DateImg = (ImageView) view.findViewById(R.id.Iv2);
        LocatoinImg = (ImageView) view.findViewById(R.id.Iv3);
        PriceImg = (ImageView) view.findViewById(R.id.Iv4);
        howmanyImg = (ImageView) view.findViewById(R.id.Iv5);*/
        plusImg = (ImageView) view.findViewById(R.id.Iv7);
        minusImg = (ImageView) view.findViewById(R.id.Iv6);
        title = (TextView) view.findViewById(R.id.tv_cruise_title);
        detText = (TextView) view.findViewById(R.id.tv_cruise_detail);
        timeFrom = (TextView) view.findViewById(R.id.tv_cruise_time);
        timeTo = (TextView) view.findViewById(R.id.Text9);
        dateText = (TextView) view.findViewById(R.id.tv_cruise_date);
        locText = (TextView) view.findViewById(R.id.tv_cruise_location);
        PriText = (TextView) view.findViewById(R.id.tv_cruise_price);
        howmanyText = (TextView) view.findViewById(R.id.tv_cruise_coming);
        contText = (TextView) view.findViewById(R.id.tv_cruise_count);
        btn_book = (Button) view.findViewById(R.id.btn_cruise_book);
        progressDialog = CommonUtils.showLoadingDialog(getActivity());
        boolean flag=CommonUtils.isNetworkConnected(getActivity());
        if (flag) {
            cruiseScript();
        } else {
            Toast.makeText(getActivity(), "Please check your Internet", Toast.LENGTH_SHORT).show();

        }


      /*  HomImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               *//* Intent intent = new Intent(getActivity(),CategoryActivity.class);
                startActivity(intent);*//*
            }
        });*/
        plusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(contText.getText().toString());
                count++;
                contText.setText(String.valueOf(count));
            }
        });
        minusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(contText.getText().toString());
                if (count != 0) {
                    count--;
                    contText.setText(String.valueOf(count));
                }
            }
        });

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!contText.getText().toString().equals("0"))
                    bookingScript();
                else
                    Toast.makeText(getActivity(), "Please choose atleast one person", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void cruiseScript() {
        progressDialog.show();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", 0);
        RequestParams params = new RequestParams();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            params.add("service_id", String.valueOf(bundle.getInt("service_id", 0)));
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-access-key", "OFEC-8BC8-12OP-434U");
        client.addHeader("x-access-token", sharedPreferences.getString("token", ""));
        client.post("https://habga.herokuapp.com/api/clientServices/fetchService", params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e("response UpdateProfile", "start");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = response;
                    int status = jsonObject.getInt("status");
                    if (status == 200) {
                        JSONArray message = jsonObject.getJSONArray("message");
                        if (message.length() > 0) {
                            JSONObject jsondata = message.getJSONObject(0);
                            detText.setText(jsondata.getString("Description"));
                            locText.setText(jsondata.getString("Location"));
                            int actual_price = Integer.parseInt(jsondata.getString("Price"));
                            double price = actual_price - actual_price * getArguments().getDouble("discount");
                            PriText.setText("" + price);
                            name = jsondata.getString("Title");
                            date = DateString(jsondata.getString("DateFrom"));
                            time = GiveTime(jsondata.getString("DateFrom"));
                            if (time != null)
                                timeFrom.setText("" + time);
                            if (jsondata.getString("DateTo") != null && !jsondata.getString("DateTo").equals("null"))
                                timeTo.setText("" + GiveTime(jsondata.getString("DateTo")));
                            if (date != null)
                                dateText.setText("" + date);

                            //     Log.d("Test_time_from",GiveTime(jsondata.getString("DateFrom")));
                        /*    if(jsondata.getString("DateTo")!=null)
                            Log.d("Test_time_to",GiveTime(jsondata.getString("DateTo")));*/
                            //   GiveDate(jsondata.getString("DateFrom"));
                        }
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

    public Date GiveDate(String timestamp) {
        Date date = null;
        try {

            CommonUtils.Dateformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = CommonUtils.Dateformat.parse(timestamp);
            Log.d("Test_Date", "" + date);


        } catch (Exception e) {

        }
        return date;
    }

    public String GiveTime(String timestamp) {
        Date date = GiveDate(timestamp);

        Log.d("Test_Date", "" + date);

        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = localDateFormat.format(date);

        Log.d("Test_Date", "" + time);

        return time;
    }

    public String DateString(String timestamp) {
        Date date = GiveDate(timestamp);

        Log.d("Test_Date", "" + date);

        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = localDateFormat.format(date);

        Log.d("Test_Date", "" + time);

        return time;
    }

    public void bookingScript() {
        progressDialog.show();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", 0);
        RequestParams params = new RequestParams();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            params.add("service_id", String.valueOf(bundle.getInt("service_id", 0)));
        }
        params.add("count", contText.getText().toString());
        params.add("price", PriText.getText().toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-access-key", "OFEC-8BC8-12OP-434U");
        client.addHeader("x-access-token", sharedPreferences.getString("token", ""));
        client.post("https://habga.herokuapp.com/api/clientServices/booking", params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e("response UpdateProfile", "start");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = response;
                    //        int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");
                    if (message.equals("booking & Ticket created Successfully")) {
                        Toast.makeText(getActivity(), "Booking successfully created", Toast.LENGTH_SHORT).show();
                        /*startActivity(new Intent(getActivity(),PaymentActivity.class));*/
                        JSONArray tickets = jsonObject.getJSONArray("tickets");
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("Name", name);
                        bundle1.putString("Date", date);
                        bundle1.putString("Time", time);
                        bundle1.putInt("ticket_id", tickets.getInt(0));
                        callback.ChangeFragment("PaymentFragment", bundle1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });

    }

}
