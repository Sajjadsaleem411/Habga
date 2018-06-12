package com.karigar.hubgaclient.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
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

public class ResetPassActivity extends AppCompatActivity {
    ImageView MainLogo,RstLogo;
    TextView txt1;
    EditText RstPass,ConfPass;
    Button SavePass;

    String mRstPass = "";
    String mConfPass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        MainLogo = (ImageView)findViewById(R.id.Iv);
        RstLogo = (ImageView)findViewById(R.id.Iv1);
        txt1 = (TextView)findViewById(R.id.restText);
        RstPass = (EditText)findViewById(R.id.passtext);
        ConfPass = (EditText)findViewById(R.id.passtext1);
        SavePass = (Button)findViewById(R.id.rstbtn);

        ConfPass.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(mRstPass.equals(mConfPass)) {


                }
                else
                    {
                        Toast.makeText(ResetPassActivity.this , "Password Does'nt Match" , Toast.LENGTH_SHORT).show();
                    }
            }
        });




        SavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRstPass = RstPass.getText().toString();
                mConfPass = ConfPass.getText().toString();

                if(mRstPass.isEmpty() && mConfPass.isEmpty()){
                    Toast.makeText(ResetPassActivity.this, "Enter New Password", Toast.LENGTH_SHORT).show();
                }else if(mRstPass.isEmpty()){
                    Toast.makeText(ResetPassActivity.this, "Enter Reset Password", Toast.LENGTH_SHORT).show();
                } else if(mConfPass.isEmpty()){
                    Toast.makeText(ResetPassActivity.this, "Enter Confirmation Password", Toast.LENGTH_SHORT).show();
              } else {
                    if(CommonUtils.isNetworkConnected(ResetPassActivity.this)) {
                        resetPassword();
                    }
                    else {
                        Toast.makeText(ResetPassActivity.this, "Please check your Internet", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });


    }
    public void resetPassword(){

        try {
            ApiInterface apiService =
                    RetroProvider.getClient();
            Call<String> call = apiService.resetCode( "email","resetCode");
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("200") ) {

                            /* Intent intent = new Intent(ResetPassActivity.this,CategoryActivity.class);
                             startActivity(intent);*/
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("error", call.toString());
                }
            });

        }
        catch (Exception ex)
        {
            Log.e("error",ex.toString());
        }

    }
 }
