package com.gmail.nicku3d.stepcounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        //after receiving broadcast reset step count to 0 and save one from previous day
        if(intent.getAction().equals(MainActivity.ACTION_RESET_DAILY_STEPS)) {
            Log.d(TAG, "Daily step count reseted");
            MainActivity.startingStepCount = 0;
            //saving dailysteps to database!
            MainActivity.insertDailySteps();
        }
    }
}
