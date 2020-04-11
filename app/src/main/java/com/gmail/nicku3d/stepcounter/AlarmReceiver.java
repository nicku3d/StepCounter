package com.gmail.nicku3d.stepcounter;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getName();
    private String sharedPrefFile;
    private StepCounter stepCounter;
    @Override
    public void onReceive(Context context, Intent intent) {
        //after receiving broadcast reset step count to 0 and save one from previous day
        if(intent.getAction() != null && intent.getAction().equals(MainActivity.ACTION_RESET_DAILY_STEPS)) {
            Log.d(TAG, "ACTION "+ MainActivity.ACTION_RESET_DAILY_STEPS +" RECEIVED!");
            sharedPrefFile = intent.getStringExtra("sharedPrefFile");
            SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
            int startingStepCount = sharedPreferences.getInt("starting_step_count", 0);

            stepCounter = StepCounter.getStepCounter();
            stepCounter.setStartingStepCountValue(startingStepCount);

            saveStepsToDatabase(context);
            resetStartingStepCount(context);
            Log.d(TAG, "steps saved and reset!");
        }
    }

    void saveStepsToDatabase(Context context){
        int stepCount = stepCounter.getStepCountValue();
        StepsRepository stepsRepository = new StepsRepository(context);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        DailySteps steps = new DailySteps(stepCount, date);
        stepsRepository.insert(steps);
    }

    void resetStartingStepCount(Context context){
        stepCounter.setStartingStepCountValue(0); //because when its equal to 0 it is given new value
        //changing shared prefs too if app is killed
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putInt("starting_step_count", 0)
                .apply();

    }
}
