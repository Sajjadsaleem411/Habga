package com.karigar.hubgaclient.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.RestApi.ApiInterface;
import com.karigar.hubgaclient.RestApi.RetroProvider;
import com.karigar.hubgaclient.ui.Callback;
import com.karigar.hubgaclient.ui.activity.RegisterActivity;
import com.karigar.hubgaclient.ui.activity.SignInActivity;
import com.karigar.hubgaclient.utils.CommonUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;

/**
 * Created by Muhammad Sajjad on 6/2/2018.
 */

@SuppressLint("ValidFragment")
public class UpdateUserFragment extends Fragment {
    Callback callBack;
    EditText Nmetext, Emailtext, Mobletext, Passtext;
    Button Register;

    SharedPreferences sharedPreferences;
    String mName = "";
    String mEmail = "";
    String mPassword = "";
    String mMobile = "";
    String mUsername = "";
    public String mVerifyCode = "";
    Context mContext;

    public UpdateUserFragment(Callback back) {
        callBack = back;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_user, container, false);

        mContext = getActivity();

        Nmetext = (EditText) view.findViewById(R.id.nameText);
        Emailtext = (EditText) view.findViewById(R.id.mailText);
        Mobletext = (EditText) view.findViewById(R.id.mobText);
        Passtext = (EditText) view.findViewById(R.id.passText);
        Register = (Button) view.findViewById(R.id.btnRegister);

        String name = Prefs.getString("name","");
        String email = Prefs.getString("email","");
        String mobile = Prefs.getString("mobile","");
        String password = Prefs.getString("password","");

        Nmetext.setText(name);
        Emailtext.setText(email);
        Mobletext.setText(mobile);
        Passtext.setText(password);

        sharedPreferences = mContext.getSharedPreferences("user", 0);

        Emailtext.setText(sharedPreferences.getString("email", ""));
        final String abc = Emailtext.getEditableText().toString().trim();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mName = Nmetext.getText().toString();
                mEmail = Emailtext.getText().toString();
                mMobile = Mobletext.getText().toString();
                mPassword = Passtext.getText().toString();
/*

                Emailtext.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {

                        if (abc.matches(emailPattern) && s.length() > 0) {
                            Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
                            // or
                            Emailtext.setText("valid email");
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                            //or
                            Emailtext.setText("invalid email");
                        }
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // other stuffs
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // other stuffs
                    }
                });
*/


                if (mName.isEmpty()) {
                    Toast.makeText(mContext, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else if (mEmail.isEmpty()) {
                    Toast.makeText(mContext, "Please Enter Email", Toast.LENGTH_SHORT).show();
                } else if (mMobile.isEmpty()) {
                    Toast.makeText(mContext, "Please Enter Mobile", Toast.LENGTH_SHORT).show();
                } else if (mPassword.isEmpty()) {
                    Toast.makeText(mContext, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    boolean flag=CommonUtils.isNetworkConnected(getActivity());
                    if (flag) {
                        UserUpdate(mName, mEmail, mPassword, mName, mMobile);
                    } else {
                        Toast.makeText(getActivity(), "Please check your Internet", Toast.LENGTH_SHORT).show();

                    }

                    // showAlertDialog();

                }
            }
        });


        return view;
    }


    public void UserUpdate(String Name, String Email, String Password, String UserName, String MobileNum) {
        final ProgressDialog progressDialog = CommonUtils.showLoadingDialog(getActivity());

        progressDialog.show();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", 0);
        RequestParams params = new RequestParams();

        params.add("name", Name);
        params.add("email", Email);
        params.add("password", Password);
        params.add("username", UserName);
        params.add("mobile", MobileNum);

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-access-key", "OFEC-8BC8-12OP-434U");
        client.addHeader("x-access-token", sharedPreferences.getString("token", ""));
        client.post("https://habga.herokuapp.com/api/profile/updateUser", params, new JsonHttpResponseHandler() {
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

                    String message = jsonObject.getString("message");

                    Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();

                    if (status == 200) {
                        startActivity(new Intent(getActivity(), SignInActivity.class));

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
