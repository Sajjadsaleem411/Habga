package com.karigar.hubgaclient.ui;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.pixplicity.easyprefs.library.Prefs;


/**
 * Created by Junaid-Invision on 8/24/2017.
 */

public class Application extends android.app.Application {



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d ("Application Created","Created");


        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();



    }


    public static void closeKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    /*public void unRegisterNetworkChangeReciever() {

        try {
            this.unregisterReceiver(reciever);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/






}
