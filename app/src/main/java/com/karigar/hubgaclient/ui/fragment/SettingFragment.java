package com.karigar.hubgaclient.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.ui.Callback;

/**
 * Created by Muhammad Sajjad on 6/2/2018.
 */

@SuppressLint("ValidFragment")
public class SettingFragment extends Fragment {
    Callback callBack;
    LinearLayout edit_layout;

    public SettingFragment(Callback back){
        callBack=back;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_setting, container, false);
        edit_layout=view.findViewById(R.id.logout_edit);
        edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.ChangeFragment("UpdateUserFragment",null);
            }
        });

        return view;
    }


}
