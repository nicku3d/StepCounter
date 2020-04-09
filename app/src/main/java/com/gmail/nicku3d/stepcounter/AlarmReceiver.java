package com.gmail.nicku3d.stepcounter;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;


public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        //after receiving broadcast reset step count to 0 and save one from previous day
        if(intent.getAction() != null && intent.getAction().equals(MainActivity.ACTION_RESET_DAILY_STEPS)) {
            Log.d(TAG, "ACTION RECEIVED!");

            String sharedPrefFile = intent.getStringExtra("sharedPrefFile");
            //saving dailysteps to database!
            //TODO: save only day month and year

            saveStepsToDatabase(context);
            resetStartingStepCount(context, sharedPrefFile);


            Log.d(TAG, "steps saved");
        }
    }

    void saveStepsToDatabase(Context context){
        StepsRepository stepsRepository = new StepsRepository(context);
        DailySteps steps = new DailySteps(MainActivity.stepCount, new Date().toString());
        stepsRepository.insert(steps);
    }

    void resetStartingStepCount(Context context, String sharedPrefFile){
        MainActivity.startingStepCount = 0; //because when its equal to 0 it is given new value
        //changing shared prefs too if app is killed
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putInt("starting_step_count", 0)
                .apply();

    }
}
