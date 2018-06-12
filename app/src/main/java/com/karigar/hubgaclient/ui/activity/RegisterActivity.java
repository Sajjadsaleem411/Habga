package com.karigar.hubgaclient.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.RestApi.ApiInterface;
import com.karigar.hubgaclient.RestApi.RetroProvider;
import com.karigar.hubgaclient.utils.CommonUtils;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    ImageView MainView;
    EditText Nmetext, Emailtext, Mobletext, Passtext;
    Button Register;

    String mName = "";
    String mEmail = "";
    String mPassword = "";
    String mMobile = "";
    String mUsername = "";
    public String mVerifyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        MainView = (ImageView) findViewById(R.id.Iv);

        Nmetext = (EditText) findViewById(R.id.nameText);


        Emailtext = (EditText) findViewById(R.id.mailText);

        Mobletext = (EditText) findViewById(R.id.mobText);


        Passtext = (EditText) findViewById(R.id.passText);


        Register = (Button) findViewById(R.id.btnRegister);

        final String abc = Emailtext.getEditableText().toString().trim();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mName = Nmetext.getText().toString();
                mEmail = Emailtext.getText().toString();
                mMobile = Mobletext.getText().toString();
                mPassword = Passtext.getText().toString();

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


                if (mName.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else if (mEmail.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                } else if (mMobile.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Mobile", Toast.LENGTH_SHORT).show();
                } else if (mPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else {

                    if(CommonUtils.isNetworkConnected(RegisterActivity.this)) {
                        register(mName, mEmail, mPassword, mName, mMobile);
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Please check your Internet", Toast.LENGTH_SHORT).show();

                    }
                    // showAlertDialog();

                }


            }
        });

    }

    public void register(String Name, String Email, String Password, String UserName, String MobileNum) {

        try {
            final ProgressDialog progressDialog = CommonUtils.showLoadingDialog(this);
            progressDialog.show();
            ApiInterface apiService =
                    RetroProvider.getClient();
            Call<String> call = apiService.register(Name, Email, Password, MobileNum);
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

                        Toast.makeText(RegisterActivity.this, "" + message, Toast.LENGTH_SHORT).show();

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

        } catch (Exception ex) {
            Log.e("error", ex.toString());
        }
    }

    public void showAlertDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.NewDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_confirmation, null);
        dialogBuilder.setView(dialogView);
        final ImageView Crossbtn = (ImageView) dialogView.findViewById(R.id.Iv);
        final AlertDialog b = dialogBuilder.create();
        final Button submitbtn = (Button) dialogView.findViewById(R.id.btn);
        final EditText ConfText = (EditText) dialogView.findViewById(R.id.eText);

        b.show();
        Crossbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });


        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mVerifyCode = ConfText.getText().toString();


                if (mVerifyCode.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Code", Toast.LENGTH_SHORT).show();

                } else {
                    int verifyCode = Integer.parseInt(ConfText.getText().toString());
                    verify(mEmail, verifyCode);
                }


            }
        });
    }

    public void verify(String email, int verifycode) {
        try {
            final ProgressDialog progressDialog = CommonUtils.showLoadingDialog(this);
            progressDialog.show();
            ApiInterface apiService = RetroProvider.getClient();
            Call<String> call = apiService.verifyCode(email.toString(), verifycode);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response.body());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        Toast.makeText(RegisterActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                        if (status.equals("200")) {
                            Prefs.putString("name",mName);
                            Prefs.putString("email",mEmail);
                            Prefs.putString("mobile",mMobile);
                            Prefs.putString("password",mPassword);
                            Intent intent = new Intent(RegisterActivity.this, MissOutActivity.class);
                            startActivity(intent);
                        } else {
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
        } catch (Exception ex) {
            Log.e("error", ex.toString());
        }
    }

    public void showErrorAlertDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.NewDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_resend_conf_cod, null);
        dialogBuilder.setView(dialogView);
        final ImageView Crossbtn = (ImageView) dialogView.findViewById(R.id.Iv);
        final EditText ConfText = (EditText) dialogView.findViewById(R.id.eText);
        final AlertDialog r = dialogBuilder.create();
        final Button submitbtn = (Button) dialogView.findViewById(R.id.btn);
        final Button Resendbtn = (Button) dialogView.findViewById(R.id.btn1);

        r.show();
        Crossbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r.dismiss();
            }
        });



        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mVerifyCode = ConfText.getText().toString();
                if (mVerifyCode.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Code", Toast.LENGTH_SHORT).show();
                } else {
                    verify(mEmail, Integer.parseInt(ConfText.getText().toString()));
                }

            }
        });
        mEmail = Emailtext.getText().toString();
        Resendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ResendCode(mEmail);
            }
        });
    }

    public void ResendCode(String Email) {
        final ProgressDialog progressDialog = CommonUtils.showLoadingDialog(this);
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
                    Prefs.putString("name",mName);
                    Prefs.putString("email",mEmail);
                    Prefs.putString("mobile",mMobile);
                    Prefs.putString("password",mPassword);
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

