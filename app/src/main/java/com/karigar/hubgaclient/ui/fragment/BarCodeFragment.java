package com.karigar.hubgaclient.ui.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.karigar.hubgaclient.Model.TicketModel;
import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.utils.CommonUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 5/2/2018.
 */

public class BarCodeFragment extends Fragment {
    TextView tv_name, tv_date, tv_time;
    ImageView img_qrcode;
    String encodeImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_barcode, container, false);
        tv_name = (TextView) view.findViewById(R.id.Vtext1);
        tv_date = (TextView) view.findViewById(R.id.Vtext2);
        tv_time = (TextView) view.findViewById(R.id.Vtext3);
        img_qrcode = (ImageView) view.findViewById(R.id.Qrcode);
      /*  Bundle bundle = this.getArguments();
        if (bundle != null) {
            tv_name.setText(bundle.getString("Name"));
            tv_date.setText(bundle.getString("Date"));
            tv_time.setText(bundle.getString("Time"));
        }*/
        boolean flag=CommonUtils.isNetworkConnected(getActivity());
        if (flag) {

            getData();
        } else {
            Toast.makeText(getActivity(), "Please check your Internet", Toast.LENGTH_SHORT).show();

        }
        return view;
    }

    public void getData() {

        final ProgressDialog progressDialog = CommonUtils.showLoadingDialog(getActivity());
        progressDialog.show();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", 0);
        RequestParams params = new RequestParams();
        //    params.add("category_id", "1");

        Bundle bundle = this.getArguments();
        params.add("ticket_id", String.valueOf(bundle.getInt("ticket_id", 0)));
        //     params.add("locale", "en");
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-access-key", "OFEC-8BC8-12OP-434U");
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.addHeader("x-access-token", sharedPreferences.getString("token", ""));
        client.addHeader("locale", "en");
        client.post("https://habga.herokuapp.com/api/ticket/fetchTicket", params, new JsonHttpResponseHandler() {
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

                        tv_name.setText(jsonObject.getString("name"));
                        JSONArray message = jsonObject.getJSONArray("message");

                        JSONObject data = message.getJSONObject(0);
                        TicketModel ticketModel = new TicketModel();
                        ticketModel.setTicketID(data.getInt("ticketID"));
                        String date = data.getString("BookingDate");

                        if (date != null || date.equals("null")) {
                            tv_time.setText(GiveTime(date));
                            tv_date.setText(DateString(date));
                        }
                        if (data.getString("t_img") != null) {
                            encodeImage = data.getString("t_img");
                            img_qrcode.setImageBitmap(convert(encodeImage));
                        }
                        JSONObject ticket_image = data.getJSONObject("ticket_image");

                        ticketModel.setType(ticket_image.getString("type"));
                        JSONArray numbers = ticket_image.getJSONArray("data");

                       /* ticketModel.data = new int[numbers.length()];
                        for (int j = 0; j < numbers.length(); j++) {
                            ticketModel.data[j]=numbers.getInt(j);
                        }*/

                    /*    ByteBuffer byteBuffer = ByteBuffer.allocate(ticketModel.data.length * 4);
                        IntBuffer intBuffer = byteBuffer.asIntBuffer();
                        intBuffer.put(ticketModel.data);
                        byte[] array = byteBuffer.array();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, ticketModel.data.length);
*/
                        /*Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
                        // vector is your int[] of ARGB
                        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(ticketModel.data));
*/
                        // Create bitmap
                        //                      Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

// Set the pixels
                        //      bitmap.setPixels(ticketModel.data, 0, 100, 0, 0, 100, 100);*/
                    }

                    progressDialog.dismiss();

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

    public Bitmap convert(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
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
}
