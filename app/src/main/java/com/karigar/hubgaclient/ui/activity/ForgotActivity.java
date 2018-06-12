package com.karigar.hubgaclient.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.RestApi.ApiInterface;
import com.karigar.hubgaclient.RestApi.RetroProvider;
import com.karigar.hubgaclient.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends AppCompatActivity {
    ImageView MainLogo,TextViewImg;
    EditText mailText;
    TextView forgotText;
    Button RstPass;

    String mEmail = "";
    public String mVerifyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        MainLogo = (ImageView)findViewById(R.id.Iv);
        TextViewImg = (ImageView)findViewById(R.id.Iv1);
        forgotText = (TextView) findViewById(R.id.restText);
        mailText = (EditText)findViewById(R.id.Ltext);
        RstPass = (Button) findViewById(R.id.resetbtn);



        RstPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mEmail = mailText.getText().toString();

                if (mEmail.isEmpty()){
                    Toast.makeText(ForgotActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                }else{
                    if(CommonUtils.isNetworkConnected(ForgotActivity.this)) {
                        forgot(mEmail.toString());
                    }
                    else {
                        Toast.makeText(ForgotActivity.this, "Please check your Internet", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });
    }
    public void forgot(String email) {
        try {
            final ProgressDialog progressDialog= CommonUtils.showLoadingDialog(this);
            progressDialog.show();
            ApiInterface apiService = RetroProvider.getClient();
            Call<String> call = apiService.forgot(email.toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response.body());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("200")) {
                            showAlertDialog();
                        }
                        else{
                            showErrorAlertDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    progressDialog.dismiss();
                    Log.e("error", call.toString());
                }
            });
        }
        catch (Exception ex)
        {
            Log.e("error",ex.toString());
        }
    }

    public void showAlertDialog(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.NewDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_confirmation, null);
        dialogBuilder.setView(dialogView);
        final ImageView Crossbtn = (ImageView) dialogView.findViewById(R.id.Iv);
        final AlertDialog b = dialogBuilder.create();
        final Button submitbtn = (Button)dialogView.findViewById(R.id.btn);
        final EditText ConfText = (EditText) dialogView.findViewById(R.id.eText);

        b.show();
        Crossbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {b.dismiss();
            }
        });





        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mVerifyCode=ConfText.getText().toString();
                int verifyCode = Integer.parseInt(mVerifyCode);

                if (mVerifyCode.isEmpty()) {
                    Toast.makeText(ForgotActivity.this, "Please Enter Code", Toast.LENGTH_SHORT).show();

                }else
                {
                    resetCode(mEmail, verifyCode);
                }


            }
        });
    }


    public void resetCode(String email , int verifycode) {
        try {
            final ProgressDialog progressDialog= CommonUtils.showLoadingDialog(this);
            progressDialog.show();
            ApiInterface apiService = RetroProvider.getClient();
            Call<String> call = apiService.verifyCode(email.toString(),verifycode);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response.body());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        String email = jsonObject.getString("email");
                        String resetCode = jsonObject.getString("reset_code");


                        if (status.equals("200")) {
                            Intent intent = new Intent(ForgotActivity.this,ResetPassActivity.class);
                            startActivity(intent);
                        }
                        else{
                            showErrorAlertDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("error", call.toString());
                }
            });
        }
        catch (Exception ex)
        {
            Log.e("error",ex.toString());
        }
    }

    public void showErrorAlertDialog(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.NewDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_resend_conf_cod, null);
        dialogBuilder.setView(dialogView);
        final ImageView Crossbtn = (ImageView) dialogView.findViewById(R.id.Iv);
        final EditText ConfText = (EditText) dialogView.findViewById(R.id.eText);
        final AlertDialog r = dialogBuilder.create();
        final Button submitbtn = (Button)dialogView.findViewById(R.id.btn);
        final Button Resendbtn = (Button)dialogView.findViewById(R.id.btn1);

        r.show();
        Crossbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {r.dismiss();
            }
        });


        mVerifyCode=ConfText.getText().toString();

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVerifyCode=ConfText.getText().toString();


                if (mVerifyCode.isEmpty()) {
                    Toast.makeText(ForgotActivity.this, "Please Enter Code", Toast.LENGTH_SHORT).show();
                }else
                {
                    resetCode(mEmail, Integer.parseInt(ConfText.getText().toString()));
                }

            }
        });

        Resendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResendCode(mEmail);
            }
        });
    }

    public void ResendCode(String Email){
        final ProgressDialog progressDialog= CommonUtils.showLoadingDialog(this);
        progressDialog.show();
        ApiInterface apiService =
                RetroProvider.getClient();
        Call<String> call = apiService.resendCode(Email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response.body());
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error", call.toString());
            }
        });
    }
}
