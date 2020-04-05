package com.gmail.nicku3d.stepcounter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static final String ACTION_RESET_DAILY_STEPS = "ACTION_RESET_DAILY_STEPS";
    static SharedPreferences mPreferences;
    private TextView tvWelcome;

    //stepcounting things
    private SensorManager sensorManager;
    private Sensor sensor;
    static int startingStepCount = 0;
    private static int stepCount = 0;

    private static StepsViewModel stepsViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //shared preferences file name
        String sharedPrefFile = getApplicationContext().getPackageName()+".preferences";
        //getting app context once per create
        Context context = getApplicationContext();

        //Init preferences
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        boolean isUserLoggedIn = mPreferences.getBoolean("isUserLoggedIn", false);

        tvWelcome = findViewById(R.id.tv_welcome);

        if(!isUserLoggedIn) {

            //open login activity
            Intent loginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(loginActivityIntent);
        } else {
            String name = mPreferences.getString("user_name", "Anonymous");
            int height = mPreferences.getInt("user_height", 180);
            int age = mPreferences.getInt("user_age", 18);

            tvWelcome.setText("Welcome "+name+ ", your height is: "+ height + "cm and " +
                    "you are "+ age + " years old. Have good time here.");
        }


        startingStepCount = mPreferences.getInt("starting_step_count", 0);
        //sensor init
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        setAlarmManager(context);


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final StepsListAdapter adapter = new StepsListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        stepsViewModel = new ViewModelProvider(this).get(StepsViewModel.class);

        stepsViewModel.getAllDailySteps().observe(this, new Observer<List<DailySteps>>() {
            @Override
            public void onChanged(@Nullable final List<DailySteps> steps) {
                adapter.setSteps(steps);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        //maybe its better to use onSavedInstance or sth
        String name = mPreferences.getString("user_name", "Anonymous");
        int height = mPreferences.getInt("user_height", 180);
        int age = mPreferences.getInt("user_age", 18);

        tvWelcome.setText("Welcome "+ name + ", your height is: "+ height + "cm and " +
                "you are "+ age + " years old. Have good time here.");

        //sensor stuff
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensor != null) {
            sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        findSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt("starting_step_count", startingStepCount);
        preferencesEditor.apply();
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        if(e.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            int rebootSteps = (int)e.values[0];

            if (startingStepCount < 1) {
                // initial value
                startingStepCount = rebootSteps;
            }


            // Calculate steps taken based on first counter value received.
            stepCount = rebootSteps - startingStepCount;
            TextView showStepCount = findViewById(R.id.tv_steps_count);
            //show steps since reboot
            ((TextView)findViewById(R.id.tv_steps_reboot)).setText(Integer.toString(rebootSteps));
            showStepCount.setText(Integer.toString(stepCount));
            //show number of starting steps
            ((TextView)findViewById(R.id.tv_starting_steps)).setText(Integer.toString(startingStepCount));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setAlarmManager(Context context){

        boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent(ACTION_RESET_DAILY_STEPS),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp)
        {
            Log.d("myTag", "Alarm is already active");
        }
        else {
            //alarmManager to reset steps at the end of the day
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction(ACTION_RESET_DAILY_STEPS);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }

    public void findSensor(){
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensor != null) {
            sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void insertDailySteps(){
        DailySteps dailySteps = new DailySteps(stepCount, new Date().toString());
        stepsViewModel.insert(dailySteps);
    }
}
