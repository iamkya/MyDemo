package com.bada.mydemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ContextModel {

    static ContextModel instance = null;

    public static ContextModel getInstance(){

        if(instance == null)
        {
            instance = new ContextModel();
        }

        return instance;
    }

    private Context mContext;

    public void setContext(Context context){
        mContext = context;
    };


    public Context getContext(){
        return mContext;
    }

    public SharedPreferences getPreference() {

        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }
}
