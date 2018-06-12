package com.karigar.hubgaclient.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.RestApi.ApiInterface;
import com.karigar.hubgaclient.RestApi.RetroProvider;
import com.karigar.hubgaclient.utils.CommonUtils;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInActivity extends AppCompatActivity {
    ImageView MainView;
    EditText EmailText, PassText;
    TextView ForgotPass;
    Button Login, Register;
    CheckBox Checkbox;
    String mEmail = "";
    String mPass = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isCheck =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initialize();
        sharedPreferences = getSharedPreferences("user", 0);
        editor = sharedPreferences.edit();

        MainView = (ImageView) findViewById(R.id.Iv);
        final EditText EmailText = (EditText) findViewById(R.id.userText);
        final EditText PassText = (EditText) findViewById(R.id.passText);
        if(sharedPreferences.getBoolean("remember",false)){
            EmailText.setText(sharedPreferences.getString("email",""));
            PassText.setText(sharedPreferences.getString("password",""));
        }
       /* EmailText.setText("muh.sajjadsaleem@gmail.com");
        PassText.setText("sajjad123");*/
        ForgotPass = (TextView) findViewById(R.id.forgotText);
        Login = (Button) findViewById(R.id.login);
        Checkbox = (CheckBox) findViewById(R.id.check_box);


        final String abc = EmailText.getEditableText().toString().trim();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        ForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgotActivity.class);
                startActivity(intent);
            }
        });

        Checkbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            isCheck = isChecked;
                            editor.putBoolean("remember", isChecked);

                    }
                }
        );
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EditText email = (EditText)view.findViewById(R.id.userText);
//                EditText pass = (EditText)view.findViewById(R.id.packed);
                mEmail = EmailText.getText().toString();
                mPass = PassText.getText().toString();
/*
                login(mEmail , mPass);
*/

                boolean checkemail = validateEmail(EmailText, "Please Enter Email", "Invalid Email");
                boolean check = validateForNull(PassText, "Please Enter Password");


                if (checkemail == true && check == true) {
                    if(CommonUtils.isNetworkConnected(SignInActivity.this)) {
                        login(mEmail, mPass);
                    }
                    else {
                        Toast.makeText(SignInActivity.this, "Please check your Internet", Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });

    }

    public void login(final String email, final String pass) {

     /*   Intent intent1 = new Intent(SignInActivity.this, MissOutActivity.class);
        startActivity(intent1);*/

        final String password = pass;
        final ProgressDialog progressDialog = CommonUtils.showLoadingDialog(this);
        progressDialog.show();
        ApiInterface apiService =
                RetroProvider.getClient();
        Call<String> call = apiService.signIn(email, pass);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {

                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response.body());
                    String code = jsonObject.getString("message");
                    JSONArray jsonArray=jsonObject.getJSONArray("user");

                    JSONObject userJson=jsonArray.getJSONObject(0);
                    String name = userJson.getString("UserName");
                    String email = userJson.getString("Email");
                    String mobile = userJson.getString("Mobile");
                    String passwordToken = userJson.getString("password");
                    Prefs.putString("name",name);
                    Prefs.putString("email",mEmail);
                    Prefs.putString("mobile",mobile);
                    Prefs.putString("password",password);
                    Prefs.putString("passwordToken",password);

              //      Toast.makeText(SignInActivity.this, "" + code, Toast.LENGTH_SHORT).show();
                    JSONObject userJsonn=jsonArray.getJSONObject(0);

                    Toast.makeText(SignInActivity.this, "" + code, Toast.LENGTH_SHORT).show();

                    if (code.equals("User logged in")) {

                    //    editor.putString("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJVc2VySUQiOjIxLCJ1c2VyTmFtZSI6Ik1hbGlrIiwiaWF0IjoxNTIyMzk0MDIxfQ.dcjqht3pzVb3MWlzuWJnOh7rrk8tn7Rg1lhO1vd60xY");
                        editor.putString("token", userJsonn.getString("token"));
                        editor.putBoolean("login", true);
                        if(!isCheck&&!sharedPreferences.getString("email","").equals(email)){
                            editor.putBoolean("remember", false);

                        }
                        editor.putString("email", email);
                        editor.putString("password", pass);

                        editor.apply();

                        Intent intent1 = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent1);
                        finish();

                    } else {
                        Toast.makeText(SignInActivity.this, "" + code, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("error", call.toString());
                progressDialog.dismiss();
            }
        });
    }


    private boolean validateEmail(EditText p_editText, String p_nullMsg, String p_invalidMsg) {
        boolean m_isValid = false;
        try {
            if (p_editText != null) {
                if (validateForNull(p_editText, p_nullMsg)) {
                    Pattern m_pattern = Pattern.compile("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})");
                    Matcher m_matcher = m_pattern.matcher(p_editText.getText().toString().trim());
                    if (!m_matcher.matches() && p_editText.getText().toString().trim().length() > 0) {
                        m_isValid = false;
                        p_editText.setError(p_invalidMsg);
                    } else {

                        m_isValid = true;

                    }
                } else {
                    m_isValid = false;
                }
            } else {
                m_isValid = false;
            }
        } catch (Throwable p_e) {
            p_e.printStackTrace(); // Error handling if application crashes
        }
        return m_isValid;
    }


    private boolean validateForNull(EditText p_editText, String p_nullMsg) {
        boolean m_isValid = false;
        try {
            if (p_editText != null && p_nullMsg != null) {
                if (TextUtils.isEmpty(p_editText.getText().toString().trim())) {
                    p_editText.setError(p_nullMsg);
                    m_isValid = false;
                } else {
                    m_isValid = true;
                }
            }
        } catch (Throwable p_e) {
            p_e.printStackTrace(); // Error handling if application crashes
        }
        return m_isValid;
    }


    private void initialize() {
        Register = (Button) findViewById(R.id.rgsbtn);

    }
}

