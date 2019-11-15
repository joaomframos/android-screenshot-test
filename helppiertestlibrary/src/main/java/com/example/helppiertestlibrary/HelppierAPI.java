package com.example.helppiertestlibrary;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class HelppierAPI {
    public static void s(Context c,String message){
        Log.i("MyActivty", "test log");
        Toast.makeText(c,message,Toast.LENGTH_SHORT).show();

    }
}
