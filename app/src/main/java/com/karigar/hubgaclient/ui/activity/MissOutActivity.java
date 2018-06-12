package com.karigar.hubgaclient.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.karigar.hubgaclient.R;

public class MissOutActivity extends AppCompatActivity {
    ImageView LogoView;
    TextView text1,text2;
    Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miss_out);

        LogoView = (ImageView)findViewById(R.id.Iv);
        text1 = (TextView)findViewById(R.id.Vtext1);
        text2 = (TextView)findViewById(R.id.Vtext2);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MissOutActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MissOutActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }


}
