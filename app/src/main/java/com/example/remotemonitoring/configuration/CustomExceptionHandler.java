package com.example.remotemonitoring.configuration;

import android.util.Log;

import androidx.annotation.NonNull;

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler{

    private Thread.UncaughtExceptionHandler oldHandler;

    public CustomExceptionHandler(){
        oldHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        Log.d("CustomException", e.getMessage());
        if(oldHandler!=null){
            oldHandler.uncaughtException(t,e);
        }
    }
}
