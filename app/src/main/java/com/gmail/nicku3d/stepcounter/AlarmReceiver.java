package com.gmail.nicku3d.stepcounter;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;


public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        //after receiving broadcast reset step count to 0 and save one from previous day
        if(intent.getAction() != null && intent.getAction().equals(MainActivity.ACTION_RESET_DAILY_STEPS)) {
            Log.d(TAG, "ACTION RECEIVED!");

            //saving dailysteps to database!
            //TODO: save only day month and year
            StepsRepository stepsRepository = new StepsRepository(context);
            MainActivity.startingStepCount = 0; //because when its equal to 0 it is given new value
            DailySteps steps = new DailySteps(MainActivity.stepCount, new Date().toString());
            stepsRepository.insert(steps);
            Log.d(TAG, "steps saved");
        }
    }
}
